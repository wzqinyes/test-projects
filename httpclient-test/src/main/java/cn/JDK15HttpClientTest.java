package cn;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 旧项目（jdk1.5之前，需要用到httpclient-4.2版本）
 *
 * {@link HttpClient} 里边介绍了基本用法
 */
public class JDK15HttpClientTest {

    public static void main(String[] args){
        HttpClient httpClient = new DefaultHttpClient();

        BasicCookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("sessionid", "aaaaaaaaabbbbbbb");
        cookie.setVersion(0);
        cookie.setDomain("xxx");   //域名，可以是
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
        ((DefaultHttpClient)httpClient).setCookieStore(cookieStore);


        HttpPost httpPost = new HttpPost("xxxxxx");
        httpPost.setHeader("aaa", "bbb");

        try {
            HttpResponse response = httpClient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                String resp = EntityUtils.toString(entity, Consts.UTF_8);
                System.out.println(resp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        httpClient.getConnectionManager().shutdown();
    }
}
