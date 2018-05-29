package com.dhb.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HttpClientUtil {

    private static final Log log = LogFactory.getLog(HttpClientUtil.class);

    private static String DEFAULT_CHARSET = "UTF-8";

    private static int DEFAULT_PORT = 80;

    /**
     * http get请求
     *
     * @param url 请求的地址
     * @return 响应结果(字符串)
     */
    public static String get(String url) {
        return get(url, DEFAULT_CHARSET);
    }

    /**
     * http get请求
     *
     * @param url     请求的地址
     * @param charSet 字符串编码格式
     * @return 响应结果(字符串)
     */
    public static String get(String url, String charSet) {
        String responseStr = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpGet httpGet = new HttpGet(url);
            // Header header = new BasicHeader("Host", "www.sztx.com");
            // httpGet.setHeader(header);
            CloseableHttpResponse response = httpclient.execute(httpGet);

            try {
                if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                    HttpEntity entity = response.getEntity();

                    if (entity != null) {
                        responseStr = EntityUtils.toString(entity, charSet);
                    }
                } else {
                    log.info("状态码：" + response.getStatusLine().getStatusCode() + "，原因：" + response.getStatusLine().getReasonPhrase());
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            log.error("Http get request error", e);
        } catch (IOException e) {
            log.error("Http get request error", e);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                log.error("Httpclient close error", e);
            }
        }

        return responseStr;
    }

    /**
     * http提交post请求
     *
     * @param url         请求的地址
     * @param paramterMap 请求的参数集合
     * @return 响应结果(字符串)
     * @throws Exception
     */
    public static String post(String url, Map<String, String> paramterMap) throws Exception {
        return post(url, paramterMap, DEFAULT_CHARSET);
    }

    /**
     * http提交post请求
     *
     * @param url         请求的地址
     * @param paramterMap 请求的参数集合
     * @param charSet     字符集
     * @return 响应结果(字符串)
     * @throws Exception
     */
    public static String post(String url, Map<String, String> paramterMap, String charSet) throws Exception {
        String responseStr = null;

        //设置http的状态参数
		/*RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();*/

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        //httpPost.setConfig(requestConfig);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Set<Entry<String, String>> set = paramterMap.entrySet();
        //System.out.println(paramterMap);

        for (Entry<String, String> entry : set) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        try {
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(params, charSet);
            httpPost.setEntity(uefEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);

            try {
                if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                    HttpEntity entity = response.getEntity();

                    if (entity != null) {
                        responseStr = EntityUtils.toString(entity, charSet);
                    }
                } else {
                    log.info("状态码：" + response.getStatusLine().getStatusCode() + "，原因：" + response.getStatusLine().getReasonPhrase());
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            log.error("Http post request error", e);
        } catch (UnsupportedEncodingException e) {
            log.error("Http post request error", e);
        } catch (IOException e) {
            log.error("Http post request error", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("Httpclient close error", e);
            }
        }

        return responseStr;
    }

    /**
     * http提交purge请求
     *
     * @param varnishIp
     * @param url
     * @return
     */
    public static String purge(String varnishIp, String url) {
        String responseStr = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpPurge httpPurge = new HttpPurge(url);
            RequestConfig requestConfig = RequestConfig.custom().setProxy(new HttpHost(varnishIp, DEFAULT_PORT)).build();
            httpPurge.setConfig(requestConfig);
            CloseableHttpResponse response = httpclient.execute(httpPurge);

            try {
                if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                    HttpEntity entity = response.getEntity();

                    if (entity != null) {
                        responseStr = EntityUtils.toString(entity, DEFAULT_CHARSET);
                    }
                } else {
                    log.info("状态码：" + response.getStatusLine().getStatusCode() + "，原因：" + response.getStatusLine().getReasonPhrase());
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            log.error("Http purge request error", e);
        } catch (IOException e) {
            log.error("Http purge request error", e);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                log.error("Httpclient close error", e);
            }
        }

        return responseStr;
    }

    /**
     * HttpClient连接SSL
     *
     * @param url
     * @param keyStorePath
     * @param keyStorePassword
     * @return
     */
    public static String sslHttpGet(String url, String keyStorePath, String keyStorePassword) {
        String responseStr = null;
        CloseableHttpClient httpclient = null;
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            FileInputStream instream = new FileInputStream(new File(keyStorePath));
            try {
                // 加载keyStore d:\\tomcat.keystore
                trustStore.load(instream, keyStorePassword.toCharArray());
            } catch (CertificateException e) {
                e.printStackTrace();
            } finally {
                try {
                    instream.close();
                } catch (Exception ignore) {
                }
            }
            // 相信自己的CA和所有自签名的证书
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();

            // 只允许使用TLSv1协议
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

            // 创建http请求(get方式)
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        responseStr = EntityUtils.toString(entity);
                        //EntityUtils.consume(entity);
                    }
                } else {
                    log.info("状态码：" + response.getStatusLine().getStatusCode() + "，原因：" + response.getStatusLine().getReasonPhrase());
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } finally {
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return responseStr;
    }

    /**
     * 获取忽略SSL证书验证的HttpClient
     *
     * @return
     * @throws Exception
     */
    private static CloseableHttpClient getIgnoeSSLClient() throws Exception {
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        }).build();

        //创建httpClient
        CloseableHttpClient client = HttpClients.custom().setSslcontext(sslContext).
                setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
        return client;
    }

    /**
     * 获取忽略SSL证书验证的HttpClient
     *
     * @return
     * @throws Exception
     */
    private static CloseableHttpClient getIgnoeSSLClient1() throws Exception {
        //采用绕过验证的方式处理https请求
        SSLContext sslcontext = createIgnoreVerifySSL();

        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);

        //创建自定义的httpclient对象
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();
        return client;
    }

    /**
     * 绕过验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    static class HttpPurge extends HttpRequestBase {
        public final static String METHOD_NAME = "PURGE";

        public HttpPurge() {
            super();
        }

        public HttpPurge(final URI uri) {
            super();
            setURI(uri);
        }

        public HttpPurge(final String uri) {
            super();
            setURI(URI.create(uri));
        }

        @Override
        public String getMethod() {
            return METHOD_NAME;
        }
    }

}
