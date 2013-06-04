package com.redhat.samples.ws;

import java.awt.Image;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class ImageResponse {
  private Image data;

  public ImageResponse() {}

  public ImageResponse(Image data) {
    this.data = data;
  }

  public Image getData() {
    return data;
  }

  public void setData(Image data) {
    this.data = data;
  }

}
