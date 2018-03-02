package com.shoa.springboot.webservice;

/**
 * Created by xiojiang on 2018/3/1.
 */
public class ConnectorException extends RuntimeException {
  ConnectorException(String msg, Throwable e) {
    super(msg, e);
  }
}
