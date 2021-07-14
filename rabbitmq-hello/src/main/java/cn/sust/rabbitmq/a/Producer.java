package cn.sust.rabbitmq.a;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 原来只是一个普通生产者:发送消息
 * 在学习过程中修改成了测试优先级队列
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
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority", 10);  //官方允许0-255 此处设置为 10 不要设置过大 浪费cpu性能和内存
        channel.queueDeclare(QUEEN_NAME, true,
                false, false, arguments);
        for (int i = 1; i < 11; i++) {
            String message = "info" + i;  //发送的消息内容
            //发送到第 5条消息时 将其优先级设置为 5 其他消息正常发布 不加优先级
            if (i == 5) {
                AMQP.BasicProperties props = new AMQP.BasicProperties()
                        .builder().priority(5).build();
                channel.basicPublish("", QUEEN_NAME, props, message.getBytes());
            } else {
                channel.basicPublish("", QUEEN_NAME, null, message.getBytes());

            }
        }
        System.out.println("消息发送完毕");
        /**
         * 发送一个消息
         * 1.发送到哪个交换机
         * 2.路由的key值
         * 3.其他参数
         * 4.发送消息的消息体
         */

    }
}
