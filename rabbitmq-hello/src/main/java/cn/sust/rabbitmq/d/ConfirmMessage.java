package cn.sust.rabbitmq.d;

import cn.sust.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 发布确认模式
 * 比较三种模式的用时
 * 1.单个确认
 * 2.批量确认
 * 3.异步批量确认
 */
public class ConfirmMessage {
    //发送1000条消息
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
       //     publishMsgIndividual();  //发送1000个消息 单个确认耗时740ms
       //     publishMsgBatch();   //发送1000个消息 批量确认耗时123ms
        publishMsgAsync();     //发送1000个消息 异步确认耗时64ms
    }

    /**
     * 异步确认
     *
     * @throws Exception
     */
    public static void publishMsgAsync() throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        /**
         * 1.轻松将序号与消息进行关联
         * 2.轻松批量删除条目
         * 3.适用于高并发
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        /**
         * 准备消息监听器 监听那些消息成功了，哪些消息失败了
         */
        ConfirmCallback ackCallback=(deliveryTag, multiple)->{
            if (multiple) {
                //2.删除掉已经确认的消息，剩下的就是未确认的消息
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }

            System.out.println("确认的消息:"+deliveryTag);
        };
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            //3.打印未确认的消息有哪些
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息:"+message+"未确认的消息标记:" + deliveryTag);
        };

        channel.addConfirmListener(ackCallback,nackCallback);
        //开始时间
        long begin = System.currentTimeMillis();
        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
            //1.此处记录下所有要发送的消息
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发送"+MESSAGE_COUNT+"条消息,异步确认方式耗时"+(end-begin)+"ms");
    }

    /**
     * 单个确认
     * @throws Exception
     */
    public static void publishMsgIndividual() throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
            //单个消息马上进行消息确认
            channel.waitForConfirms();
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发送"+MESSAGE_COUNT+"条消息,单个确认方式耗时"+(end-begin)+"ms");
    }

    /**
     * 批量发布确认
     * @throws Exception
     */
    public static void publishMsgBatch() throws Exception {
        Channel channel = RabbitMqUtil.getChannel();
        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量发送消息
        int count = 100;
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
            //进行批量确认
            if (i%100==0) {
                channel.waitForConfirms();
            }
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发送"+MESSAGE_COUNT+"条消息,批量确认方式耗时"+(end-begin)+"ms");
    }
}
