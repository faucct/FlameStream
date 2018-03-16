package com.spbsu.benchmark.flink.index.ops;

import com.spbsu.benchmark.flink.index.Result;
import com.spbsu.flamestream.example.bl.index.model.WordIndexAdd;
import com.spbsu.flamestream.example.bl.index.model.WordIndexRemove;
import com.spbsu.flamestream.example.bl.index.ops.InvertedIndexState;
import com.spbsu.flamestream.example.bl.index.utils.IndexItemInLong;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.typeutils.GenericTypeInfo;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import java.io.IOException;

/**
 * User: Artem
 * Date: 04.01.2018
 */
public class IndexFunction extends ProcessFunction<Tuple2<String, long[]>, Result> {
  private transient ValueStateDescriptor<InvertedIndexState> descriptor;

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    descriptor = new ValueStateDescriptor<>(
            "index_state",
            new GenericTypeInfo<>(InvertedIndexState.class)
    );
  }

  @Override
  public void processElement(Tuple2<String, long[]> value, Context ctx, Collector<Result> out) throws IOException {
    final ValueState<InvertedIndexState> state = getRuntimeContext().getState(descriptor);
    final InvertedIndexState indexState;
    if (state.value() == null) {
      indexState = new InvertedIndexState();
    } else {
      indexState = state.value().copy();
    }

    final long prevValue = indexState.updateOrInsert(value.f1);
    state.update(indexState);

    final WordIndexAdd wordIndexAdd = new WordIndexAdd(value.f0, value.f1);
    WordIndexRemove wordIndexRemove = null;
    if (prevValue != InvertedIndexState.PREV_VALUE_NOT_FOUND) {
      wordIndexRemove = new WordIndexRemove(
              value.f0,
              IndexItemInLong.setRange(prevValue, 0),
              IndexItemInLong.range(prevValue)
      );
    }
    out.collect(new Result(wordIndexAdd, wordIndexRemove));
  }
}
