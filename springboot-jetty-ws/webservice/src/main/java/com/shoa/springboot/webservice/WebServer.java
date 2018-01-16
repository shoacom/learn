package com.shoa.springboot.webservice;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.xml.ws.Endpoint;

@Component
public class WebServer {

  private WebConnectorManager webConnectorManager;
  private Endpoint endpoint;

  public WebServer(WebConnectorManager webConnectorManager, @Qualifier("helloEndpoint") Endpoint endpoint) {
    this.webConnectorManager = webConnectorManager;
    this.endpoint = endpoint;
  }

  public void start() {
    try {
      webConnectorManager.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
    endpoint.publish("/hello");
  }


}
