package com.shoa.springboot.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by xiojiang on 2017/8/2.
 */
@WebService
public interface HelloService {

  @WebMethod
  String hello(@WebParam(name = "who") String who);


}
