package cn;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;

/**
 * httpclient-4.3开始已经不使用{@link HttpClient}了，改为{@link CloseableHttpClient}
 * {@link CloseableHttpClient} 可以通过HttpClientBuilder.create().build()建造
 */
public class JDK18HttpClientTest {

    public static void main(String[] args) {
        CloseableHttpClient httpClient;

        BasicCookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("sessionid", "aaaaaaaaabbbbbbb");
        cookie.setVersion(0);
        cookie.setDomain("xxx");   //域名，可以是
        cookie.setPath("/");
        cookieStore.addCookie(cookie);

        /*builder有很多set，设置一些通用属性 */
        httpClient = HttpClientBuilder.create()
            .setDefaultHeaders(Collections.singleton(new BasicHeader("aaa", "aaaaaa")))
            .setDefaultCookieStore(cookieStore)
            .build();

        /* 设置超时时间、请求时间、socket时间都为5秒，允许重定向 */
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000)
            .setSocketTimeout(5000)
            .setRedirectsEnabled(true)
            .build();

        HttpPost httpPost = new HttpPost("xxxxxx");
        httpPost.setHeader("ccc", "ccccc");
        httpPost.setConfig(requestConfig);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                String resp = EntityUtils.toString(entity, Consts.UTF_8);
                System.out.println(resp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!httpPost.isAborted()) {
                httpPost.releaseConnection();
                httpPost.abort();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
