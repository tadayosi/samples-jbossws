package com.redhat.samples.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.log4j.Logger;

@WebService
public class GreetingService {
  private static final Logger LOGGER = Logger.getLogger(GreetingService.class);

  @WebMethod
  public String hello(@WebParam(name = "name") String name) {
    String message = String.format("Hello, %s!", name);
    LOGGER.info(message);
    return message;
  }

  @WebMethod
  public String goodbye(@WebParam(name = "name") String name) {
    String message = String.format("Goodbye, %s!", name);
    LOGGER.info(message);
    return message;
  }

}
