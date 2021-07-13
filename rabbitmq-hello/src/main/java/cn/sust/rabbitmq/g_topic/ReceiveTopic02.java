package cn.sust.rabbitmq.g_topic;

import cn.sust.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.*;

import java.nio.charset.Charset;

/**
 * 消费者C2
 */
public class ReceiveTopic02 {
    //交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    //接收消息
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtil.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明消息队列
        String queueName = "Q2";
        channel.queueDeclare(queueName, false, false, false, null);
        //绑定
        channel.queueBind(queueName, EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");
        System.out.println("消费者02等待接收消息");
        //接收消息
        //接收到消息的回调
        DeliverCallback deliverCallback=(consumerTag,message)->{
            System.out.println(new String(message.getBody(), Charset.defaultCharset()));
            System.out.println("接收队列:"+queueName+"绑定键:"+message.getEnvelope().getRoutingKey());
        };
        //未接收到的回调
        CancelCallback cancelCallback =(consumerTag)->{};
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }
}
