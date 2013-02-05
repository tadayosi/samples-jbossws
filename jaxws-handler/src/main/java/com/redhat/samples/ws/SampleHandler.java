package com.redhat.samples.ws;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

public class SampleHandler implements SOAPHandler<SOAPMessageContext> {
  private static final Logger LOGGER = Logger.getLogger(SampleHandler.class);

  @Override
  public boolean handleMessage(SOAPMessageContext context) {
    LOGGER.info("*** handleMessage ***");
    return true;
  }

  @Override
  public boolean handleFault(SOAPMessageContext context) {
    LOGGER.info("*** handleFault ***");
    return true;
  }

  @Override
  public void close(MessageContext context) {
    LOGGER.info("*** close ***");
  }

  @Override
  public Set<QName> getHeaders() {
    LOGGER.info("*** getHeaders ***");
    return null;
  }
}
