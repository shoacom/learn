package com.shoa.queue;

import com.shoa.tape.LargeFileObjectQueue;
import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.InMemoryObjectQueue;
import com.squareup.tape.ObjectQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by xiojiang on 2018/5/9.
 */
public class FileAndMemoryQueue<T> implements ObjectQueue<T> {
  private static final Logger LOGGER = LoggerFactory.getLogger(FileAndMemoryQueue.class);
  private ObjectQueue<T> fileQueue;
  private ObjectQueue<T> memoryQueue = new InMemoryObjectQueue<>();
  private PersistenceStrategy strategy = new DefaultPersistenceStrategy();

  public FileAndMemoryQueue(FileObjectQueue.Converter<T> converter) throws IOException {
    LOGGER.info("Persistence Strategy: {}", strategy);
    if (!strategy.isLoadFromFile()) {
      clearFileQueue();
    }
    fileQueue = new LargeFileObjectQueue<>(strategy.persistFile(), converter);
    if (strategy.isLoadFromFile()) {
      LOGGER.info("Before: fileQueue.size={}, fileQueue.peek={}, memoryQueue.size={}, memoryQueue.peek={}", fileQueue
          .size(), fileQueue.peek(), memoryQueue.size(), memoryQueue.peek());
      populateMemoryIfNeeded();
      LOGGER.info("After: fileQueue.size={}, fileQueue.get={},memoryQueue.size={}, memoryQueue.peek={}", fileQueue
          .size(), fileQueue.peek(), memoryQueue.size(), memoryQueue.peek());
    }
  }

  private void clearFileQueue() {
    try {
      Files.deleteIfExists(strategy.persistFile().toPath());
    } catch (IOException e) {
      LOGGER.error("Clear file error.", e);
    }
  }

  @Override
  public synchronized int size() {
    return memoryQueue.size() + fileQueue.size();
  }

  @Override
  public synchronized void add(T entry) {
    if (memoryQueue.size() < strategy.memoryCapacity() && fileQueue.size() == 0) {
      memoryQueue.add(entry);
      return;
    }
    LOGGER.debug("Adding to file queue...{}", entry);
    fileQueue.add(entry);
  }

  @Override
  public synchronized T peek() {
    populateMemoryIfNeeded();
    return memoryQueue.peek();
  }

  @Override
  public synchronized void remove() {
    memoryQueue.remove();
  }

  @Override
  public void setListener(Listener listener) {
    // nothing
  }

  public void populateMemoryIfNeeded() {
    int threshold = strategy.memoryCapacity() / strategy.populateRatio();

    if (memoryQueue.size() <= threshold && fileQueue.size() > 0) {
      LOGGER.info("Start populate, memoryQueue.size={}, memoryQueue.peek={}, fileQueue.size={}, fileQueue.peek={}",
          memoryQueue.size(), memoryQueue.peek(), fileQueue.size(), fileQueue.peek());
      while (memoryQueue.size() < strategy.memoryCapacity()) {
        T e = fileQueue.peek();
        if (e == null) {
          LOGGER.info("File queue is empty.");
          break;
        }
        LOGGER.debug("Populating to memory queue...{}", e);
        memoryQueue.add(e);
        fileQueue.remove();
      }
    }
  }
}
