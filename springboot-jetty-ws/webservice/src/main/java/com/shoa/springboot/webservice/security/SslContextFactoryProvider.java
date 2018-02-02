package com.shoa.springboot.webservice.security;

import static com.shoa.springboot.webservice.Constants.KEYSTORE_PASS;
import static com.shoa.springboot.webservice.Constants.KEYSTORE_PATH;

import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by xiojiang on 2017/8/22.
 */

public class SslContextFactoryProvider extends SslContextFactory {
  private static final Logger LOGGER = LoggerFactory.getLogger(SslContextFactoryProvider.class);

  public SslContextFactoryProvider() {
    File file = new File(KEYSTORE_PATH);
    LOGGER.info("Keystore path: " + file.getAbsolutePath());
    setKeyManagerPassword(KEYSTORE_PASS);
    setTrustStorePath(KEYSTORE_PATH);
    setTrustStorePassword(KEYSTORE_PASS);
    setKeyStorePath(KEYSTORE_PATH);
    setKeyStorePassword(KEYSTORE_PASS);
    setIncludeCipherSuites(CipherSuites.allValues());
    setNeedClientAuth(false);
  }


}
