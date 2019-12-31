package com.xwtec.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhaolijun@upstudio.cn 这个类全是赵立军当年天津写的，我完全照搬，最下面的https的方法有缺陷，全部注释
 * @Package com.upstudio.http.client
 * @ClassName: HttpClientUtilNew
 * @Description: 新版HttpClient
 * @date 2016-11-30 上午9:24:49
 */
public class HttpClientUtilNew {
    private static RequestConfig requestConfig;
    private static StandardHttpRequestRetryHandler standardHandler;
    private static PoolingHttpClientConnectionManager cm;

    static {
        requestConfig = RequestConfig.custom().setSocketTimeout(5000)
                .setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                .build();
        standardHandler = new StandardHttpRequestRetryHandler(0, false);
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder
                .<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslsf).build();
        cm = new PoolingHttpClientConnectionManager(registry);
        // 将最大连接数增加到200
        cm.setMaxTotal(200);
        // 将每个路由基础的连接增加到200
        cm.setDefaultMaxPerRoute(200);
    }

    private static HttpClientUtilNew instance = null;

    public static HttpClientUtilNew getInstance() {
        if (instance == null) {
            instance = new HttpClientUtilNew();
        }
        return instance;
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     */
    public String sendHttpPost(String httpUrl) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param params  参数(格式:key1=value1&key2=value2)
     */
    public String sendHttpPost(String httpUrl, String params) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            // 设置参数
            StringEntity stringEntity = new StringEntity(params, "UTF-8");
            stringEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(stringEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps    参数
     */
    public String sendData4Post(String httpUrl, Map<String, String> maps) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (String key : maps.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求（带文件）
     *
     * @param httpUrl   地址
     * @param maps      参数
     * @param fileLists 附件
     */
//    public String sendHttpPost(String httpUrl, Map<String, String> maps,
//                               List<File> fileLists) {
//        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
//        MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
//        for (String key : maps.keySet()) {
//            meBuilder.addPart(key, new StringBody(maps.get(key),
//                    ContentType.TEXT_PLAIN));
//        }
//        for (File file : fileLists) {
//            FileBody fileBody = new FileBody(file);
//            meBuilder.addPart("files", fileBody);
//        }
//        HttpEntity reqEntity = meBuilder.build();
//        httpPost.setEntity(reqEntity);
//        return sendHttpPost(httpPost);
//    }

    /**
     * 发送Post请求
     *
     * @param httpPost
     * @return
     */
    private String sendHttpPost(HttpPost httpPost) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;

        try {
            // 创建默认的httpClient实例.
            // httpClient = HttpClients.createDefault();
            httpClient = HttpClients.custom().setConnectionManager(cm)
                    .setRetryHandler(standardHandler).build();
            httpPost.setConfig(requestConfig);

            // 执行请求
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    /**
     * 发送 get请求
     *
     * @param httpUrl
     */
    public String sendData4Get(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendHttpGet(httpGet);
    }

    /**
     * 发送 get请求Https
     *
     * @param httpUrl
     */
    public String sendData4GetHTTPS(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendHttpsGet(httpGet);
    }

    /**
     * 发送Get请求
     *
     * @return
     */
    private String sendHttpGet(HttpGet httpGet) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;

        try {
            // 创建默认的httpClient实例.
            // httpClient = HttpClients.createDefault();
            httpClient = HttpClients.custom().setConnectionManager(cm)
                    .setRetryHandler(standardHandler).build();
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    /**
     * 发送Get请求Https
     *
     * @return
     */
    private String sendHttpsGet(HttpGet httpGet) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader
                    .load(new URL(httpGet.getURI().toString()));
            DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(
                    publicSuffixMatcher);
            httpClient = HttpClients.custom().setConnectionManager(cm)
                    .setSSLHostnameVerifier(hostnameVerifier)
                    .setRetryHandler(standardHandler).build();
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    /***************************以下是发送HTPPS请求方法，此处是绕过签名证书的，只提供了JSON格式传输，后续再进行扩展***************************************/
    /**
     * @param httpUrl
     * @param jsonStr
     * @return
     * @Title: sendHttpsPost
     * @Description: TODO(发送https请求-JSON格式)
     * @author zhaolijun@upstudio.cn
     */
//    public String sendHttpsPostToJson(String httpUrl, String jsonStr, String charset) {
//        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
//        try {
//            // 设置参数
//            httpPost.addHeader("content-type", "application/json;charset=" + charset);
//            StringEntity stringEntity = new StringEntity(jsonStr.toString(), charset);
//            stringEntity.setContentType("application/json");
//            httpPost.setEntity(stringEntity);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return sendHttpsPost(httpPost, charset);
//    }

    /**
     * @param httpUrl
     * @param maps
     * @return
     * @Title: sendHttpsPostToMap
     * @Description: TODO(发送https请求-Map)
     * @author zhaolijun@upstudio.cn
     */
//    public String sendHttpsPostToMap(String httpUrl, Map<String, String> maps, String charset) {
//        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
//        // 创建参数队列
//        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//        for (String key : maps.keySet()) {
//            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
//        }
//        try {
//            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, charset));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return sendHttpsPost(httpPost, charset);
//    }

    /**
     * @param httpPost
     * @param charset
     * @return
     * @Title: sendHttpsPost
     * @Description: TODO(发送POST请求公用方法)
     * @author zhaolijun@upstudio.cn
     */
//    private String sendHttpsPost(HttpPost httpPost, String charset) {
//        CloseableHttpClient httpClient = null;
//        CloseableHttpResponse response = null;
//        HttpEntity entity = null;
//        String responseContent = null;
//
//        try {
//            //绕过证书验证
//            httpClient = new SSLClient();
//            httpPost.setConfig(requestConfig);
//            // 执行请求
//            response = httpClient.execute(httpPost);
//            entity = response.getEntity();
//            responseContent = EntityUtils.toString(entity, charset);
//            responseContent = responseContent.replaceAll("[\\t\\n\\r]", "");//将内容区域的回车换行去除
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                // 关闭连接,释放资源
//                if (response != null) {
//                    response.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return responseContent;
//    }
}