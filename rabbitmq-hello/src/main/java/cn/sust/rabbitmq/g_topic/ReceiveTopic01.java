package cn.sust.rabbitmq.g_topic;

import cn.sust.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.*;

import java.nio.charset.Charset;

/**
 * 消费者C1 声明交换机，队列并绑定，等待 接收消息
 */
public class ReceiveTopic01 {
    //交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    //接收消息
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtil.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明消息队列
        String queueName = "Q1";
        channel.queueDeclare(queueName, false, false, false, null);
        //绑定
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");
        System.out.println("消费者01等待接收消息");
        //接收消息
        //接收到消息的回调
        DeliverCallback deliverCallback=(String consumerTag, Delivery message)->{
            System.out.println(new String(message.getBody(), Charset.defaultCharset()));
            System.out.println("接收队列:"+queueName+"绑定键:"+message.getEnvelope().getRoutingKey());
        };
        //未接收到的回调
        CancelCallback cancelCallback =(String consumerTag)->{

        };
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }
}
