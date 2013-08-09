package com.redhat.samples.ws;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class GreetingClient {

  private static final URL    WSDL              = GreetingClient.class.getResource("/META-INF/greeting.wsdl");
  private static final QName  SERVICE           = new QName("http://ws.samples.redhat.com/", "GreetingService");

  private static final String KEYSTORE          = System.getProperty("user.home") + "/.keystore";
  private static final String KEYSTORE_PASSWORD = "password";

  public static void main(String[] args) {
    configureSSL();
    invokeService();
  }

  private static void configureSSL() {
    System.setProperty("javax.net.ssl.keyStoreType", "JKS");
    System.setProperty("javax.net.ssl.keyStore", KEYSTORE);
    System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);
    System.setProperty("javax.net.ssl.trustStore", KEYSTORE);
    System.setProperty("javax.net.ssl.trustStorePassword", KEYSTORE_PASSWORD);
  }

  private static void invokeService() {
    GreetingService service = Service.create(WSDL, SERVICE).getPort(GreetingService.class);
    System.out.println(service.hello(GreetingClient.class.getSimpleName()));
    System.out.println(service.goodbye(GreetingClient.class.getSimpleName()));
  }
}
