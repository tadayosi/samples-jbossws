package com.redhat.samples.ws;

import java.awt.Image;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.jws.WebService;

@WebService(name = "MTOMEndpoint", serviceName = "MTOMService", endpointInterface = "com.redhat.samples.ws.MTOMService")
public class MTOMServiceImpl implements MTOMService {

  @Override
  public DataResponse echoData(DataRequest request) {
    DataHandler handler = request.getDataHandler();
    try {
      System.out.println("[echoData]  " + handler.getContent());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new DataResponse(handler);
  }

  @Override
  public ImageResponse echoImage(ImageRequest request) {
    Image image = request.getData();
    System.out.println("[echoImage] " + image);
    return new ImageResponse(image);
  }

}
