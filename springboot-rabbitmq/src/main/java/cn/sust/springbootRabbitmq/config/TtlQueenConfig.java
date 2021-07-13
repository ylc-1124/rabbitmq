package cn.sust.springbootRabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟队列的配置类
 */
@Configuration
public class TtlQueenConfig {
    /**
     * 步骤一：先定义交换机和队列的名称
     */
    //普通交换机
    public static final String X_EXCHANGE = "X";
    //死信交换机
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    //普通队列
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    //通用延迟队列
    public static final String QUEUE_C = "QC";
    //死信队列
    public static final String DEAD_LETTER_QUEUE = "QD";

    /**
     * 步骤二：声明各个交换机和队列
     * @return
     */
    //声明普通交换机 xExchange
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    //声明死信交换机 yExchange
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    //声明普通队列A  设置 TTL为10s
    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> arguments = new HashMap<>();
        //1.设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //2.设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        //3.设置TTL  单位 ms
        arguments.put("x-message-ttl", 10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    //声明普通队列 B  设置 TTL为 40s
    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> arguments = new HashMap<>();
        //1.设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //2.设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        //3.设置TTL  单位 ms
        arguments.put("x-message-ttl", 40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }
    //声明 QC
    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> arguments = new HashMap<>();
        //1.设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //2.设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }

    //声明死信队列
    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    /**
     * 步骤三：绑定交换机和队列
     */
    //绑定队列 QA和普通交换机 xExchange
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    //绑定队列 QB和普通交换机 xExchange
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }
    //绑定队列 QC和普通交换机 xExchange
    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }

    //绑定死信队列 QD和死信交换机 yExchange
    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }
}
