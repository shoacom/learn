package com.shoa.springboot;

import com.shoa.springboot.webservice.WebConnectorManager;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.SecuredRedirectHandler;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Created by xiojiang on 2017/7/4.
 */
@Configuration
public class AppConfig {

  @Bean
  public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory(WebConnectorManager
                                                                                       webConnectorManager) {
    JettyEmbeddedServletContainerFactory containerFactory = new JettyEmbeddedServletContainerFactory();
    containerFactory.addServerCustomizers(server -> {
      clearDefaultConnectors(server);
      webConnectorManager.initialize(server);
      addRedirectHandler(server);
    });

    return containerFactory;
  }

  private void clearDefaultConnectors(Server server) {
    Arrays.stream(server.getConnectors()).forEach(server::removeConnector);
  }

  private void addRedirectHandler(Server server) {
    Handler handler = new SecuredRedirectHandler();
    final Handler currentHandler = server.getHandler();
    if (currentHandler == null) {
      server.setHandler(handler);
    } else {
      if (currentHandler instanceof HandlerList) {
        ((HandlerList) currentHandler).addHandler(handler);
      } else {
        final HandlerList handlerList = new HandlerList();
        handlerList.addHandler(handler);
        handlerList.addHandler(currentHandler);
        server.setHandler(handlerList);
      }
    }
  }
}
