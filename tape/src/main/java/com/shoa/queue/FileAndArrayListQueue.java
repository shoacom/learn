package com.shoa.queue;

import com.shoa.tape.LargeFileObjectQueue;
import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.ObjectQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xiojiang on 2018/5/9.
 */
public class FileAndArrayListQueue<T> implements ObjectQueue<T> {
  private static final Logger LOGGER = LoggerFactory.getLogger(FileAndArrayListQueue.class);
  private ObjectQueue<T> fileQueue;
  private List<T> list = new CopyOnWriteArrayList<>();
  private PersistenceStrategy strategy = new DefaultPersistenceStrategy();

  public FileAndArrayListQueue(FileObjectQueue.Converter<T> converter) throws IOException {
    LOGGER.info("Persistence Strategy: {}", strategy);
    if (!strategy.isLoadFromFile()) {
      clearFileQueue();
    }
    fileQueue = new LargeFileObjectQueue<>(strategy.persistFile(), converter);
    if (strategy.isLoadFromFile()) {
      LOGGER.info("Before: fileQueue.size={}, fileQueue.get={},list.size={}", fileQueue.size(), fileQueue.peek(), list
          .size());
      populateMemoryIfNeeded();
      LOGGER.info("After: fileQueue.size={}, fileQueue.get={},list.size={}", fileQueue.size(), fileQueue.peek(), list
          .size());
    }
  }


  @Override
  public synchronized void add(T e) {
    if (list.size() < strategy.memoryCapacity() && fileQueue.size() == 0) {
      list.add(e);
      return;
    }
    LOGGER.debug("Adding to file queue...{}", e);
    fileQueue.add(e);
  }


  @Override
  public synchronized int size() {
    return list.size() + fileQueue.size();
  }

  @Override
  public synchronized T peek() {
    return get(0);
  }

  @Override
  public synchronized void remove() {
    list.remove(0);
  }

  @Override
  public void setListener(Listener<T> listener) {
    // nothing
  }

  private T get(int index) {
    populateMemoryIfNeeded();
    return list.isEmpty() || index > list.size() ? null : list.get(index);
  }


  private void populateMemoryIfNeeded() {
    int threshold = strategy.memoryCapacity() / strategy.populateRatio();

    if (list.size() <= threshold && fileQueue.size() > 0) {
      LOGGER.info("Start populate, list.size={}, list.first={}, fileQueue.size={}, fileQueue.get={}", list.size(),
          list.isEmpty() ? null : list.get(0), fileQueue.size(), fileQueue.peek());
      while (list.size() < strategy.memoryCapacity()) {
        T e = fileQueue.peek();
        if (e == null) {
          LOGGER.info("File queue is empty.");
          break;
        }
        LOGGER.debug("Populating to list...{}", e);
        list.add(list.size(), e);
        fileQueue.remove();
      }
    }
  }

  private void clearFileQueue() {
    try {
      Files.deleteIfExists(strategy.persistFile().toPath());
    } catch (IOException e) {
      LOGGER.error("Clear file error.", e);
    }
  }

}
