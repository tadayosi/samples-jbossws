package com.redhat.samples.ws;

import java.awt.Image;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class ImageRequest {
  private Image data;

  public ImageRequest() {}

  public ImageRequest(Image data) {
    this.data = data;
  }

  public Image getData() {
    return data;
  }

  public void setData(Image data) {
    this.data = data;
  }

}
