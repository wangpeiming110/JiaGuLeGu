package com.zf.plugins.JiaGuLeGu

import org.gradle.api.GradleException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import groovy.lang.Closure;

public class Utils {

    public static String getMD5(File file) {

        FileInputStream fileInputStream = null;
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            BigInteger bigInt = new BigInteger(1, MD5.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static File downLoad(String proxyAddr, String url, File saveDir, Closure closure) {

        try {
            if (proxyAddr) {
                URL proxyAddrUrl = new URL(proxyAddr);

                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddrUrl.getAuthority(), proxyAddrUrl.getPort()));
                String protocol = proxyAddrUrl.getProtocol();
                if (protocol == null) {
                    throw new GradleException("Proxy address error。addr=${proxyAddr}")
                } else if ("http".equals(protocol.toLowerCase())) {
                    return downLoadHttpInternal(proxy, url, saveDir, closure)
                } else if ("https".equals(protocol.toLowerCase())) {
                    return downLoadHttpsInternal(proxy, url, saveDir, closure)
                } else {
                    throw new GradleException("Proxy address error。addr=${proxyAddr}")
                }
            } else {

                if (url.startsWith("http")) {
                    return downLoadHttpInternal(null, url, saveDir, closure)
                } else if (url.startsWith("https")) {
                    return downLoadHttpsInternal(null, url, saveDir, closure)
                }else{
                    throw new GradleException("uploadPath address error。uploadPath=${url}")
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;

    }

    public static File downLoadHttpsInternal(Proxy proxy, String url, File saveDir, Closure closure) {

        File apkFile = new File(saveDir, UUID.randomUUID().toString());

        InputStream stream = null;
        FileOutputStream fos = null;

        try {

            long startTime = System.currentTimeMillis();

            SSLContext sc = SSLContext.getInstance("SSL");


            TrustManager[] trustManagers = [new TrustAnyTrustManager()]

            sc.init(null, trustManagers, new java.security.SecureRandom());

            HttpsURLConnection urlConnection = null;
            if (proxy) {
                urlConnection = (HttpsURLConnection) new URL(url).openConnection(proxy);
            } else {
                urlConnection = (HttpsURLConnection) new URL(url).openConnection();
            }

            urlConnection.setSSLSocketFactory(sc.getSocketFactory());
            urlConnection.setHostnameVerifier(new TrustAnyHostnameVerifier());
            stream = urlConnection.getInputStream();

            fos = new FileOutputStream(apkFile);

            long total = 0;
            byte[] bt = new byte[1024 * 4];
            int length = 0;
            while ((length = stream.read(bt)) != -1) {
                fos.write(bt, 0, length);
                total += length;
                if (closure != null) {
                    if (System.currentTimeMillis() - startTime > 1000) {
                        closure(total);
                    }
                }
            }
            fos.close();
            stream.close();
            return apkFile;

        } catch (Exception ex) {
            if (apkFile.exists()) {
                apkFile.delete();
            }
            ex.printStackTrace();

        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception ex1) {
                ex1.printStackTrace();
            }

            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception ex1) {
                ex1.printStackTrace();
            }
        }

        return null;

    }

    public static File downLoadHttpInternal(Proxy proxy, String url, File saveDir, Closure closure) {

        File apkFile = new File(saveDir, UUID.randomUUID().toString());

        InputStream stream = null;
        FileOutputStream fos = null;

        try {

            long startTime = System.currentTimeMillis();

            HttpURLConnection connection = null;
            if (proxy) {
                connection = (HttpURLConnection) new URL(url).openConnection(proxy);
            } else {
                connection = (HttpURLConnection) new URL(url).openConnection();
            }
            stream = connection.getInputStream();

            fos = new FileOutputStream(apkFile);

            long total = 0;
            byte[] bt = new byte[1024 * 4];
            int length = 0;
            while ((length = stream.read(bt)) != -1) {
                fos.write(bt, 0, length);
                total += length;
                if (closure != null) {
                    if (System.currentTimeMillis() - startTime > 1000) {
                        closure(total);
                    }
                }
            }
            fos.close();
            stream.close();
            return apkFile;

        } catch (Exception ex) {
            if (apkFile.exists()) {
                apkFile.delete();
            }
            ex.printStackTrace()

        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception ex1) {
                ex1.printStackTrace();
            }

            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception ex1) {
                ex1.printStackTrace();
            }
        }

        return null;

    }


    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            X509Certificate[] x509Certificates = [];
            return x509Certificates;
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


}
