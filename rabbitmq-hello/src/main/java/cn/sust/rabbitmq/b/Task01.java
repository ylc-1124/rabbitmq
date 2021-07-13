package cn.sust.rabbitmq.b;

import cn.sust.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;

public class Task01 {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //发送消息
    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMqUtil.getChannel();
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.是否持久化
         * 3.是否进行消息共享
         * 4.是否自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //从控制台接收信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            /**
             * 发送一个消息
             * 1.发送到哪个交换机
             * 2.路由的key值
             * 3.其他参数
             * 4.发送消息的消息体
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("消息发送完毕"+message);
        }
    }
}
