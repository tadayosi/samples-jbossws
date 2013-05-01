package com.redhat.samples.ws;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.jboss.ws.core.StubExt;

@SuppressWarnings("serial")
public class GreetingClientServlet extends HttpServlet {

  private static final URL    WSDL              = GreetingClientServlet.class.getResource("/META-INF/greeting.wsdl");
  private static final QName  SERVICE           = new QName("http://ws.samples.redhat.com/", "GreetingService");

  private static final String KEYSTORE          = System.getProperty("user.home") + "/.keystore";
  private static final String KEYSTORE_PASSWORD = "password";

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    GreetingService service = Service.create(WSDL, SERVICE).getPort(GreetingService.class);
    configureSSL(service);
    invokeService(service, response);
  }

  private void configureSSL(GreetingService service) {
    Map<String, Object> context = ((BindingProvider) service).getRequestContext();
    context.put(StubExt.PROPERTY_KEY_STORE_TYPE, "JKS");
    context.put(StubExt.PROPERTY_KEY_STORE, KEYSTORE);
    context.put(StubExt.PROPERTY_KEY_STORE_PASSWORD, KEYSTORE_PASSWORD);
    context.put(StubExt.PROPERTY_TRUST_STORE, KEYSTORE);
    context.put(StubExt.PROPERTY_TRUST_STORE_PASSWORD, KEYSTORE_PASSWORD);
  }

  private void invokeService(GreetingService service, HttpServletResponse response) throws IOException {
    String hello = service.hello(GreetingClientServlet.class.getSimpleName());
    String goodbye = service.goodbye(GreetingClientServlet.class.getSimpleName());
    System.out.println(hello);
    System.out.println(goodbye);
    response.getWriter().println(hello);
    response.getWriter().println(goodbye);
  }

}
