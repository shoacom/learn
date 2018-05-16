package com.shoa.queue.vo;

/**
 * Created by xiojiang on 2018/5/11.
 */
public class IdGenerator {


  public static int generate(Object obj) {
    return Integer.parseInt(obj.toString());
  }

  private IdGenerator() {
  }
}
