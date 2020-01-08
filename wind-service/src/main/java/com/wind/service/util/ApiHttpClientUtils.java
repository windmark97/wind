package com.wind.service.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wind.dao.model.ApiCallParam;
import com.wind.manager.constant.AdvertApiConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * 搜索推广平台，发送http请求
 *
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/18 17:33
 **/
@Slf4j
public class ApiHttpClientUtils {
    private ApiHttpClientUtils() {

    }

    public static String sendHttpPost(ApiCallParam apiCallParam) throws Exception {
        CloseableHttpResponse response = doPost(apiCallParam);
        int status = response.getStatusLine().getStatusCode();
        log.info("advert http post :url:{},status:{}", apiCallParam.getUrl(), status);
        HttpEntity responseEntity = response.getEntity();
        return EntityUtils.toString(responseEntity, apiCallParam.getEncodeFormat());
    }

    /**
     * 下载文件
     *
     * @param apiCallParam
     * @return
     * @throws Exception
     */
    public static ByteArrayInputStream httpPostDownload(ApiCallParam apiCallParam) throws Exception {
        CloseableHttpResponse response = doPost(apiCallParam);
        return processCloseableHttpResponse(response);
    }

    /**
     * 下载文件
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static ByteArrayInputStream httpGetDownload(String url) throws Exception {
        CloseableHttpResponse response = doGet(url);
        return processCloseableHttpResponse(response);
    }

    /**
     * 返回的二进制流结果处理，
     * @param response
     * @return
     * @throws Exception
     */
    public static ByteArrayInputStream processCloseableHttpResponse(CloseableHttpResponse response) throws Exception {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            response.getEntity().writeTo(bos);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
            return inputStream;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * @param apiCallParam
     * @return
     * @throws Exception
     */
    private static CloseableHttpResponse doPost(ApiCallParam apiCallParam) throws Exception {
        HttpPost post = new HttpPost(apiCallParam.getUrl());
        String contentType = (String) apiCallParam.getHeaderMap().get("Content-Type");
        if (StringUtils.isEmpty(contentType)) {
            contentType = AdvertApiConsts.CONTENT_TYPE_JSON;
            apiCallParam.getHeaderMap().put("Content-Type", contentType);
        }
        buildHeader(post, apiCallParam.getHeaderMap());

        if (AdvertApiConsts.CONTENT_TYPE_FORM.equals(contentType)) {
            buildFormBody(post, apiCallParam.getDataMap(), apiCallParam.getEncodeFormat());
        } else {
            buildJsonBody(post, apiCallParam.getDataMap());
        }
        CloseableHttpClient client = HttpClientBuilder.create().build();
        return client.execute(post);
    }

    /**
     * 执行get请求
     *
     * @param url
     * @return
     * @throws Exception
     */
    private static CloseableHttpResponse doGet(String url) throws Exception {
        HttpGet get = new HttpGet(url);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        return client.execute(get);
    }

    /**
     * 设置请求的header
     *
     * @param post
     * @param headerMap
     */
    public static void buildHeader(HttpPost post, Map<String, Object> headerMap) {
        if (headerMap == null || headerMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
            post.setHeader(entry.getKey(), (String) entry.getValue());
        }
    }

    /**
     * form格式，设置请求的body
     *
     * @param post
     * @param dataMap
     * @param charset
     * @throws Exception
     */
    public static void buildFormBody(HttpPost post, Map<String, Object> dataMap, String charset) throws UnsupportedEncodingException {
        List<NameValuePair> paramList = Lists.newArrayList();
        dataMap.forEach((key, value) -> {
            NameValuePair pair = new BasicNameValuePair(key, String.valueOf(value));
            paramList.add(pair);
        });
        post.setEntity(new UrlEncodedFormEntity(paramList, charset));
    }

    /**
     * json格式，设置请求的body参数
     *
     * @param post
     * @param dataMap
     * @throws Exception
     */
    public static void buildJsonBody(HttpPost post, Map<String, Object> dataMap) {
        String content = JSON.toJSONString(dataMap);
        post.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));
    }


}
