package com.spbsu.flamestream.runtime;

import com.spbsu.flamestream.core.graph.TheGraph;
import com.spbsu.flamestream.runtime.environmet.Environment;
import com.spbsu.flamestream.runtime.environmet.remote.RemoteEnvironment;
import com.spbsu.flamestream.runtime.range.HashRange;
import com.spbsu.flamestream.runtime.tick.TickInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public final class TestStand implements AutoCloseable {
  private final LocalCluster cluster;
  private final RemoteEnvironment environment;

  public TestStand(int workerCount) {
    this.cluster = new LocalCluster(workerCount);
    this.environment = new RemoteEnvironment(cluster.zookeeperString());
  }

  public Environment environment() {
    return environment;
  }

  public void deploy(TheGraph theGraph, int tickLengthSeconds, int ticksCount) {
    final Map<HashRange, Integer> workers = rangeMappingForTick();
    final long tickNanos = SECONDS.toNanos(tickLengthSeconds);

    long startTs = System.nanoTime();
    for (int i = 0; i < ticksCount; ++i, startTs += tickNanos) {
      final TickInfo tickInfo = new TickInfo(
              i,
              startTs,
              startTs + tickNanos,
              theGraph,
              workers.values().stream().findAny().orElseThrow(RuntimeException::new),
              workers,
              MILLISECONDS.toNanos(10),
              i == 0 ? emptySet() : singleton(i - 1L)
      );
      environment.deploy(tickInfo);
    }

    //This sleep doesn't affect correctness.
    // Only reduces stashing overhead for first several items
    try {
      SECONDS.sleep(2);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

  }

  public Consumer<Object> randomFrontConsumer(int maxFrontsCount) {
    final Set<Integer> fronts = environment.availableFronts();
    final List<Consumer<Object>> collectors = fronts.stream()
            .map(environment::frontConsumer)
            .limit(maxFrontsCount)
            .collect(Collectors.toList());

    final Random rd = new Random();
    return obj -> collectors.get(rd.nextInt(collectors.size())).accept(obj);
  }

  private Map<HashRange, Integer> rangeMappingForTick() {
    final Map<HashRange, Integer> result = new HashMap<>();
    final Set<Integer> workerIds = environment.availableWorkers();

    final int step = (int) (((long) Integer.MAX_VALUE - Integer.MIN_VALUE) / workerIds.size());
    long left = Integer.MIN_VALUE;
    long right = left + step;

    for (int workerId : workerIds) {
      result.put(new HashRange((int) left, (int) right), workerId);

      left += step;
      right = Math.min(Integer.MAX_VALUE, right + step);
    }

    return result;
  }

  public void awaitTick(int seconds) {
    try {
      SECONDS.sleep(seconds);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() {
    environment.close();
    cluster.close();
  }
}