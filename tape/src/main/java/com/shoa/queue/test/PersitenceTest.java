package com.shoa.queue.test;

import com.google.gson.Gson;
import com.shoa.queue.FileAndMemoryQueue;
import com.shoa.queue.GsonConverter;
import com.shoa.queue.FileAndArrayListQueue;
import com.shoa.queue.task.Consumer;
import com.shoa.queue.task.Processor;
import com.shoa.queue.task.Provider;
import com.shoa.queue.vo.IdGenerator;
import com.shoa.queue.vo.Notification;
import com.shoa.queue.vo.StorableNotification;
import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.ObjectQueue;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiojiang on 2018/5/9.
 */
public class PersitenceTest {
  private static final ExecutorService executorService = Executors.newCachedThreadPool();
  private static final int LIMIT = 10000;


  public static void main(String[] args) throws Exception {
    testConsistency();
    executorService.shutdown();
  }

  public static void testLargeFile() throws IOException {
    FileAndArrayListQueue<StorableNotification> list = new FileAndArrayListQueue<>(getGsonConverter
        (StorableNotification.class));
    executorService.execute(new Provider(list, Notification::createAndConvert, LIMIT));
  }

  public static void testConsistency() throws IOException, InterruptedException {
    ObjectQueue<Integer> list = new FileAndMemoryQueue<>(getGsonConverter(Integer.class));

    Provider provider = new Provider(list, IdGenerator::generate, LIMIT);
    Processor processor = new Processor();
    Consumer consumer = new Consumer(list, provider, processor);
    processor.setConsumer(consumer);
    executorService.execute(provider);
    Thread.sleep(TimeUnit.SECONDS.toSeconds(5));
    executorService.execute(consumer);
    executorService.execute(processor);
  }

  public static FileObjectQueue.Converter getGsonConverter(Class clazz) {
    return new GsonConverter(new Gson(), clazz);
  }


}

