package com.shoa.springboot.webservice;


import com.shoa.springboot.webservice.security.HttpConfigurationProvider;
import com.shoa.springboot.webservice.security.SslContextFactoryProvider;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class WebConnectorManager {
  private Server server;
  private WebServiceProperty webServiceProperty;

  public WebConnectorManager(WebServiceProperty webServiceProperty) {
    this.webServiceProperty = webServiceProperty;
  }

  public void initialize(Server server) {
    this.server = server;
  }

  private ServerConnector getHttpConnector(String host, int port) {
    return Arrays.stream(server.getConnectors())
        .filter(connector -> connector instanceof ServerConnector).map(connector -> (ServerConnector) connector)
        .filter(serverConnector -> serverConnector.getPort() == port && host.equals(serverConnector.getHost()))
        .findAny().orElse(createServerConnector(host, port));
  }

  private ServerConnector createServerConnector(String host, int port) {
    final ServerConnector serverConnector;
    if (port == webServiceProperty.getHttpsPort()) {
      serverConnector = new ServerConnector(server,
          new SslConnectionFactory(new SslContextFactoryProvider(), HttpVersion.HTTP_1_1.asString()),
          new HttpConnectionFactory(new HttpConfigurationProvider(true)));
    } else {
      serverConnector = new ServerConnector(server,
          new HttpConnectionFactory(new HttpConfigurationProvider()));
    }

    serverConnector.setHost(host);
    serverConnector.setPort(port);
    server.addConnector(serverConnector);
    return serverConnector;
  }


  public void start() {
    webServiceProperty.getIpAddresss().forEach(inetAddress -> {
      try {
        getHttpConnector(inetAddress.getHostAddress(), webServiceProperty.getHttpPort()).start();
        getHttpConnector(inetAddress.getHostAddress(), webServiceProperty.getHttpsPort()).start();
      } catch (Exception e) {
        throw new ConnectorException("Connector start failed!", e);
      }
    });
  }


}
