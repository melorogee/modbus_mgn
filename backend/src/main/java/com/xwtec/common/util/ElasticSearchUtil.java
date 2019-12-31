package com.xwtec.common.util;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wufanxin
 * @description es 工具类
 * @date 2019-7-3
 */
@Component
public class ElasticSearchUtil {


    @Value("${esurlAutowired}")
    private  String esurlAutowired;

    private static String esurl;

    @PostConstruct
    public void init() {
        esurl = this.esurlAutowired;
    }

    public static RestHighLevelClient getClient(){

        String[] esUrlArray = esurl.split(",");
        HttpHost[] httpArray = new HttpHost[esUrlArray.length];

        for(int i = 0 ; i<esUrlArray.length;i++){
            httpArray[i] = new HttpHost(esUrlArray[i],9200,"http");
        }

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(httpArray));
        return client;
    }
    /**
     * 索引数据
     * @param index 索引
     * @param type  类型
     * @param id 文档的id
     * @param json 数据格式
     * @return
     * @throws Exception
     */
    public static IndexResponse index(String index, String type, String id, Map<String,Object> json) throws Exception{
        RestHighLevelClient restClient = getClient();
        IndexResponse  indexResponse = null;
        try {
            IndexRequest indexRequest = new IndexRequest(index, type, id);
            indexRequest.source(json);
            indexResponse = restClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            restClient.close();
        }
        return indexResponse;
    }


    /**
     * 根据id get数据
     * @param index 索引
     * @param type  类型
     * @param id 文档的id
     * @return
     * @throws Exception
     */
    public static Map<String,Object> get(String index, String type, String id) throws Exception{
        RestHighLevelClient restClient = getClient();
        GetRequest request = null;
        Map<String,Object> retMap = new HashMap<>();
        try {
             request = new GetRequest(index, type, id);
             GetResponse resp = restClient.get(request);
             retMap = resp.getSourceAsMap();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            restClient.close();
        }
        return retMap;
    }
}
