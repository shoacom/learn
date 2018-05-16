package com.shoa.queue.task;

import com.squareup.tape.ObjectQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * Created by xiojiang on 2018/5/14.
 */
public class Provider<T> implements Runnable {
  private static final Logger LOGGER = LoggerFactory.getLogger(Provider.class);
  private int limit = 1000;
  private ObjectQueue<T> objectQueue;
  private Function<Object, T> function;
  private volatile boolean stopped = true;

  public Provider(ObjectQueue<T> objectQueue, Function<Object, T> function) {
    this.objectQueue = objectQueue;
    this.function = function;
  }

  public Provider(ObjectQueue<T> objectQueue, Function<Object, T> function, int limit) {
    this.objectQueue = objectQueue;
    this.function = function;
    this.limit = limit;
  }

  public boolean isStopped() {
    return stopped;
  }

  @Override
  public void run() {
    stopped = false;
    int i = 0;
    while (i < limit) {
      T element = function.apply(i++);
      objectQueue.add(element);
      LOGGER.debug("Providing...{}", element);
    }
    stopped = true;

    LOGGER.info("Provider is stopped ");
  }
}
