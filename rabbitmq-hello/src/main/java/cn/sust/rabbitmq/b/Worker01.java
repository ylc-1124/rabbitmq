package cn.sust.rabbitmq.b;

import cn.sust.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;

public class Worker01 {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //接收消息
    public static void main(String[] args) throws IOException {
        //获取信道
        Channel channel = RabbitMqUtil.getChannel();
        //消息的接收
        DeliverCallback deliverCallback=(String var1, Delivery var2)->{
            System.out.println("接收到的消息:"+new String(var2.getBody()));
        };
        //消息接收被取消时执行下面方法
        CancelCallback cancelCallback=var1->{
            System.out.println("消费者取消消费的逻辑回调");
        };
        System.out.println("C2等待接收消息......");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
