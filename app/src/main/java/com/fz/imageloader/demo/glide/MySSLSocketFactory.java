package com.fz.imageloader.demo.glide;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * 解决android 5.0以下访问https，报
 * javax.net.ssl.SSLHandshakeException: javax.net.ssl.SSLProtocolException: SSL handshake aborted: ssl=0x824f95a8: Failure in SSL library, usually a protocol error
 * 异常的问题；
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2018/9/19 20:46
 */
public class MySSLSocketFactory extends SSLSocketFactory {
    private SSLSocketFactory defaultFactory;
    // Android 5.0+ (API level21) provides reasonable default settings
    // but it still allows SSLv3
    // https://developer.android.com/about/versions/android-5.0-changes.html#ssl
    static String[] protocols = null;
    static String[] cipherSuites = null;

    static {
        try {
            SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket();
            if (socket != null) {
                /* set reasonable protocol versions */
                // - enable all supported protocols (enables TLSv1.1 and TLSv1.2 on Android <5.0)
                // - remove all SSL versions (especially SSLv3) because they're insecure now
                List<String> protocols = new LinkedList<>();
                for (String protocol : socket.getSupportedProtocols()) {
                    if (!protocol.toUpperCase().contains("SSL")) {
                        protocols.add(protocol);
                    }
                }
                MySSLSocketFactory.protocols = protocols.toArray(new String[0]);
                /* set up reasonable cipher suites */
                // choose known secure cipher suites
                List<String> allowedCiphers = Arrays.asList(
                        // TLS 1.2
                        "TLS_RSA_WITH_AES_256_GCM_SHA384",
                        "TLS_RSA_WITH_AES_128_GCM_SHA256",
                        "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",
                        "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
                        "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",
                        "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
                        "TLS_ECHDE_RSA_WITH_AES_128_GCM_SHA256",
                        // maximum interoperability
                        "TLS_RSA_WITH_3DES_EDE_CBC_SHA",
                        "TLS_RSA_WITH_AES_128_CBC_SHA",
                        // additionally
                        "TLS_RSA_WITH_AES_256_CBC_SHA",
                        "TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA",
                        "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
                        "TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA",
                        "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA");
                List<String> availableCiphers = Arrays.asList(socket.getSupportedCipherSuites());
                // take all allowed ciphers that are available and put them into preferredCiphers
                HashSet<String> preferredCiphers = new HashSet<>(allowedCiphers);
                preferredCiphers.retainAll(availableCiphers);
                /* For maximum security, preferredCiphers should *replace* enabled ciphers (thus disabling
                 * ciphers which are enabled by default, but have become unsecure), but I guess for
                 * the security level of DAVdroid and maximum compatibility, disabling of insecure
                 * ciphers should be a server-side task */
                // add preferred ciphers to enabled ciphers
                HashSet<String> enabledCiphers = preferredCiphers;
                enabledCiphers.addAll(new HashSet<>(Arrays.asList(socket.getEnabledCipherSuites())));
                MySSLSocketFactory.cipherSuites = enabledCiphers.toArray(new String[0]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MySSLSocketFactory(X509TrustManager tm) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, (tm != null) ? new X509TrustManager[]{tm} : null, null);
            defaultFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new AssertionError(); // The system has no TLS. Just give up.
        }
    }

    private void upgradeTLS(SSLSocket ssl) {
        // Android 5.0+ (API level21) provides reasonable default settings
        // but it still allows SSLv3
        // https://developer.android.com/about/versions/android-5.0-changes.html#ssl
        if (protocols != null) {
            try {
                ssl.setEnabledProtocols(protocols);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cipherSuites != null) {
            try {
                //TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA is not supported.
                ssl.setEnabledCipherSuites(cipherSuites);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return cipherSuites;
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return cipherSuites;
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        Socket ssl = defaultFactory.createSocket(s, host, port, autoClose);
        if (ssl instanceof SSLSocket) {
            upgradeTLS((SSLSocket) ssl);
        }
        return ssl;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        Socket ssl = defaultFactory.createSocket(host, port);
        if (ssl instanceof SSLSocket) {
            upgradeTLS((SSLSocket) ssl);
        }
        return ssl;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        Socket ssl = defaultFactory.createSocket(host, port, localHost, localPort);
        if (ssl instanceof SSLSocket) {
            upgradeTLS((SSLSocket) ssl);
        }
        return ssl;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        Socket ssl = defaultFactory.createSocket(host, port);
        if (ssl instanceof SSLSocket) {
            upgradeTLS((SSLSocket) ssl);
        }
        return ssl;
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        Socket ssl = defaultFactory.createSocket(address, port, localAddress, localPort);
        if (ssl instanceof SSLSocket) {
            upgradeTLS((SSLSocket) ssl);
        }
        return ssl;
    }
}
