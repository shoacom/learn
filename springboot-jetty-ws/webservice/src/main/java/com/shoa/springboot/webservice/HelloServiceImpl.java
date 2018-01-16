package com.shoa.springboot.webservice;

/**
 * Created by xiojiang on 2017/8/2.
 */
public class HelloServiceImpl implements HelloService {
  @Override
  public String hello(String who) {
    return "Hello " + who;
  }
}
