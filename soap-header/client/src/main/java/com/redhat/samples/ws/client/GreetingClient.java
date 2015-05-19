package com.redhat.samples.ws.client;

import java.io.PrintWriter;
import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.apache.log4j.Logger;

import com.redhat.samples.ws.*;

public class GreetingClient {

    private static final Logger LOGGER = Logger.getLogger(GreetingClient.class);

    private static final URL WSDL = GreetingClient.class.getResource("/META-INF/greeting.wsdl");
    private static final QName SERVICE = new QName("http://ws.samples.redhat.com/", "GreetingService");
    //private static final String ENDPOINT = "http://localhost:18080/greeting";
    private static final String ENDPOINT = "http://localhost:18080/sample/greeting";

    public static void main(String[] args) {
        new GreetingClient().invokeService(new PrintWriter(System.out, true));
    }

    public void invokeService(PrintWriter writer) {
        GreetingService service = createService();
        HelloResponse helloResponse = service.hello(hello("Eren"), "Yeager");
        GoodbyeResponse goodbyeResponse = service.goodbye(goodbye("Mikasa"), "Ackerman");
        LOGGER.info(helloResponse.getReturn());
        LOGGER.info(goodbyeResponse.getReturn());
        writer.println(helloResponse.getReturn());
        writer.println(goodbyeResponse.getReturn());
    }

    private Hello hello(String name) {
        Hello h = new Hello();
        h.setFirstName(name);
        return h;
    }

    private Goodbye goodbye(String name) {
        Goodbye g = new Goodbye();
        g.setFirstName(name);
        return g;
    }

    private static GreetingService createService() {
        GreetingService service = Service.create(WSDL, SERVICE).getPort(GreetingService.class);
        Map<String, Object> context = ((BindingProvider) service).getRequestContext();
        context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, ENDPOINT);
        return service;
    }

}
