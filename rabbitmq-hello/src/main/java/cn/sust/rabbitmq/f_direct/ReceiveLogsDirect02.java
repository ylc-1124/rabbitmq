package cn.sust.rabbitmq.f_direct;

import cn.sust.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.*;

public class ReceiveLogsDirect02 {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明一个队列
        channel.queueDeclare("disk", false, false, false, null);
        //交换机和队列通过routingKey绑定
        channel.queueBind("disk", EXCHANGE_NAME, "error");
        //消费
        System.out.println("02等待接收消息");
        //接收到消息的回调
        DeliverCallback deliverCallback=(String consumerTag, Delivery message)->{
            System.out.println(new String(message.getBody()));
        };
        //未接收到的回调
        CancelCallback cancelCallback =(String consumerTag)->{

        };
        channel.basicConsume("disk", true, deliverCallback, cancelCallback);
    }
}
