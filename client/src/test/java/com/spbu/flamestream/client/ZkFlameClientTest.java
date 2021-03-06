package com.spbu.flamestream.client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.spbsu.flamestream.core.Graph;
import com.spbsu.flamestream.core.Job;
import com.spbsu.flamestream.core.graph.FlameMap;
import com.spbsu.flamestream.core.graph.Sink;
import com.spbsu.flamestream.core.graph.Source;
import com.spbsu.flamestream.runtime.LocalClusterRuntime;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ZkFlameClientTest {
  private static final Logger LOG = LoggerFactory.getLogger(ZkFlameClientTest.class);

  @Test
  public void testPushJobWorks() throws InterruptedException, IOException {
    final int inputSize = 100;
    final int frontPort = 4567;
    final int rearPort = 5678;

    final List<String> input = Stream.generate(() -> UUID.randomUUID().toString())
            .limit(inputSize)
            .collect(Collectors.toList());
    final Server front = front(input, frontPort);

    final List<String> result = new ArrayList<>();
    final CountDownLatch latch = new CountDownLatch(inputSize);
    final Server rear = rear(latch, result, rearPort);

    try (final LocalClusterRuntime localClusterRuntime = new LocalClusterRuntime.Builder().parallelism(4).build()) {
      final FlameClient flameClient = new ZkFlameClient(localClusterRuntime.zkString());
      flameClient.push(new Job.Builder(testGraph())
              .addFront(new Job.Front("socket-front", "localhost", frontPort, String.class))
              .addRear(new Job.Rear("socket-rear", "localhost", rearPort, String.class))
              .build());

      latch.await(5, TimeUnit.MINUTES);
      Assert.assertEquals(result, input.stream().map(s -> ("prefix_" + s)).collect(Collectors.toList()));
    }

    front.close();
    rear.close();
  }

  private static Server front(List<String> input, int port) {
    final Server producer = new Server(1_000_000, 1000);
    ((Kryo.DefaultInstantiatorStrategy) producer.getKryo().getInstantiatorStrategy())
            .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());

    final Connection[] connection = new Connection[1];
    new Thread(() -> {
      synchronized (connection) {
        try {
          connection.wait();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
      input.forEach(s -> {
        synchronized (connection) {
          connection[0].sendTCP(s);
          LOG.info("Sending: {}", s);
          LockSupport.parkNanos((long) (50 * 1.0e6));
        }
      });
    }).start();

    producer.addListener(new Listener() {
      @Override
      public void connected(Connection newConnection) {
        synchronized (connection) {
          LOG.info("There is new connection: {}", newConnection.getRemoteAddressTCP());
          //first condition for local testing
          if (connection[0] == null) {
            LOG.info("Accepting connection: {}", newConnection.getRemoteAddressTCP());
            connection[0] = newConnection;
            connection.notify();
          } else {
            LOG.info("Closing connection {}", newConnection.getRemoteAddressTCP());
            newConnection.close();
          }
        }
      }
    });
    producer.start();
    try {
      producer.bind(port);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return producer;
  }

  private static Server rear(CountDownLatch latch, List<String> result, int port) {
    final Server consumer = new Server(2000, 1_000_000);
    ((Kryo.DefaultInstantiatorStrategy) consumer.getKryo()
            .getInstantiatorStrategy())
            .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());

    consumer.addListener(new Listener() {
      @Override
      public void connected(Connection connection) {
        LOG.info("Consumer has been connected {}, {}", connection, connection.getRemoteAddressTCP());
      }

      @Override
      public void disconnected(Connection connection) {
        LOG.info("Consumer has been disconnected {}", connection);
      }
    });

    consumer.addListener(new Listener() {
      @Override
      public void received(Connection connection, Object o) {
        if (o instanceof String) {
          LOG.info("Received: {}", o);
          result.add((String) o);
          latch.countDown();
        }
      }
    });

    consumer.start();
    try {
      consumer.bind(port);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return consumer;
  }

  private static Graph testGraph() {
    final Source source = new Source();
    final Sink sink = new Sink();

    //noinspection Convert2Lambda
    final FlameMap<String, String> dumbMap = new FlameMap<>(new Function<String, Stream<String>>() {
      @Override
      public Stream<String> apply(String s) {
        return Stream.of("prefix_" + s);
      }
    }, String.class);
    return new Graph.Builder().link(source, dumbMap).link(dumbMap, sink).build(source, sink);
  }
}
