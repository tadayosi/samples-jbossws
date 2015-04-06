package com.redhat.samples.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.configuration.security.FiltersType;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;

@SuppressWarnings("serial")
public class GreetingClientServlet extends HttpServlet {

    private static final URL WSDL = GreetingClientServlet.class.getResource("/META-INF/greeting.wsdl");
    private static final QName SERVICE = new QName("http://ws.samples.redhat.com/", "GreetingService");

    private static final String KEYSTORE = System.getProperty("user.home") + "/.keystore";
    private static final String KEYSTORE_PASSWORD = "password";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GreetingService service = Service.create(WSDL, SERVICE).getPort(GreetingService.class);
        configureSSL(service);
        invokeService(service, response);
    }

    private void configureSSL(GreetingService service) {
        HTTPConduit conduit = (HTTPConduit) ClientProxy.getClient(service).getConduit();
        TLSClientParameters params = new TLSClientParameters();
        try {
            params.setTrustManagers(trustManagers());
            params.setKeyManagers(keyManagers());
            params.setCipherSuitesFilter(cipherSuitesFilter());
            conduit.setTlsClientParameters(params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private KeyManager[] keyManagers() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(new File(KEYSTORE)), KEYSTORE_PASSWORD.toCharArray());
        KeyManagerFactory factory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        factory.init(keyStore, KEYSTORE_PASSWORD.toCharArray());
        return factory.getKeyManagers();
    }

    private TrustManager[] trustManagers() throws Exception {
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream(new File(KEYSTORE)), KEYSTORE_PASSWORD.toCharArray());
        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init(trustStore);
        return factory.getTrustManagers();
    }

    private FiltersType cipherSuitesFilter() {
        FiltersType filter = new FiltersType();
        filter.getInclude().add(".*_EXPORT_.*");
        filter.getInclude().add(".*_EXPORT1024_.*");
        filter.getInclude().add(".*_WITH_DES_.*");
        filter.getInclude().add(".*_WITH_AES_.*");
        filter.getInclude().add(".*_WITH_NULL_.*");
        filter.getExclude().add(".*_DH_anon_.*");
        return filter;
    }

    private void invokeService(GreetingService service, HttpServletResponse response) throws IOException {
        String hello = service.hello(GreetingClientServlet.class.getSimpleName());
        String goodbye = service.goodbye(GreetingClientServlet.class.getSimpleName());
        System.out.println(hello);
        System.out.println(goodbye);
        response.getWriter().println(hello);
        response.getWriter().println(goodbye);
    }

}
