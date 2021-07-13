package cn.sust.rabbitmq.e_fanout;

import cn.sust.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * 消息接收
 */
public class ReceiveLogs02 {
    public static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtil.getChannel();
        //生成一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        /**
         * 生成一个临时队列
         * 当消费者断开连接，自动删除队列
         */
        String queueName = channel.queueDeclare().getQueue();
        //把该临时队列绑定交换机，并且设置routingKey为空字符串
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("ReceiveLogs02等待接收消息......");
        //接收到消息的回调
        DeliverCallback deliverCallback=(String consumerTag, Delivery message)->{
            System.out.println(new String(message.getBody()));
        };
        //未接收到的回调
        CancelCallback cancelCallback =(String consumerTag)->{

        };
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }
}
