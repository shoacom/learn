package com.shoa.queue.vo;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Random;

/**
 * Created by xiojiang on 2018/5/11.
 */
public class Notification implements Serializable {
  private int id;
  private String neId;
  private Type type;
  private transient InputStream inputStream;

  public Notification(int id, String neId, Type type) {
    this.id = id;
    this.neId = neId;
    this.type = type;
    inputStream = getClass().getResourceAsStream("/attachment_large.xml");
  }

  @Override
  public String toString() {
    return "Notification{" +
        "id=" + id +
        ", neId='" + neId + '\'' +
        ", type=" + type +
        ", inputStream=" + inputStream +
        '}';
  }

  public int getId() {
    return id;
  }

  public String getNeId() {
    return neId;
  }

  public Type getType() {
    return type;
  }

  public InputStream getInputStream() {
    return inputStream;
  }

  enum Type {
    CM,
    FM,
    PM
  }

  public static Notification create(Object id) {
    Random random = new Random();
    return new Notification((int) id, String.valueOf(random.nextInt(10)), Type.values()[random.nextInt(3)]);
  }

  public static StorableNotification convert(Notification notification) {
    try {
      return new StorableNotification(notification, IOUtils.toByteArray(notification.getInputStream()));
    } catch (IOException e) {
      throw new IllegalArgumentException("Error", e);
    }
  }

  public static StorableNotification createAndConvert(Object id) {
    return convert(create(id));
  }
}
