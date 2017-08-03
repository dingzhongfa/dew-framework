package com.tairanchina.csp.dew.example.zipkin.config;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by 迹_Jason on 2017/8/1.
 */
public class ClientMultiThreadedExecution {

    private static PoolingClientConnectionManager conMgr = null;

    static {
        HttpParams params = new BasicHttpParams();
        Integer CONNECTION_TIMEOUT = 2 * 1000; //设置请求超时2秒钟 根据业务调整
        Integer SO_TIMEOUT = 2 * 1000; //设置等待数据超时时间2秒钟 根据业务调整
        Long CONN_MANAGER_TIMEOUT = 500L; //该值就是连接不够用的时候等待超时时间，一定要设置，而且不能太大

        params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
        params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
        params.setLongParameter(ClientPNames.CONN_MANAGER_TIMEOUT, CONN_MANAGER_TIMEOUT);
        params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);

        conMgr = new PoolingClientConnectionManager();
        conMgr.setMaxTotal(2000);

        conMgr.setDefaultMaxPerRoute(conMgr.getMaxTotal());
    }

    public static String get(String url, String param) {

        DefaultHttpClient httpClient = new DefaultHttpClient(conMgr);

//        httpClient.setParams(params);

        httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));

        HttpResponse httpResponse = null;

        // 发送get请求
        try {
            // 用get方法发送http请求
            HttpGet get = new HttpGet(url );
//            HttpGet get = new HttpGet(url + param);
//            get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//            get.setHeader("Accept-Encoding", "gzip, deflate, sdch");
//            get.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
//            get.setHeader("Cache-Control", "max-age=0");
//            get.setHeader("Connection", "keep-alive");
//            get.setHeader("Content-Type", "text/xml;charset=utf-8");
//            get.setHeader("Cookie", "BDUSS=EZZa1RmTVZWU0NqZ2VuM1RNdVhuYjR4QTkzbTNaMGRrNXladmFidFRwZHZmQ1pZQVFBQUFBJCQAAAAAAAAAAAEAAABNtEoNcWlhbmppY2hlbmdhYmMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAG~v~ldv7~5XO; BAIDUID=50970119FD0148A9DC0B168BC1DF574C:FG=1; PSTM=1476958371; BDRCVFR[nXyXJys849T]=mk3SLVN4HKm; BIDUPSID=D09F46D9DB5776482C08FA93075CE076; MCITY=-289%3A; BDRCVFR[feWj1Vr5u3D]=I67x6TjHwwYf0; PSINO=5; H_PS_PSSID=1429_19036_18240_17949_21109_17001_20593_21377_21189_21372");
//            get.setHeader("Host", "180.97.33.90");
//            get.setHeader("Upgrade-Insecure-Requests", "1");
            get.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/51.0.2704.79 Chrome/51.0.2704.79 Safari/537.36");
            System.out.println("执行get请求, uri: " + get.getURI());
            httpResponse = httpClient.execute(get);
            // response实体
            HttpEntity entity = httpResponse.getEntity();
            if (null != entity) {
                String response = EntityUtils.toString(entity);
                int statusCode = httpResponse.getStatusLine().getStatusCode();
//                System.out.println("响应状态码:" + statusCode);
//                System.out.println("响应内容:" + response);
                if (statusCode == HttpStatus.SC_OK) {
                    // 成功
                    return response;
                } else {
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("httpclient请求失败");
            return null;
        } finally {
            if (httpResponse != null) {
                try {
                    EntityUtils.consume(httpResponse.getEntity()); //会自动释放连接
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        get("http://192.168.111.224:8891/start",null);
    }
}
