package com.spbsu.datastream.core.barrier;

import com.spbsu.datastream.core.DataItem;
import com.spbsu.datastream.core.GlobalTime;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;

public final class LinearCollector implements BarrierCollector {
  private final SortedMap<GlobalTime, List<DataItem<?>>> invalidationPool = new TreeMap<>();

  private final Queue<Object> released = new ArrayDeque<>();

  @Override
  public void update(final GlobalTime newOldest) {
    this.invalidationPool.headMap(newOldest)
            .values().stream().flatMap(List::stream).forEach(this.released::add);
    this.invalidationPool.headMap(newOldest).clear();
  }

  @Override
  public void enqueue(final DataItem<?> item) {
    this.invalidationPool.putIfAbsent(item.meta().globalTime(), new ArrayList<>());

    this.invalidationPool.computeIfPresent(item.meta().globalTime(), (key, oldList) -> {
      oldList.removeIf(di -> di.meta().isInvalidatedBy(item.meta()));
      oldList.add(item);
      return oldList;
    });
  }

  @Override
  public void release(final Consumer<Object> consumer) {
    this.released.forEach(consumer);
    this.released.clear();
  }
}