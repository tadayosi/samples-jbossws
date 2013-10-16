package com.redhat.samples.ws;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import javax.xml.ws.WebServiceContext;

import org.apache.log4j.Logger;

@WebService
@HandlerChain(file = "/META-INF/handler-chain.xml")
public class GreetingService {
  private static final Logger LOGGER = Logger.getLogger(GreetingService.class);

  @Resource
  WebServiceContext           context;

  @WebMethod
  public String hello(@WebParam(name = "name") String name) {
    LOGGER.info(getSoapXml());
    return String.format("Hello, %s!", name);
  }

  @WebMethod
  public String goodbye(@WebParam(name = "name") String name) {
    LOGGER.info(getSoapXml());
    return String.format("Goodbye, %s!", name);
  }

  private String getSoapXml() {
    return (String) context.getMessageContext().get(SoapXmlHandler.SOAP_XML);
  }

  public static void main(String[] args) {
    try {
      Endpoint.publish("http://localhost:18080/greeting", new GreetingService());
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    }
  }

}
