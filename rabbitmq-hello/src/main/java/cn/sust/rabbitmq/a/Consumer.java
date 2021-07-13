package cn.sust.rabbitmq.a;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者：接收消息
 */
public class Consumer {
    //队列名称
    public static final String QUEUE_NAME = "hello";
    //接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置工厂 ip 连接的rabbitmq队列
        factory.setHost("192.168.177.128");
        //设置用户名 密码
        factory.setUsername("admin");
        factory.setPassword("123");
        Connection connection = factory.newConnection();

        //创建信道对象
        Channel channel = connection.createChannel();

        //声明 接收消息
        DeliverCallback deliverCallback=(String var1, Delivery var2)->{
            System.out.println(new String(var2.getBody()));
        };
        //取消消息时的回调
        CancelCallback cancelCallback = var1 -> {
            System.out.println("消费消息中断");
        };

        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功后是否自动应答 true是自动 false是手动
         * 3.消费者未成功消费的回调
         * 4.消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
