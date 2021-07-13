package cn.sust.rabbitmq.h_deadMessage;

import cn.sust.rabbitmq.utils.RabbitMqUtil;
import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费者01
 * 1.声明普通交换机，普通队列并绑定
 * 2.声明死信交换机，死信队列并绑定
 */
public class Consumer01 {
    //定义名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";

    //接收消息
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtil.getChannel();
        //声明普通交换机和死信交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //声明普通队列及其参数
        Map<String, Object> arguments = new HashMap<>();
        //设置其死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //设置其死信routingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
        //设置普通队列的最大长度
//        arguments.put("x-max-length", 6);
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);

        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        //绑定普通交换机和普通队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");

        //绑定死信交换机和死信队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");
        System.out.println("消费者01等待接收消息");

        //接收到消息的回调
        DeliverCallback deliverCallback=(consumerTag,message)->{
            String msg = new String(message.getBody(), "UTF-8");
            if ("info5".equals(msg)) {
                System.out.println("消费者01接收到消息:" + msg + "==>此消息被拒绝了");
                //拒绝接收消息，不重新放回普通队列
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("消费者01接收到消息:" + msg);
                //正常应答,不批量应答
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }

        };
        //未接收的回调
        CancelCallback cancelCallback=consumerTag->{};

        //切换成手动应答
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, cancelCallback);
    }

}
