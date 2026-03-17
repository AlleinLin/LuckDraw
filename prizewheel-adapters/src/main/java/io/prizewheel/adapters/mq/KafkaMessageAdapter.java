package io.prizewheel.adapters.mq;

import com.alibaba.fastjson.JSON;
import io.prizewheel.core.port.output.MessageQueuePort;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * Kafka消息队列适配器
 * 
 * @author Allein
 * @since 1.0.0
 */
@Component
public class KafkaMessageAdapter implements MessageQueuePort {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageAdapter.class);

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void send(String topic, Object message) {
        String json = JSON.toJSONString(message);
        kafkaTemplate.send(topic, json);
        log.info("发送消息 topic:{} message:{}", topic, json);
    }

    @Override
    public void send(String topic, String key, Object message) {
        String json = JSON.toJSONString(message);
        kafkaTemplate.send(topic, key, json);
        log.info("发送消息 topic:{} key:{} message:{}", topic, key, json);
    }

    @Override
    public void sendDelayed(String topic, Object message, long delaySeconds) {
        String json = JSON.toJSONString(message);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, null, json);
        record.headers().add("delay-seconds", String.valueOf(delaySeconds).getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(record);
        log.info("发送延迟消息 topic:{} delay:{}s message:{}", topic, delaySeconds, json);
    }
}
