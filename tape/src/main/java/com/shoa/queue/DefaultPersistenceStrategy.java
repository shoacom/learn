package com.shoa.queue;

import java.io.File;

/**
 * Created by xiojiang on 2018/5/9.
 */
public class DefaultPersistenceStrategy implements PersistenceStrategy {
  private static final int MEMORY_CAPACITY = 400;
  private static final int POPULATE_RATIO = 2;
  private static final String PATHNAME = DefaultPersistenceStrategy.class.getResource("/").getPath() + "persistence" +
      ".lst";
  private static final boolean LOAD_FROM_FILE = false;


  @Override
  public File persistFile() {
    return new File(PATHNAME);
  }

  @Override
  public int memoryCapacity() {
    return MEMORY_CAPACITY;
  }

  @Override
  public int populateRatio() {
    return POPULATE_RATIO;
  }

  @Override
  public boolean isLoadFromFile() {
    return LOAD_FROM_FILE;
  }

  @Override
  public String toString() {
    return "DefaultPersistenceStrategy{" +
        "persistFile='" + persistFile().getAbsolutePath() + '\'' +
        ", memoryCapacity='" + MEMORY_CAPACITY + '\'' +
        ", populateRatio='" + POPULATE_RATIO + '\'' +
        ", isLoadFromFile='" + LOAD_FROM_FILE + '\'' +
        '}';
  }

}
