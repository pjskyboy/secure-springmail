package com.freesundance.mail;

import org.springframework.core.io.DefaultResourceLoader;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;


public class AuthSSLSocketFactory {

    public static final String DEFAULT_TRUSTSTORE_PASSWORD = "notasecret";
    public static final String DEFAULT_SECURE_SOCKET_PROTOCOL = "TLS";
    public static final String DEFAULT_TRUSTSTORE_TYPE = "jks";
    public static final String DEFAULT_TRUSTSTORE_ALGORITHM = "SunX509";

    /**
     * Loads and configures an Socket Factory using the desired secure protocol and authenticating the
     * remote socket against a given trust store's certificate(s) typically the remote server's Root CA.
     * <br/>
     * <br/>
     * Intended to be used as a factory in a Spring configuration to support secure SpringMail.
     * <br/>
     * <br/>
     * The returned {@link javax.net.ssl.SSLSocketFactory} can be used a value-ref in the javamail property:
     * <a href="https://javamail.java.net/nonav/docs/api/com/sun/mail/smtp/package-summary.html">mail.smtp.ssl.socketFactory</a>
     * <br/>
     * <br/>
     * @param secureSocketProtocol the protocol e.g. TLS which is the default if passed null
     * @param truststoreResourceName the name of the trust store resource using Spring resource naming e.g. classpath:mytrust.jks
     * @param truststorePassword password to gain entry to the trust store, unlikely to be secret but the trust store should be secure, if passed null the password assumed is notasecret
     * @param truststoreType type of store e.g. jks which is the default  if passed null
     * @param truststoreAlgorithm algorithm to use e.g. SunX509 which is the default if passed null
     * @return {@link javax.net.ssl.SSLSocketFactory}
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws CertificateException
     * @throws KeyStoreException
     * @throws IOException
     */
    public static SSLSocketFactory getAuthSocketFactory(String secureSocketProtocol,
                                                        String truststoreResourceName,
                                                        String truststorePassword,
                                                        String truststoreType,
                                                        String truststoreAlgorithm) throws NoSuchAlgorithmException, KeyManagementException, CertificateException, KeyStoreException, IOException {
        secureSocketProtocol = (null == secureSocketProtocol ? DEFAULT_SECURE_SOCKET_PROTOCOL : secureSocketProtocol);
        truststorePassword = (null == truststorePassword ? DEFAULT_TRUSTSTORE_ALGORITHM : truststorePassword);
        truststoreType = (null == truststoreType ? DEFAULT_TRUSTSTORE_TYPE : truststoreType);
        truststoreAlgorithm = (null == truststoreAlgorithm ? DEFAULT_TRUSTSTORE_ALGORITHM : truststoreAlgorithm);

        SSLContext sslContext = SSLContext.getInstance(secureSocketProtocol);
        sslContext.init(null, assembleTrustManagers(truststoreResourceName, truststorePassword, truststoreType, truststoreAlgorithm), new SecureRandom());
        return sslContext.getSocketFactory();
    }

    private static TrustManager[] assembleTrustManagers(String truststoreResourceName,
                                                        String truststorePassword,
                                                        String truststoreType,
                                                        String truststoreAlgorithm) throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException {
        // load a truststore using Spring's resource loader
        KeyStore keyStore = KeyStore.getInstance(truststoreType);
        keyStore.load(new DefaultResourceLoader().getResource(truststoreResourceName).getInputStream(), truststorePassword.toCharArray());
        // now create a trust manager factory initialised with the trust store we just loaded
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(truststoreAlgorithm);
        tmf.init(keyStore);
        return tmf.getTrustManagers();
    }
}
