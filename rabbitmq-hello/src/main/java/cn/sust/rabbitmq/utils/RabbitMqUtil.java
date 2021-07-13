package cn.sust.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 连接工厂创建信道的工具类
 */
public class RabbitMqUtil {
    public static Channel getChannel() {
        Channel channel = null;
        try {
            //创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            //设置工厂 ip 连接的rabbitmq队列
            factory.setHost("192.168.177.128");
            //设置用户名 密码
            factory.setUsername("admin");
            factory.setPassword("123");
            Connection connection = factory.newConnection();

            //创建信道对象
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return channel;
    }
}
