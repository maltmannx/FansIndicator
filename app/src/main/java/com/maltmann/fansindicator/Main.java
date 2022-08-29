package com.maltmann.fansindicator;

import com.alibaba.fastjson2.JSON;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;

/**
 * @Author AirConditoner
 * @Date 2022/8/29 15:40
 **/
public class Main {
    //https://api.bilibili.com/x/relation/stat?vmid=38485464&jsonp=jsonp

    public static String sendGet(String urlParam) throws IOException {
        // 创建httpClient实例对象
        HttpClient httpClient = new HttpClient();
        // 设置httpClient连接主机服务器超时时间：15000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        // 创建GET请求方法实例对象
        GetMethod getMethod = new GetMethod(urlParam);
        // 设置post请求超时时间
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
        getMethod.addRequestHeader("Content-Type", "application/json");

        httpClient.executeMethod(getMethod);

        String result = getMethod.getResponseBodyAsString();
        getMethod.releaseConnection();
        return result;
    }

    public static void main(String[] args) throws IOException {
        String num = JSON.parseObject(sendGet("https://api.bilibili.com/x/relation/stat?vmid=4172216&jsonp=jsonp")).getJSONObject("data").getString("follower");    // 粉丝数
        System.out.println(num);
    }
}

