package com.shoa.queue.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xiojiang on 2018/5/16.
 */
public class Monitor {
  private static final Logger LOGGER = LoggerFactory.getLogger(Monitor.class);
  private static final int PATCH_COUNT = 1000;
  private static volatile Monitor monitor;
  private List processedList = new CopyOnWriteArrayList<>();

  public static Monitor getInstance() {
    if (monitor == null) {
      monitor = new Monitor();
    }
    return monitor;
  }

  public void addToMonitor(Object obj) {
    processedList.add(obj);
    int size = processedList.size();
    String first = processedList.get(0).toString();
    String last = processedList.get(size - 1).toString();

    if (size >= PATCH_COUNT) {
      LOGGER.debug("Processed elements. total: {} [{}-{}]", size, first, last);
      if ((size - 1) != (Integer.valueOf(last) - Integer.valueOf(first))) {
        LOGGER.warn("All elements. {} ", processedList);
      }
      processedList.clear();
    }
  }


}
