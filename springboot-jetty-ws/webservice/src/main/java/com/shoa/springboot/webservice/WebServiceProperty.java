package com.shoa.springboot.webservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xiojiang on 2018/3/1.
 */
@Component
public class WebServiceProperty {

  private final Set<InetAddress> ipAddresss = new HashSet<>();
  private final int httpPort;
  private final int httpsPort;

  public WebServiceProperty(@Value("${webservice.hosts}") String ipHosts,
                            @Value("${webservice.port.http}") int httpPort,
                            @Value("${webservice.port.https}") int httpsPort) {
    try {
      String[] hosts = ipHosts.split(",");
      for (String host : hosts) {
        ipAddresss.add(InetAddress.getByName(host));
      }
    } catch (UnknownHostException e) {
      throw new IllegalArgumentException("IllegalArgumentException", e);
    }
    this.httpPort = httpPort;
    this.httpsPort = httpsPort;

  }

  public Set<InetAddress> getIpAddresss() {
    return ipAddresss;
  }

  public int getHttpPort() {
    return httpPort;
  }

  public int getHttpsPort() {
    return httpsPort;
  }

}
