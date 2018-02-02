package com.shoa.springboot.webservice;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WebServer {
  private static final Logger LOGGER = LoggerFactory.getLogger(WebServer.class);
  private WebConnectorManager webConnectorManager;

  public WebServer(WebConnectorManager webConnectorManager) {
    this.webConnectorManager = webConnectorManager;
  }

  public void start() {
    try {
      webConnectorManager.start();
    } catch (Exception e) {
      LOGGER.error("Web Connector start failure!", e);
    }
  }


}
