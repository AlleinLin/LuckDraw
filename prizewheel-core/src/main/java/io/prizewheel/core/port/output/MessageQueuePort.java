package io.prizewheel.core.port.output;

/**
 * 消息队列输出端口
 * 
 * @author Allein
 * @since 1.0.0
 */
public interface MessageQueuePort {

    void send(String topic, Object message);

    void send(String topic, String key, Object message);

    void sendDelayed(String topic, Object message, long delaySeconds);
}
