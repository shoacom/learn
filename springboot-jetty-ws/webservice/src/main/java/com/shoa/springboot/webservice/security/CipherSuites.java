package com.shoa.springboot.webservice.security;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by xiojiang on 2017/8/21.
 */
public enum CipherSuites {
  TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,
  TLS_DHE_RSA_WITH_AES_256_CBC_SHA256,
  TLS_DHE_RSA_WITH_AES_256_CBC_SHA,
  TLS_RSA_WITH_AES_256_GCM_SHA384,
  TLS_RSA_WITH_AES_256_CBC_SHA256,
  TLS_RSA_WITH_AES_256_CBC_SHA,
  TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
  TLS_DHE_RSA_WITH_AES_128_CBC_SHA256,
  TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
  TLS_RSA_WITH_AES_128_GCM_SHA256,
  TLS_RSA_WITH_AES_128_CBC_SHA256;

  public static String[] allValues() {
    return Arrays.stream(CipherSuites.values()).map(CipherSuites::name).collect(Collectors.toList()).toArray(new
        String[]{});
  }
}
