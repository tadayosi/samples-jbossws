package com.redhat.samples.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.BindingType;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.binding.soap.SoapFault;
import org.apache.log4j.Logger;

@WebService
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
//@BindingType("http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class GreetingService {

  private static final Logger LOGGER        = Logger.getLogger(GreetingService.class);

  private static final String NS_SAMPLES    = "http://ws.samples.redhat.com/";
  private static final String FAULT_SUBCODE = "NameIsNullOrEmpty";
  private static final String FAULT_REASON  = "'name' is null or empty.";

  @WebMethod
  public String hello(@WebParam(name = "name") String name) {
    if (StringUtils.isEmpty(name)) { throw createSOAPFaultException(FAULT_SUBCODE, FAULT_REASON); }
    String message = String.format("Hello, %s!", name);
    LOGGER.info(message);
    return message;
  }

  @WebMethod
  public String goodbye(@WebParam(name = "name") String name) {
    if (StringUtils.isEmpty(name)) { throw createCXFSoapFault(FAULT_SUBCODE, FAULT_REASON); }
    String message = String.format("Goodbye, %s!", name);
    LOGGER.info(message);
    return message;
  }

  private SOAPFaultException createSOAPFaultException(String code, String reason) {
    LOGGER.info("SOAP Fault: " + reason);
    try {
      SOAPFactory factory = SOAPFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
      SOAPFault fault = factory.createFault(reason, SOAPConstants.SOAP_RECEIVER_FAULT);
      fault.appendFaultSubcode(new QName(NS_SAMPLES, code + 1));
      fault.appendFaultSubcode(new QName(NS_SAMPLES, code + 2));
      fault.appendFaultSubcode(new QName(NS_SAMPLES, code + 3));
      fault.addDetail().addChildElement("message").setTextContent(reason);
      return new SOAPFaultException(fault);
    } catch (SOAPException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private SoapFault createCXFSoapFault(String code, String reason) {
    SoapFault fault = new SoapFault(reason, SoapFault.FAULT_CODE_SERVER);
    fault.setSubCode(new QName(NS_SAMPLES, code));
    fault.getOrCreateDetail().appendChild(fault.getDetail().getOwnerDocument().createElement("message"))
        .setTextContent(reason);
    return fault;
  }

  public static void main(String[] args) {
    try {
      Endpoint.publish("http://localhost:18080/greeting", new GreetingService());
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    }
  }

}
