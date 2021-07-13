package cn.sust.rabbitmq.a;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者:发送消息
 */
public class Producer {
    //队列名称
    public static final String QUEEN_NAME = "hello";

    //发消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置工厂ip 连接的rabbitmq队列
        factory.setHost("192.168.177.128");
        //设置用户名 密码
        factory.setUsername("admin");
        factory.setPassword("123");

        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.是否持久化
         * 3.是否进行消息共享
         * 4.是否自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEEN_NAME, false, false, false, null);
        //发消息
        String message = "hello world";
        /**
         * 发送一个消息
         * 1.发送到哪个交换机
         * 2.路由的key值
         * 3.其他参数
         * 4.发送消息的消息体
         */
        channel.basicPublish("", QUEEN_NAME, null, message.getBytes());
        System.out.println("消息发送完毕");
    }
}
