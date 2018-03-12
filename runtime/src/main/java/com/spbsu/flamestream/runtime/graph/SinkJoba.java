package com.spbsu.flamestream.runtime.graph;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import com.spbsu.flamestream.core.DataItem;
import com.spbsu.flamestream.core.data.invalidation.ArrayInvalidatingBucket;
import com.spbsu.flamestream.core.data.invalidation.InvalidatingBucket;
import com.spbsu.flamestream.core.data.meta.GlobalTime;
import com.spbsu.flamestream.core.data.meta.Meta;
import com.spbsu.flamestream.runtime.utils.tracing.Tracing;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SinkJoba implements Joba {
  private final InvalidatingBucket invalidatingBucket = new ArrayInvalidatingBucket();
  private final List<ActorRef> rears = new ArrayList<>();
  private final ActorContext context;

  private final Tracing.Tracer barrierReceiveTracer = Tracing.TRACING.forEvent("barrier-receive");
  private final Tracing.Tracer barrierSendTracer = Tracing.TRACING.forEvent("barrier-send");

  public SinkJoba(ActorContext context) {
    this.context = context;
  }

  @Override
  public void accept(DataItem item, Consumer<DataItem> sink) {
    barrierReceiveTracer.log(item.xor());
    invalidatingBucket.insert(item);
  }

  public void attachRear(ActorRef rear) {
    rears.add(rear);
  }

  @Override
  public void onMinTime(GlobalTime minTime) {
    final int pos = invalidatingBucket.lowerBound(new Meta(minTime));
    invalidatingBucket.rangeStream(0, pos).forEach(di -> {
      barrierSendTracer.log(di.xor());
      rears.forEach(rear -> rear.tell(di, context.self()));
    });
    invalidatingBucket.clearRange(0, pos);
  }
}