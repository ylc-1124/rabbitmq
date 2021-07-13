package cn.sust.rabbitmq.h_deadMessage;

import cn.sust.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

/**
 * 生产者
 * 1.发送十条消息 设置TTL为10s
 */
public class Producer {
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtil.getChannel();
        //设置TTL消息过期时间为10000 ms  = 10s
        AMQP.BasicProperties props = new AMQP.BasicProperties()
                .builder().expiration("10000")
                .build();

        //发送10条消息
        for (int i = 1; i < 11; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",props,message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
