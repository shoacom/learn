package com.shoa.queue;

import java.io.File;

/**
 * Created by xiojiang on 2018/5/9.
 */
public interface PersistenceStrategy {
  File persistFile();

  int memoryCapacity();

  int populateRatio();

  boolean isLoadFromFile();
}
