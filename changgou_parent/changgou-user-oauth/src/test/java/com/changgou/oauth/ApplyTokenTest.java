package com.changgou.oauth;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;


@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplyTokenTest {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    //在注册中心中获取注册信息
    private LoadBalancerClient loadBalancerClient;

    @Test
    public void ApplyToken() {
        // 发送请求

        //构建地址 http://localhost:9200/oauth/token
        // serviced  Eureka 的实例名称
        ServiceInstance serviceInstance = loadBalancerClient.choose("user-auth");
        //http://localhost:9200
        URI uri = serviceInstance.getUri();
        // http://localhost:9200/oauth/token
        String url = uri + "/oauth/token";

        //封装请求参数 body headers
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", "itheima");
        body.add("password", "itheima");

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", this.getHttpBaics("changgou", "changgou"));
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        // 当后端出现401 400 直接返回前端
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });
                                                                 //请求方式         请求参数  返回值的类型
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
        Map map = exchange.getBody();
        System.out.println(map);


    }

    private String getHttpBaics(String clientId, String clientSecret) {
        String value = clientId + ":" + clientSecret;
        byte[] encode = Base64Utils.encode(value.getBytes());
        return "Basic " + new String(encode);
    }
}
