package com.redhat.samples.ws.client;

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

import org.apache.log4j.Logger;

import com.redhat.samples.ws.GreetingService;

@SuppressWarnings("serial")
public class GreetingClientServlet extends HttpServlet {
  private static final Logger LOGGER   = Logger.getLogger(GreetingClientServlet.class);

  private static final URL    WSDL     = GreetingClientServlet.class.getResource("/META-INF/greeting.wsdl");
  private static final QName  SERVICE  = new QName("http://ws.samples.redhat.com/", "GreetingService");
  //private static final String ENDPOINT = "http://localhost:18080/greeting";
  private static final String ENDPOINT = "http://localhost:8080/sample/greeting";

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    invokeService(response);
  }

  private void invokeService(HttpServletResponse response) throws IOException {
    GreetingService service = createService();
    String helloResponse = service.hello(GreetingClientServlet.class.getSimpleName());
    String goodbyeResponse = service.goodbye(GreetingClientServlet.class.getSimpleName());
    LOGGER.info(helloResponse);
    LOGGER.info(goodbyeResponse);
    response.getWriter().println(helloResponse);
    response.getWriter().println(goodbyeResponse);
  }

  private static GreetingService createService() {
    GreetingService service = Service.create(WSDL, SERVICE).getPort(GreetingService.class);
    Map<String, Object> context = ((BindingProvider) service).getRequestContext();
    context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, ENDPOINT);
    return service;
  }

}
