package com.spbsu.datastream.core.graph;

import java.util.*;
import java.util.stream.Collectors;

public final class ComposedGraphImpl<T extends Graph> implements ComposedGraph<T> {
  private final Map<OutPort, InPort> downstreams;

  private final List<InPort> inPorts;
  private final List<OutPort> outPorts;

  private final Set<T> subGraphs;

  public ComposedGraphImpl(final Set<T> graphs) {
    this(graphs, Collections.emptyMap());
  }

  ComposedGraphImpl(final T graph,
                    final OutPort from,
                    final InPort to) {
    this(Collections.singleton(graph), Collections.singletonMap(from, to));
  }

  ComposedGraphImpl(final Set<T> graphs,
                    final Map<OutPort, InPort> wires) {
    ComposedGraphImpl.assertCorrectWires(graphs, wires);

    this.subGraphs = new HashSet<>(graphs);

    this.downstreams = new HashMap<>(wires);

    this.inPorts = graphs.stream().map(Graph::inPorts)
            .flatMap(Collection::stream).filter(port -> !this.downstreams.containsValue(port))
            .collect(Collectors.toList());

    this.outPorts = graphs.stream().map(Graph::outPorts)
            .flatMap(List::stream).filter(port -> !this.downstreams.containsKey(port))
            .collect(Collectors.toList());
  }

  @Override
  public Set<T> subGraphs() {
    return Collections.unmodifiableSet(this.subGraphs);
  }

  @Override
  public List<InPort> inPorts() {
    return Collections.unmodifiableList(this.inPorts);
  }

  @Override
  public List<OutPort> outPorts() {
    return Collections.unmodifiableList(this.outPorts);
  }

  @Override
  public ComposedGraph<AtomicGraph> flattened() {
    final Set<ComposedGraph<AtomicGraph>> flatteneds = this.subGraphs().stream()
            .map(Graph::flattened).collect(Collectors.toSet());
    final Set<AtomicGraph> atomics = flatteneds.stream().map(ComposedGraph::subGraphs)
            .flatMap(Set::stream).collect(Collectors.toSet());

    final Map<OutPort, InPort> flatDownStreams = flatteneds.stream().map(ComposedGraph::downstreams)
            .map(Map::entrySet).flatMap(Set::stream).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    flatDownStreams.putAll(this.downstreams());

    return new ComposedGraphImpl<>(atomics, flatDownStreams);
  }

  @Override
  public Map<OutPort, InPort> downstreams() {
    return Collections.unmodifiableMap(this.downstreams);
  }

  private static void assertCorrectWires(final Collection<? extends Graph> graphs,
                                         final Map<OutPort, InPort> wires) {
    wires.forEach((from, to) -> ComposedGraphImpl.assertCorrectWire(graphs, from, to));
  }

  private static void assertCorrectWire(final Collection<? extends Graph> graphs, final OutPort from, final InPort to) {
    if (graphs.stream().noneMatch(graph -> graph.outPorts().contains(from))) {
      throw new WiringException("Out ports of " + graphs + " hasn't got " + from);
    }

    if (graphs.stream().noneMatch(graph -> graph.inPorts().contains(to))) {
      throw new WiringException("In ports of " + graphs + " hasn't got " + to);
    }
  }
}
