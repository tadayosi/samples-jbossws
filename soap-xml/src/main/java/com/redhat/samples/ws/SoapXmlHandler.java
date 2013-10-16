package com.redhat.samples.ws;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class SoapXmlHandler implements SOAPHandler<SOAPMessageContext> {

  public static final String SOAP_XML = "com.redhat.samples.ws.SOAP_XML";

  @Override
  public boolean handleMessage(SOAPMessageContext context) {
    boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
    if (outbound) return true;

    SOAPMessage message = context.getMessage();
    OutputStream soapOut = new ByteArrayOutputStream();
    try {
      message.writeTo(soapOut);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    context.put(SOAP_XML, soapOut.toString());
    context.setScope(SOAP_XML, MessageContext.Scope.APPLICATION);
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
