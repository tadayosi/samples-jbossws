package com.redhat.samples.ws.client;

import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.apache.log4j.Logger;

import com.redhat.samples.ws.GreetingService;

public class GreetingClient {
  private static final Logger LOGGER   = Logger.getLogger(GreetingClient.class);

  private static final URL    WSDL     = GreetingClient.class.getResource("/META-INF/greeting.wsdl");
  private static final QName  SERVICE  = new QName("http://ws.samples.redhat.com/", "GreetingService");
  //private static final String ENDPOINT = "http://localhost:18080/greeting";
  private static final String ENDPOINT = "http://localhost:8080/sample/greeting";

  public static void main(String[] args) {
    invokeService();
  }

  private static void invokeService() {
    GreetingService service = createService();
    String helloResponse = service.hello(GreetingClientServlet.class.getSimpleName());
    String goodbyeResponse = service.goodbye(GreetingClientServlet.class.getSimpleName());
    LOGGER.info(helloResponse);
    LOGGER.info(goodbyeResponse);
  }

  private static GreetingService createService() {
    GreetingService service = Service.create(WSDL, SERVICE).getPort(GreetingService.class);
    Map<String, Object> context = ((BindingProvider) service).getRequestContext();
    context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, ENDPOINT);
    return service;
  }

}
