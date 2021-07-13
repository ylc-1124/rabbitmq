package cn.sust.rabbitmq.h_deadMessage;

import cn.sust.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费者02 消费死信队列
 */
public class Consumer02 {
    public static final String DEAD_QUEUE = "dead_queue";

    //接收消息
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtil.getChannel();
        //接收到消息的回调
        DeliverCallback deliverCallback=(consumerTag,message)->{
            System.out.println("消费者02接收到消息:"+new String(message.getBody(),"UTF-8"));
        };
        //未接收的回调
        CancelCallback cancelCallback=consumerTag->{};
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, cancelCallback);
    }

}
