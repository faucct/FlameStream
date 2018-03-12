package com.spbsu.flamestream.runtime.edge.akka;

import akka.actor.ActorPath;
import akka.actor.ActorPaths;
import akka.actor.ActorRef;
import akka.actor.ActorRefFactory;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.pattern.PatternsCS;
import com.spbsu.flamestream.core.Batch;
import com.spbsu.flamestream.core.Rear;
import com.spbsu.flamestream.runtime.edge.EdgeContext;
import com.spbsu.flamestream.runtime.edge.api.Accept;
import com.spbsu.flamestream.runtime.edge.api.GimmeLastBatch;
import com.spbsu.flamestream.runtime.utils.akka.AwaitResolver;
import com.spbsu.flamestream.runtime.utils.akka.LoggingActor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class AkkaRear implements Rear {
  private final ActorRef innerActor;

  public AkkaRear(EdgeContext edgeContext, ActorRefFactory refFactory, String localMediatorPath) {
    this.innerActor = refFactory.actorOf(
            RemoteMediator.props(localMediatorPath + '/' + edgeContext.edgeId().nodeId() + "-localrear")
                    .withDispatcher("util-dispatcher"),
            edgeContext.edgeId().nodeId() + "-inner"
    );
  }

  @Override
  public void accept(Batch batch) {
    try {
      PatternsCS.ask(innerActor, batch, TimeUnit.SECONDS.toMillis(10))
              .toCompletableFuture().get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Batch last() {
    try {
      return PatternsCS.ask(innerActor, new GimmeLastBatch(), TimeUnit.SECONDS.toMillis(10))
              .thenApply(Batch.class::cast)
              .toCompletableFuture().get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  private static class RemoteMediator extends LoggingActor {
    private final String localMediatorPath;
    private ActorRef localMediator;

    private RemoteMediator(String localMediatorPath) {
      this.localMediatorPath = localMediatorPath;
    }

    public static Props props(String localMediatorPath) {
      return Props.create(RemoteMediator.class, localMediatorPath);
    }

    @Override
    public void preStart() throws Exception {
      super.preStart();
      final ActorPath path = ActorPaths.fromString(localMediatorPath);
      localMediator = AwaitResolver.resolve(path, context())
              .toCompletableFuture()
              .get();
    }

    @Override
    public Receive createReceive() {
      return ReceiveBuilder.create()
              .match(Batch.class, b -> {
                PatternsCS.ask(localMediator, b, TimeUnit.SECONDS.toMillis(10))
                        .thenRun(() -> sender().tell(new Accept(), self()));
              })
              .match(GimmeLastBatch.class, g -> {
                PatternsCS.ask(localMediator, g, TimeUnit.SECONDS.toMillis(10))
                        .thenApply(a -> (Batch) a)
                        .thenAccept(batch -> sender().tell(batch, self()));
              })
              .build();
    }
  }

  public static class LocalMediator<T> extends LoggingActor {
    private final Class<T> clazz;
    private Consumer<T> consumer = null;

    private Batch lastBatch = Batch.EMPTY_BATCH;

    private LocalMediator(Class<T> clazz) {
      this.clazz = clazz;
    }

    public static <T> Props props(Class<T> clazz) {
      return Props.create(LocalMediator.class, clazz);
    }

    @Override
    public Receive createReceive() {
      return ReceiveBuilder.create()
              .match(Consumer.class, c -> {
                this.consumer = c;
                unstashAll();
                getContext().become(serving());
              })
              .matchAny(a -> stash())
              .build();
    }

    private Receive serving() {
      return ReceiveBuilder.create()
              .match(Consumer.class, c -> {
                consumer = c;
              })
              .match(Batch.class, b -> {
                lastBatch = b;
                b.payload(clazz).forEach(consumer);
                sender().tell(new Accept(), self());
              })
              .match(GimmeLastBatch.class, g -> sender().tell(lastBatch, self()))
              .build();
    }
  }

  public static class Handle<T> {
    private final ActorRef localMediator;

    public Handle(ActorRef localMediator) {
      this.localMediator = localMediator;
    }

    public void addListener(Consumer<T> sink) {
      localMediator.tell(sink, ActorRef.noSender());
    }
  }
}
