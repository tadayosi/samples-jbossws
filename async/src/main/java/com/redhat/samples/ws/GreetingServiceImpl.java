package com.redhat.samples.ws;

import java.util.concurrent.Future;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Endpoint;

import org.apache.cxf.annotations.UseAsyncMethod;
import org.apache.cxf.jaxws.ServerAsyncResponse;
import org.apache.log4j.Logger;

@WebService(serviceName = "GreetingServiceImpl", portName = "GreetingServicePort")
public class GreetingServiceImpl implements GreetingService {

    private static final Logger LOGGER = Logger.getLogger(GreetingServiceImpl.class);

    @WebMethod
    //@UseAsyncMethod(always = true)
    @UseAsyncMethod
    public String hello(@WebParam(name = "name") String name) {
        String message = String.format("Hello, %s! (SYNC)", name);
        LOGGER.info(message);
        return message;
    }

    public Future<HelloResponse> helloAsync(final String name, final AsyncHandler<HelloResponse> asyncHandler) {
        final ServerAsyncResponse<HelloResponse> response = new ServerAsyncResponse<HelloResponse>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HelloResponse message = new HelloResponse();
                message.setReturn(String.format("Hello, %s! (ASYNC)", name));
                LOGGER.info(message.getReturn());
                response.set(message);
                asyncHandler.handleResponse(response);
            }
        }).start();
        return response;
    }

    @WebMethod
    public String goodbye(@WebParam(name = "name") String name) {
        String message = String.format("Goodbye, %s!", name);
        LOGGER.info(message);
        return message;
    }

    public static void main(String[] args) {
        try {
            Endpoint.publish("http://localhost:18080/greeting", new GreetingServiceImpl());
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

}
