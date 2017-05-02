package com.spbsu.datastream.core.node;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spbsu.datastream.core.LoggingActor;
import com.spbsu.datastream.core.configuration.HashRange;
import com.spbsu.datastream.core.configuration.RangeMappingsDto;
import com.spbsu.datastream.core.front.FrontActor;
import com.spbsu.datastream.core.range.RangeConcierge;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class NodeConcierge extends LoggingActor {
  private final LoggingAdapter LOG = Logging.getLogger(this.context().system(), this.self());

  private final ZooKeeper zooKeeper;
  private final InetSocketAddress myAddress;
  private final ObjectMapper mapper = new ObjectMapper();

  private final List<ActorRef> deployees = new ArrayList<>();

  private NodeConcierge(final InetSocketAddress myAddress, final ZooKeeper zooKeeper) {
    this.zooKeeper = zooKeeper;
    this.myAddress = myAddress;
  }

  public static Props props(final InetSocketAddress address, final ZooKeeper zooKeeper) {
    return Props.create(NodeConcierge.class, address, zooKeeper);
  }

  @Override
  public void preStart() throws Exception {
    this.LOG.info("Starting... Address: {}", this.myAddress);

    final Map<HashRange, InetSocketAddress> rangeMappings = this.fetchRangeMappings();
    this.LOG.info("Range mappings fetched: {}", rangeMappings);

    final ActorRef rootRouter = this.context().actorOf(RootRouter.props(rangeMappings), "rootRouter");

    final Set<HashRange> myRanges = this.myRanges(rangeMappings);
    this.deployees.addAll(myRanges.stream().map(r -> this.conciergeForRange(r, rootRouter)).collect(Collectors.toList()));

    final Map<InetSocketAddress, Integer> frontMappings = this.fetchFrontMappings();
    this.LOG.info("Front mappings fetched: {}", frontMappings);

    Optional.ofNullable(frontMappings.get(this.myAddress))
            .map(id -> this.context().actorOf(FrontActor.props(rootRouter, id), "front"))
            .ifPresent(this.deployees::add);

    super.preStart();
  }

  private ActorRef conciergeForRange(final HashRange range, final ActorRef remoteRouter) {
    return this.context().actorOf(RangeConcierge.props(range, remoteRouter), range.toString());
  }

  private Set<HashRange> myRanges(final Map<HashRange, InetSocketAddress> mappings) {
    return mappings.entrySet().stream().filter(e -> e.getValue().equals(this.myAddress))
            .map(Map.Entry::getKey).collect(Collectors.toSet());
  }

  private Map<HashRange, InetSocketAddress> fetchRangeMappings() throws KeeperException, InterruptedException, IOException {
    final String path = "/ranges";
    final byte[] data = this.zooKeeper.getData(path, this.selfWatcher(), new Stat());
    return this.mapper.readValue(data, RangeMappingsDto.class).rangeMappings();
  }

  private Map<InetSocketAddress, Integer> fetchFrontMappings() throws KeeperException, InterruptedException, IOException {
    final String path = "/fronts";
    final byte[] data = this.zooKeeper.getData(path, this.selfWatcher(), new Stat());
    final Map<Integer, InetSocketAddress> result = this.mapper.readValue(data, new TypeReference<Map<Integer, InetSocketAddress>>() {
    });

    return result.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
  }

  @Override
  public void onReceive(final Object message) throws Throwable {
    if (message instanceof DeployForTick) {
      this.deployees.forEach(d -> d.tell(message, ActorRef.noSender()));
    } else {
      this.unhandled(message);
    }
  }

  private Watcher selfWatcher() {
    return event -> this.self().tell(event, this.self());
  }
}
