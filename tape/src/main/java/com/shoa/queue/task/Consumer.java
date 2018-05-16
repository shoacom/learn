package com.shoa.queue.task;

import com.squareup.tape.ObjectQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by xiojiang on 2018/5/14.
 */
public class Consumer<T> implements Runnable {
  private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);
  private final ObjectQueue<T> objectQueue;
  private final Provider<T> provider;
  private final Processor<T> processor;
  private volatile boolean stopped = true;

  public Consumer(ObjectQueue<T> objectQueue, Provider<T> provider, Processor<T> processor) {
    this.objectQueue = objectQueue;
    this.provider = provider;
    this.processor = processor;
  }


  public boolean isStopped() {
    return stopped;
  }

  @Override
  public void run() {
    stopped = false;
    while (!provider.isStopped()) {
      while (objectQueue.size() > 0) {
        T element = objectQueue.peek();
        if (element == null) {
          LOGGER.warn("element is null.");
          continue;
        }
        while (processor.isExhausted()) {
          try {
            Thread.sleep(TimeUnit.SECONDS.toSeconds(1));
          } catch (InterruptedException e) {
            LOGGER.warn("Interrupted.", e);
            Thread.currentThread().interrupt();
          }
        }
        objectQueue.remove();
        LOGGER.debug("Consumed...{}", element);
        processor.addToProcess(element);
      }
    }
    LOGGER.info("Consumer is stopped.");
    stopped = true;

  }
}
