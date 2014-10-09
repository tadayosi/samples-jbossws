package com.redhat.samples.ws;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

public class HttpHeadersHandler implements SOAPHandler<SOAPMessageContext> {

    private static final Logger LOGGER = Logger.getLogger(HttpHeadersHandler.class);

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        LOGGER.info("Attaching HTTP headers to response");
        @SuppressWarnings("unchecked")
        Map<String, List<String>> headers = (Map<String, List<String>>) context
                .get(MessageContext.HTTP_RESPONSE_HEADERS);
        if (headers == null) {
            headers = new HashMap<String, List<String>>();
            context.put(MessageContext.HTTP_RESPONSE_HEADERS, headers);
        }
        headers.put("Custom-Header", Arrays.asList("XXXXX"));
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {}

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

}
