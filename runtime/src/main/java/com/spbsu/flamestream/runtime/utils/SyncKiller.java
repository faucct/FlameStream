package com.spbsu.flamestream.runtime.utils;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorRefFactory;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.Status;
import akka.actor.Terminated;
import akka.japi.pf.ReceiveBuilder;
import akka.pattern.PatternsCS;
import akka.util.Timeout;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SyncKiller extends AbstractActor {
  private final ActorRef ref;
  private ActorRef requester;

  public static void syncKill(ActorRef ref, ActorRefFactory context) {
    try {
      kill(ref, context).toCompletableFuture().get(10, TimeUnit.SECONDS);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      throw new RuntimeException("Failed to kill " + ref, e);
    }
  }

  public static CompletionStage<Void> kill(ActorRef ref, ActorRefFactory context) {
    final ActorRef resolver = context.actorOf(SyncKiller.props(ref).withDispatcher("util-dispatcher"));
    return PatternsCS.ask(resolver, "KILL", Timeout.apply(100, TimeUnit.SECONDS)).thenApply(a -> null);
  }

  private SyncKiller(ActorRef ref) {this.ref = ref;}

  public static Props props(ActorRef ref) {
    return Props.create(SyncKiller.class, ref);
  }

  @Override
  public Receive createReceive() {
    return ReceiveBuilder.create()
            .match(String.class, s -> s.equals("KILL"), s -> {
              if (requester != null) {
                requester.tell(new Status.Failure(new IllegalStateException()), self());
              } else {
                requester = sender();
                context().watch(ref);
                ref.tell(PoisonPill.getInstance(), self());
              }
            })
            .match(Terminated.class, t -> {
              requester.tell(t, self());
              context().stop(self());
            })
            .build();
  }
}
