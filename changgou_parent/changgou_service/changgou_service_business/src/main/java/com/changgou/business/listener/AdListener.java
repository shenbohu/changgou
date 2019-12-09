package com.changgou.business.listener;

import com.itheima.canal.config.RabbitMQConfig;
import okhttp3.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AdListener {
    @RabbitListener(queues = RabbitMQConfig.AD_UPDATE_QUEUE)
    public void myListener1(String message) {
        System.out.println("消费者接收到的消息为：" + message);

        // 发起远程调用
        OkHttpClient okHttpClient = new OkHttpClient();
        // 设置新的访问
        String url = "http://192.168.200.128/ad_update?position=" + message;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功
                System.out.println("qingqiu chenggong"+response.message());
            }
        });
    }
}
