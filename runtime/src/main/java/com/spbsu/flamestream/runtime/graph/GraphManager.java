package com.spbsu.flamestream.runtime.graph;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.pattern.PatternsCS;
import com.spbsu.flamestream.core.DataItem;
import com.spbsu.flamestream.core.Graph;
import com.spbsu.flamestream.core.graph.Grouping;
import com.spbsu.flamestream.core.graph.Sink;
import com.spbsu.flamestream.core.graph.Source;
import com.spbsu.flamestream.runtime.master.acker.LocalAcker;
import com.spbsu.flamestream.runtime.master.acker.api.Heartbeat;
import com.spbsu.flamestream.runtime.master.acker.api.MinTimeUpdate;
import com.spbsu.flamestream.runtime.master.acker.api.commit.GimmeTime;
import com.spbsu.flamestream.runtime.master.acker.api.commit.LastCommit;
import com.spbsu.flamestream.runtime.master.acker.api.commit.Prepare;
import com.spbsu.flamestream.runtime.master.acker.api.commit.Prepared;
import com.spbsu.flamestream.runtime.master.acker.api.commit.Ready;
import com.spbsu.flamestream.runtime.master.acker.api.registry.UnregisterFront;
import com.spbsu.flamestream.runtime.config.ComputationProps;
import com.spbsu.flamestream.runtime.config.HashGroup;
import com.spbsu.flamestream.runtime.config.HashUnit;
import com.spbsu.flamestream.runtime.graph.api.AddressedItem;
import com.spbsu.flamestream.runtime.graph.api.NewRear;
import com.spbsu.flamestream.runtime.graph.state.GroupGroupingState;
import com.spbsu.flamestream.runtime.graph.state.GroupingState;
import com.spbsu.flamestream.runtime.state.StateStorage;
import com.spbsu.flamestream.runtime.utils.FlameConfig;
import com.spbsu.flamestream.runtime.utils.akka.LoggingActor;
import com.spbsu.flamestream.runtime.utils.collections.HashUnitMap;
import com.spbsu.flamestream.runtime.utils.collections.ListHashUnitMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GraphManager extends LoggingActor {
  private final Graph graph;
  private final ActorRef acker;
  private final ComputationProps computationProps;
  private final StateStorage storage;
  private final String nodeId;

  private ActorRef sourceComponent;
  private ActorRef sinkComponent;

  private final HashUnitMap<ActorRef> routes = new ListHashUnitMap<>();
  private final Map<Destination, ActorRef> verticesComponents = new HashMap<>();
  private final Set<ActorRef> components = new HashSet<>();

  private final Map<HashUnit, Map<String, GroupingState>> unitStates = new HashMap<>();
  private final TObjectIntMap<String> groupingWindows = new TObjectIntHashMap<>();

  private GraphManager(String nodeId,
                       Graph graph,
                       ActorRef acker,
                       ComputationProps computationProps,
                       StateStorage storage) {
    this.nodeId = nodeId;
    this.storage = storage;
    this.computationProps = computationProps;
    this.graph = graph;
    this.acker = context().actorOf(LocalAcker.props(acker), "local-acker");
  }

  public static Props props(
          String nodeId,
          Graph graph,
          ActorRef acker,
          ComputationProps layout,
          StateStorage storage) {
    return Props.create(GraphManager.class, nodeId, graph, acker, layout, storage)
            .withDispatcher("processing-dispatcher");
  }

  @Override
  public Receive createReceive() {
    return ReceiveBuilder.create()
            .match(Map.class, managers -> {
              log().info("Finishing constructor");
              final Map<HashUnit, ActorRef> routerMap = new HashMap<>();
              computationProps.hashGroups()
                      .forEach((key, value) -> value.units()
                              .forEach(unit -> routerMap.put(unit, (ActorRef) managers.get(key))));
              routes.putAll(routerMap);

              acker.tell(new GimmeTime(), self());
              getContext().become(deploying());
            })
            .matchAny(m -> stash())
            .build();
  }

  private Receive deploying() {
    return ReceiveBuilder.create()
            .match(LastCommit.class, lastCommit -> {
              log().info("Received last commit '{}'", lastCommit);
              final Map<String, GroupGroupingState> stateByVertex = new HashMap<>();
              final HashGroup localGroup = computationProps.hashGroups().get(nodeId);
              for (final HashUnit unit : localGroup.units()) {
                final Map<String, GroupingState> unitState = storage.stateFor(
                        unit,
                        lastCommit.globalTime()
                );
                graph.components()
                        .flatMap(vertexStream -> vertexStream)
                        .filter(vertex -> vertex instanceof Grouping)
                        .forEach(vertex -> {
                          unitState.putIfAbsent(vertex.id(), new GroupingState());
                          groupingWindows.put(vertex.id(), ((Grouping) vertex).window());
                        });
                unitState.forEach((vertexId, groupingState) -> {
                  stateByVertex.putIfAbsent(vertexId, new GroupGroupingState());
                  stateByVertex.get(vertexId).addUnitState(unit, groupingState);
                });
                unitStates.put(unit, unitState);
              }

              graph.components().forEach(c -> {
                final Set<Graph.Vertex> vertexSet = c.collect(Collectors.toSet());
                final ActorRef component = context().actorOf(Component.props(
                        vertexSet,
                        graph,
                        routes,
                        self(),
                        acker,
                        computationProps,
                        stateByVertex
                ));

                vertexSet.stream()
                        .map(v -> Destination.fromVertexId(v.id()))
                        .forEach(dest -> verticesComponents.put(dest, component));

                components.add(component);

                vertexSet.stream()
                        .filter(v -> v instanceof Source)
                        .findAny()
                        .ifPresent(v -> sourceComponent = component);

                vertexSet.stream()
                        .filter(v -> v instanceof Sink)
                        .findAny()
                        .ifPresent(v -> sinkComponent = component);
              });

              acker.tell(new Ready(), self());
              unstashAll();
              getContext().become(managing());
            })
            .matchAny(m -> stash())
            .build();
  }

  private Receive managing() {
    return ReceiveBuilder.create()
            .match(DataItem.class, dataItem -> sourceComponent.forward(dataItem, context()))
            .match(
                    AddressedItem.class,
                    addressedItem -> verticesComponents.get(addressedItem.destination())
                            .forward(addressedItem, context())
            )
            .match(
                    MinTimeUpdate.class,
                    minTimeUpdate -> components.forEach(c -> c.forward(minTimeUpdate, context()))
            )
            .match(Prepare.class, this::onPrepare)
            .match(NewRear.class, newRear -> sinkComponent.forward(newRear, context()))
            .match(Heartbeat.class, gt -> sourceComponent.forward(gt, context()))
            .match(UnregisterFront.class, u -> sourceComponent.forward(u, context()))
            .build();
  }

  private void onPrepare(Prepare prepare) {
    PatternsCS.ask(sinkComponent, prepare, FlameConfig.config.smallTimeout()).thenRun(() -> {
      unitStates.forEach((hashUnit, stateMap) -> storage.putState(
              hashUnit,
              prepare.globalTime(),
              stateMap.entrySet()
                      .stream()
                      .collect(Collectors.toMap(
                              Map.Entry::getKey,
                              e -> e.getValue()
                                      .subState(prepare.globalTime(), groupingWindows.get(e.getKey()))
                      ))
      ));
      acker.tell(new Prepared(), self());
    });
  }

  public static class Destination {
    private static final Map<String, Destination> cache = new HashMap<>();
    private final String vertexId;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      final Destination that = (Destination) o;
      return vertexId.equals(that.vertexId);
    }

    @Override
    public int hashCode() {
      return vertexId.hashCode();
    }

    @Override
    public String toString() {
      return "Destination{" +
              "vertexId='" + vertexId + '\'' +
              '}';
    }

    private Destination(String vertexId) {
      this.vertexId = vertexId;
    }

    static Destination fromVertexId(String vertexId) {
      return cache.compute(vertexId, (s, destination) -> {
        if (destination == null) {
          return new Destination(s);
        }
        return destination;
      });
    }
  }
}
