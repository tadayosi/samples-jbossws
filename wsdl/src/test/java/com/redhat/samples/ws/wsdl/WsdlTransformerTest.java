package com.redhat.samples.ws.wsdl;

import static org.custommonkey.xmlunit.XMLAssert.*;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.ElementNameAndAttributeQualifier;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;

public class WsdlTransformerTest {

  @Before
  public void setUp() {
    XMLUnit.setIgnoreWhitespace(true);
  }

  @Test
  public void inlineSchemas() throws Exception {
    String expected = IOUtils.toString(getClass().getResource("/wsdl/expected.wsdl"));
    String wsdl = getClass().getResource("/wsdl/greeting.wsdl").getPath();
    String inlined = new WsdlTransformer().inlineSchemas(wsdl);
    System.out.println(inlined);
    assertWSDLEqual(expected, inlined);
  }

  private static void assertWSDLEqual(String expected, String inlined) throws Exception {
    Diff diff = new Diff(expected, inlined);
    diff.overrideElementQualifier(new ElementNameAndAttributeQualifier());
    assertXMLEqual(diff, true);
  }

}
