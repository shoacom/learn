package com.shoa.springboot.webservice.security;

import static com.shoa.springboot.webservice.Constants.*;

import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * Created by xiojiang on 2017/8/22.
 */

public class SslContextFactoryProvider extends SslContextFactory {

  public SslContextFactoryProvider() {
    setKeyManagerPassword(KEYSTORE_PASS);
    setTrustStorePath(KEYSTORE_PATH);
    setTrustStorePassword(KEYSTORE_PASS);
    setKeyStorePath(KEYSTORE_PATH);
    setKeyStorePassword(KEYSTORE_PASS);
    setIncludeCipherSuites(CipherSuites.allValues());
    setNeedClientAuth(false);
  }


}
