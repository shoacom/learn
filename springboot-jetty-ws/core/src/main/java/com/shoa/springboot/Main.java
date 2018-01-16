package com.shoa.springboot;

import com.shoa.springboot.webservice.WebServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

  public static void main(String[] args) {
    startWeb();
  }


  private static void startWeb() {
    ConfigurableApplicationContext context = new SpringApplicationBuilder(Main.class).run();
    WebServer webServer = context.getBean(WebServer.class);
    webServer.start();
  }

}
