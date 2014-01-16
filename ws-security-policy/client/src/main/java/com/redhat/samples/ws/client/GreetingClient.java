package com.redhat.samples.ws.client;

import java.io.PrintWriter;
import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.apache.log4j.Logger;

import com.redhat.samples.ws.GreetingService;

public class GreetingClient {

    private static final Logger LOGGER = Logger.getLogger(GreetingClient.class);

    private static final URL WSDL = GreetingClient.class.getResource("/META-INF/greeting.wsdl");
    private static final QName SERVICE = new QName("http://ws.samples.redhat.com/", "GreetingService");
    //private static final String ENDPOINT = "http://localhost:18080/greeting";
    private static final String ENDPOINT = "http://localhost:8080/sample/greeting";

    private static final String USERNAME = "kermit";
    private static final String PASSWORD = "thefrog";

    public static void main(String[] args) {
        new GreetingClient().invokeService(GreetingClient.class.getSimpleName(), new PrintWriter(System.out, true));
    }

    public void invokeService(String name, PrintWriter writer) {
        GreetingService service = createService();
        String helloResponse = service.hello(name);
        String goodbyeResponse = service.goodbye(name);
        LOGGER.info(helloResponse);
        LOGGER.info(goodbyeResponse);
        writer.println(helloResponse);
        writer.println(goodbyeResponse);
    }

    private static GreetingService createService() {
        GreetingService service = Service.create(WSDL, SERVICE).getPort(GreetingService.class);
        Map<String, Object> context = ((BindingProvider) service).getRequestContext();
        context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, ENDPOINT);
        context.put("ws-security.username", USERNAME);
        context.put("ws-security.password", PASSWORD);
        return service;
    }

}
