package cn.sust.rabbitmq.c;

import cn.sust.rabbitmq.utils.RabbitMqUtil;
import cn.sust.rabbitmq.utils.SleepUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;

/**
 * 工作线程 03 处理消息速度快
 */
public class Worker04 {
    public static final String TASK_QUEUE_NAME = "ack_queue";
    //接收消息
    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMqUtil.getChannel();
        System.out.println("消费者04 处理速度慢");
        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功后是否自动应答 true是自动 false是手动
         * 3.消费者未成功消费的回调
         * 4.消费者取消消费的回调
         */
        DeliverCallback deliverCallback=(String consumerTag, Delivery message)->{
            SleepUtil.sleep(30);
            System.out.println("接收到的消息:" + new String(message.getBody(), "UTF-8"));
            //手动应答
            /**
             * 1.消息的标记 tag
             * 2.是否批量应答
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        CancelCallback cancelCallback=(String var1)->{
            System.out.println("消费者取消消费回调的函数");
        };
        //设置不公平分发（不设置则默认为轮询）
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);
        //采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
