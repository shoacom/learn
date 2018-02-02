package com.shoa.springboot.webservice;


import com.shoa.springboot.webservice.security.HttpConfigurationProvider;
import com.shoa.springboot.webservice.security.SslContextFactoryProvider;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class WebConnectorManager {
  private static final String HOST_V4 = "localhost";
  private static final int PORT_TLS = 8443;
  private static final int PORT_HTTP = 8080;
  private Server server;
  @Value("${spring.arg.myname:shoa}")
  private String myName;

  WebConnectorManager() {
    System.out.println(myName);
  }

  public void initialize(Server server) {
    this.server = server;
  }

  private ServerConnector getHttpConnector(String host, int port) {
    return Arrays.stream(server.getConnectors())
        .filter(connector -> connector instanceof ServerConnector).map(connector -> (ServerConnector) connector)
        .filter(serverConnector -> serverConnector.getPort() == port && host.equals(serverConnector.getHost()))
        .findAny().orElse(createConnector(host, port));
  }

  private ServerConnector createConnector(String host, int port) {
    return port == 8443 ? createHttpsConnector(host) : createHttpConnector(host);
  }

  private ServerConnector createHttpConnector(String host) {
    ServerConnector connector = new ServerConnector(server,
        new HttpConnectionFactory(new HttpConfigurationProvider()));
    connector.setHost(host);
    connector.setPort(8080);
    return connector;
  }


  private ServerConnector createHttpsConnector(String host) {
    ServerConnector connector = new ServerConnector(server,
        new SslConnectionFactory(new SslContextFactoryProvider(), HttpVersion.HTTP_1_1.asString()),
        new HttpConnectionFactory(new HttpConfigurationProvider(true)));
    connector.setPort(8443);
    connector.setHost(host);
    return connector;
  }

  public void start() throws Exception {
    getHttpConnector(HOST_V4, PORT_TLS).start();
    getHttpConnector(HOST_V4, PORT_HTTP).start();
  }


}
