package com.shoa.springboot.webservice.security;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.SecureRequestCustomizer;

/**
 * Created by xiojiang on 2017/8/22.
 */
public class HttpConfigurationProvider extends HttpConfiguration {

  public HttpConfigurationProvider() {
    setSecureScheme("https");
    setSecurePort(8443);
    setOutputBufferSize(32768);
    setRequestHeaderSize(8192);
    setResponseHeaderSize(8192);
    setSendServerVersion(true);
    setSendDateHeader(false);

  }

  public HttpConfigurationProvider(boolean isHttps) {
    this();
    if (isHttps)
      addCustomizer(new SecureRequestCustomizer());
  }
}
