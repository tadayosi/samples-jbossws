package com.redhat.samples.ws.client;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import sun.awt.image.ToolkitImage;

import com.redhat.samples.ws.DataRequest;
import com.redhat.samples.ws.DataResponse;
import com.redhat.samples.ws.ImageRequest;
import com.redhat.samples.ws.ImageResponse;
import com.redhat.samples.ws.MTOMService;

@SuppressWarnings("restriction")
public class MTOMClient {
  private static final Logger LOGGER       = Logger.getLogger(MTOMClient.class);

  private static final String ENDPOINT     = "http://localhost:18080/MTOMService";
  //private static final String ENDPOINT     = "http://localhost:8080/sample/MTOMService";
  private static QName        SERVICE_NAME = new QName("http://ws.samples.redhat.com/", "MTOMService");

  private MTOMService         service;

  public MTOMClient() throws MalformedURLException {
    service = Service.create(new URL(ENDPOINT + "?wsdl"), SERVICE_NAME).getPort(MTOMService.class);

    BindingProvider provider = (BindingProvider) service;
    //provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:18081/MTOMService");
    // enable MTOM
    ((SOAPBinding) provider.getBinding()).setMTOMEnabled(true);
  }

  public void run() throws IOException {
    byte[] bytes = IOUtils.toByteArray(MTOMClient.class.getResourceAsStream("/image.png"));
    Image image = Toolkit.getDefaultToolkit().createImage(bytes);

    ImageResponse response = service.echoImage(new ImageRequest(image));
    assertEquals(image, response.getData());

    DataResponse response2 = service.echoData(new DataRequest(new DataHandler(image, "image/x-png")));
    byte[] respBytes = IOUtils.toByteArray(((InputStream) response2.getDataHandler().getContent()));
    assertEquals(image, Toolkit.getDefaultToolkit().createImage(respBytes));
  }

  private static void assertEquals(Image expected, Image actual) {
    int expectedHeight = ((ToolkitImage) expected).getHeight();
    int expectedWidth = ((ToolkitImage) expected).getWidth();
    int actualHeight = 0;
    int actualWidth = 0;
    if (actual instanceof BufferedImage) {
      actualHeight = ((BufferedImage) actual).getHeight();
      actualWidth = ((BufferedImage) actual).getWidth();
    } else {
      actualHeight = ((ToolkitImage) actual).getHeight();
      actualWidth = ((ToolkitImage) actual).getWidth();
    }
    assertEquals("Image heights don't match.", expectedHeight, actualHeight);
    assertEquals("Image widths don't match.", expectedWidth, actualWidth);
    LOGGER.info("Successfully sent image data (binary) to MTOM Service endpoint, which echoed it back!");
  }

  private static void assertEquals(String failureMessage, Object expected, Object actual) {
    if (!expected.equals(actual)) {
      String message = String.format("%s - Expected '%s', actual '%s'.", failureMessage, expected, actual);
      throw new RuntimeException(message);
    }
  }

  public static void main(String[] args) throws Exception {
    new MTOMClient().run();
  }

}
