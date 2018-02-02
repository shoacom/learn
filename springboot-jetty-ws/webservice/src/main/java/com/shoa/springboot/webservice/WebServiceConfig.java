package com.shoa.springboot.webservice;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * Created by xiojiang on 2017/8/2.
 */
@Configuration
public class WebServiceConfig {
  @Bean(name = "cxfServletRegistration")
  public ServletRegistrationBean getCxfDispatcherServlet() {
    return new ServletRegistrationBean(new CXFServlet(), "/soap/*");
  }

  @Bean(name = Bus.DEFAULT_BUS_ID)
  public SpringBus springBus() {
    return new SpringBus();
  }

  @Bean
  public HelloService helloService() {
    return new HelloServiceImpl();
  }

  @Bean
  public Endpoint endpoint() {
    Endpoint endpoint = new EndpointImpl(springBus(), helloService());
    endpoint.publish("/hello");
    return endpoint;
  }
}
