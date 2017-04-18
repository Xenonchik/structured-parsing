package brws;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Blahblahblah
 */
public class HttpBrowser implements Browser {

  final static Logger log = LoggerFactory.getLogger(HttpBrowser.class);

  private CloseableHttpClient httpclient;
  private String userAgent = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:45.0) Gecko/20100101 Firefox/45.0";
  private String proxyHost = "31.131.23.37";
  private Integer proxyPort = 3128;

  public HttpBrowser() {
    httpclient = (CloseableHttpClient) createHttpClient();
  }

  @Override
  public String browse(String url) {
    String content = "";

    HttpGet httpGet = null;
    try {
      httpGet = buildHttpGet(url);
    } catch (MalformedURLException | URISyntaxException e) {
      log.error(url);
      e.printStackTrace();
    }

    ResponseHandler<String> responseHandler = new BasicResponseHandler();

    try {
      content = httpclient.execute(httpGet, responseHandler);
    } catch (IOException e) {
      log.error(url);
      e.printStackTrace();
    }

    return content;
  }

  private HttpGet buildHttpGet(String url) throws MalformedURLException, URISyntaxException {
    URL encodedUrl = new URL(url);
    String nullFragment = null;
    URI uri = new URI(encodedUrl.getProtocol(), encodedUrl.getHost(), encodedUrl.getPath(), encodedUrl.getQuery(), nullFragment);
    HttpGet httpGet = new HttpGet(uri);

    httpGet.addHeader("User-Agent", this.userAgent);
    httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    return httpGet;
  }

  private HttpClient createHttpClient() {
    HttpClientBuilder b = HttpClientBuilder.create();

    b = addSSL(b);
    b = addProxy(b);

    HttpClient client = b.build();
    return client;
  }

  /**
   * Add some magic ssl for HttpClient
   * @param builder
   * @return
   */
  private HttpClientBuilder addSSL(HttpClientBuilder builder) {
    try {
      SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (arg0, arg1) -> true).build();

    builder.setSslcontext( sslContext);
    SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext,
        SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
        .register("http", PlainConnectionSocketFactory.getSocketFactory())
        .register("https", sslSocketFactory)
        .build();
    PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager( socketFactoryRegistry);
    builder.setConnectionManager(connMgr);
    } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
      e.printStackTrace();
    }
    return builder;
  }

  /**
   * Add proxy way to HttpClient
   * @param builder
   * @return
   */
  private HttpClientBuilder addProxy(HttpClientBuilder builder) {
    HttpHost proxy = new HttpHost(proxyHost, proxyPort);
    DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
    builder.setRoutePlanner(routePlanner);
    return builder;
  }
}
