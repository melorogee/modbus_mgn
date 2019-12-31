package com.xwtec.common.util;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * ES-SearchResponse @{#2018-10-15}
 * SearchResponse由执行搜索返回提供有关搜索执行本身以及访问返回文档的详细信息。
 * 
 * @author yanshuangbin
 * 
 */

public class ElasticSearchResponse {

	/**
	 * SearchResponse由执行搜索返回提供有关搜索执行本身以及访问返回文档的详细信息。
	 * 
	 * @param index
	 * @param types
	 * @param searchSourceBuilder
	 * @return String
	 * @throws Exception
	 */
	public static SearchResponse getSearchResponse(String index, String types, SearchSourceBuilder searchSourceBuilder)
			throws Exception {
		RestHighLevelClient client = null;
		SearchResponse searchResponse = null;
		try {
			client =  ElasticSearchUtil.getClient();
			searchResponse = client.search(getSearchRequest(index, types, searchSourceBuilder), RequestOptions.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return searchResponse;

	}

	/**
	 * 该SearchRequest用于具有与搜索文件，汇总，建议做，也要求提供高亮显示所产生的文件的方式中的任何操作。
	 * 
	 * @param index
	 * @param types
	 * @param searchSourceBuilder
	 * @return
	 */
	private static SearchRequest getSearchRequest(String index, String types, SearchSourceBuilder searchSourceBuilder) {
		SearchRequest searchRequest = new SearchRequest(index);
		searchRequest.types(types);
		searchRequest.source(searchSourceBuilder);
		return searchRequest;
	}


}
