package com.redhat.samples.ws.client;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

public class JmsClient implements MessageListener {

  //@formatter:off
  private static final String REQUEST_TEMPLATE =
        "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:ws='http://ws.samples.redhat.com/'>"
      +   "<soapenv:Body>"
      +     "<ws:hello>"
      +       "<name>%s</name>"
      +     "</ws:hello>"
      +   "</soapenv:Body>"
      + "</soapenv:Envelope>";
  //@formatter:on

  private static final Logger LOGGER           = Logger.getLogger(JmsClient.class);

  private static final String QUEUE_REQUEST    = "queue/samples.ws.greeting.request";
  private static final String QUEUE_RESPONSE   = "queue/samples.ws.greeting.response";

  private Queue               requestQueue;
  private Queue               responseQueue;

  private QueueConnection     connection;
  private QueueSession        session;
  private QueueReceiver       receiver;

  public JmsClient() throws Exception {
    InitialContext context = new InitialContext();
    requestQueue = (Queue) context.lookup(QUEUE_REQUEST);
    responseQueue = (Queue) context.lookup(QUEUE_RESPONSE);

    QueueConnectionFactory factory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
    connection = factory.createQueueConnection();
    session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
    receiver = session.createReceiver(responseQueue);
    receiver.setMessageListener(this);
    connection.start();
  }

  public void send(String request) throws JMSException {
    TextMessage message = session.createTextMessage(request);
    message.setJMSReplyTo(responseQueue);
    QueueSender sender = null;
    try {
      sender = session.createSender(requestQueue);
      sender.send(message);
    } finally {
      if (sender != null) sender.close();
    }
  }

  public void onMessage(Message msg) {
    TextMessage textMessage = (TextMessage) msg;
    try {
      String response = textMessage.getText();
      LOGGER.info("Response:\n" + format(response));
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  private static String format(String xml) throws Exception {
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    StringWriter writer = new StringWriter();
    transformer.transform(new StreamSource(new ByteArrayInputStream(xml.getBytes())), new StreamResult(writer));
    return writer.toString();
  }

  public void close() {
    try {
      if (receiver != null) receiver.close();
      if (connection == null) connection.stop();
      if (session == null) session.close();
      if (connection == null) connection.close();
    } catch (JMSException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception {
    JmsClient client = null;
    try {
      client = new JmsClient();
      String request = String.format(REQUEST_TEMPLATE, JmsClient.class.getSimpleName());
      client.send(request);
      Thread.sleep(1000);
    } finally {
      if (client != null) client.close();
    }
  }

}
