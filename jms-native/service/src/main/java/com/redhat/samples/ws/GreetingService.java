package com.redhat.samples.ws;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.apache.log4j.Logger;
import org.jboss.ws.core.transport.jms.JMSTransportSupportEJB3;

@WebService(wsdlLocation = "META-INF/wsdl/greeting.wsdl")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/samples.ws.greeting.request") },
    messageListenerInterface = javax.jms.MessageListener.class)
public class GreetingService extends JMSTransportSupportEJB3 {
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
