package com.shoa.queue.vo;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by xiojiang on 2018/5/15.
 */
public class StorableNotification implements Serializable {
  private Notification notification;
  private byte[] bytes;

  public StorableNotification(Notification notification, byte[] bytes) {
    this.notification = notification;
    this.bytes = bytes;
  }

  public Notification getNotification() {
    return notification;
  }

  public byte[] getBytes() {
    return bytes;
  }

  @Override
  public String toString() {
    return "StorableNotification{" +
        "notification=" + notification +
        ", bytes=" + Arrays.toString(bytes) +
        '}';
  }
}
