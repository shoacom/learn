package com.shoa.queue.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xiojiang on 2018/5/14.
 */
public class Processor<T> implements Runnable {
  private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);
  private static final int SIZE_LIMIT = 10000;
  private List<T> list = new CopyOnWriteArrayList<>();
  private AtomicInteger exhaustedTimes = new AtomicInteger();
  private Consumer<T> consumer;

  public void setConsumer(Consumer<T> consumer) {
    this.consumer = consumer;
  }

  public void addToProcess(T e) {
    list.add(e);
  }

  public boolean isExhausted() {
    boolean isExhausted = list.size() >= SIZE_LIMIT;
    if (isExhausted) {
      exhaustedTimes.incrementAndGet();
      LOGGER.info("Processor is exhausted {}.", exhaustedTimes());
    }
    return isExhausted;
  }


  public int exhaustedTimes() {
    return exhaustedTimes.get();
  }

  @Override
  public void run() {
    while (!consumer.isStopped()) {
      while (!list.isEmpty()) {
        T object = list.get(0);
        if (object == null) {
          LOGGER.warn("object is null");
          continue;
        }
        list.remove(object);
        Monitor.getInstance().addToMonitor(object);
      }
    }

    LOGGER.info("Processor is stopped.");
  }
}
