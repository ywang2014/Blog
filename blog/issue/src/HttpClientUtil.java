package com.common.tool.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 支持GET 方法传递body的httpClient
 */
public class HttpClientUtil {
    private static final AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient();

    /**
     * GET 请求
     *
     * @param url 请求url
     * @param json 请求body
     * @param headers 请求headers
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> get(String url, String json, Map<String, String> headers) throws Exception {
        // 构建请求
        BoundRequestBuilder requestBuilder = asyncHttpClient.prepareGet(url).setBody(json);
        headers.forEach(requestBuilder::addHeader);

        List<Response> list = new ArrayList<>();
        requestBuilder.execute()
                .toCompletableFuture()
                .thenAccept(list::add)
                .join();
        if (list.isEmpty()) {
            return null;
        }
        Response response = list.get(0);
        if (response.getStatusCode() != 200) {
            return null;
        }

        return new ObjectMapper().readValue(response.getResponseBody(), Map.class);
    }

    /**
     * GET 请求
     *
     * @param url 请求url
     * @param headers 请求headers
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> get(String url, Map<String, String> headers) throws Exception {
        // 构建请求
        BoundRequestBuilder requestBuilder = asyncHttpClient.prepareGet(url);
        headers.forEach(requestBuilder::addHeader);

        List<Response> list = new ArrayList<>();
        requestBuilder.execute()
                .toCompletableFuture()
                .thenAccept(list::add)
                .join();
        if (list.isEmpty()) {
            return null;
        }
        Response response = list.get(0);
        if (response.getStatusCode() != 200) {
            return null;
        }

        return new ObjectMapper().readValue(response.getResponseBody(), Map.class);
    }

    /**
     * GET 请求
     *
     * @param url 请求url
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> get(String url) throws Exception {
        // 构建请求
        BoundRequestBuilder requestBuilder = asyncHttpClient.prepareGet(url);

        List<Response> list = new ArrayList<>();
        requestBuilder.execute()
                .toCompletableFuture()
                .thenAccept(list::add)
                .join();
        if (list.isEmpty()) {
            return null;
        }
        Response response = list.get(0);
        if (response.getStatusCode() != 200) {
            return null;
        }

        return new ObjectMapper().readValue(response.getResponseBody(), Map.class);
    }
}
