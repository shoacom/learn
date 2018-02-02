package com.shoa.springboot.webservice;


import org.springframework.stereotype.Component;

@Component
public class WebServer {

  private WebConnectorManager webConnectorManager;

  public WebServer(WebConnectorManager webConnectorManager) {
    this.webConnectorManager = webConnectorManager;
  }

  public void start() {
    try {
      webConnectorManager.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
