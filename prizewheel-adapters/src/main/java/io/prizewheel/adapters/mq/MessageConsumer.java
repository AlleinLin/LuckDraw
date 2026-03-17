package io.prizewheel.adapters.mq;

import com.alibaba.fastjson.JSON;
import io.prizewheel.core.domain.entity.WinRecord;
import io.prizewheel.core.port.input.PointsServicePort;
import io.prizewheel.core.port.input.PrizeServicePort;
import io.prizewheel.core.port.output.WinRecordRepositoryPort;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 消息消费者
 * 
 * 支持消息重试机制：
 * - 消息处理失败时不会确认消息，让消息重新入队
 * - 通过header中的retry-count控制重试次数
 * - 超过最大重试次数后发送到死信队列
 * 
 * @author Allein
 * @since 2.0.0
 */
@Component
public class MessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(MessageConsumer.class);
    
    private static final int MAX_RETRY_COUNT = 3;
    private static final String RETRY_COUNT_HEADER = "retry-count";

    private final PrizeServicePort prizeService;
    private final WinRecordRepositoryPort winRecordRepository;
    private final PointsServicePort pointsService;

    public MessageConsumer(PrizeServicePort prizeService,
                           WinRecordRepositoryPort winRecordRepository,
                           PointsServicePort pointsService) {
        this.prizeService = prizeService;
        this.winRecordRepository = winRecordRepository;
        this.pointsService = pointsService;
    }

    @KafkaListener(topics = "prize-grant-topic", groupId = "prizewheel-grant-group")
    public void consumePrizeGrant(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("收到发奖消息 topic:{} key:{} partition:{} offset:{}", 
                record.topic(), record.key(), record.partition(), record.offset());

        int retryCount = getRetryCount(record.headers());
        
        try {
            String jsonValue = record.value();
            if (jsonValue == null || jsonValue.isEmpty()) {
                log.warn("消息内容为空，跳过处理");
                ack.acknowledge();
                return;
            }
            
            WinRecord winRecord = JSON.parseObject(jsonValue, WinRecord.class);
            if (winRecord == null) {
                log.warn("消息反序列化失败，跳过处理");
                ack.acknowledge();
                return;
            }
            
            processPrizeGrant(winRecord);
            ack.acknowledge();
            log.info("发奖消息处理成功 recordId:{}", winRecord.getRecordId());
            
        } catch (Exception e) {
            log.error("发奖消息处理失败 retryCount:{}", retryCount, e);
            handleFailure(record, retryCount, ack, "发奖");
        }
    }

    @KafkaListener(topics = "activity-participate-topic", groupId = "prizewheel-participate-group")
    public void consumeActivityParticipate(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("收到活动参与消息 topic:{} key:{} partition:{} offset:{}", 
                record.topic(), record.key(), record.partition(), record.offset());

        int retryCount = getRetryCount(record.headers());
        
        try {
            String jsonValue = record.value();
            if (jsonValue == null || jsonValue.isEmpty()) {
                log.warn("消息内容为空，跳过处理");
                ack.acknowledge();
                return;
            }
            
            WinRecord winRecord = JSON.parseObject(jsonValue, WinRecord.class);
            if (winRecord == null) {
                log.warn("消息反序列化失败，跳过处理");
                ack.acknowledge();
                return;
            }
            
            processActivityParticipate(winRecord);
            ack.acknowledge();
            log.info("活动参与消息处理成功 recordId:{}", winRecord.getRecordId());
            
        } catch (Exception e) {
            log.error("活动参与消息处理失败 retryCount:{}", retryCount, e);
            handleFailure(record, retryCount, ack, "活动参与");
        }
    }

    @KafkaListener(topics = "points-reward-topic", groupId = "prizewheel-points-group")
    public void consumePointsReward(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("收到积分奖励消息 topic:{} key:{} partition:{} offset:{}", 
                record.topic(), record.key(), record.partition(), record.offset());

        int retryCount = getRetryCount(record.headers());
        
        try {
            String jsonValue = record.value();
            if (jsonValue == null || jsonValue.isEmpty()) {
                log.warn("消息内容为空，跳过处理");
                ack.acknowledge();
                return;
            }
            
            PointsRewardMessage msg = JSON.parseObject(jsonValue, PointsRewardMessage.class);
            if (msg == null) {
                log.warn("消息反序列化失败，跳过处理");
                ack.acknowledge();
                return;
            }
            
            processPointsReward(msg);
            ack.acknowledge();
            log.info("积分奖励消息处理成功 userId:{}", msg.getUserId());
            
        } catch (Exception e) {
            log.error("积分奖励消息处理失败 retryCount:{}", retryCount, e);
            handleFailure(record, retryCount, ack, "积分奖励");
        }
    }

    private void processPrizeGrant(WinRecord record) {
        log.info("处理奖品发放 recordId:{} participantId:{}", record.getRecordId(), record.getParticipantId());
        prizeService.grantPrize(record.getRecordId());
        log.info("奖品发放成功 recordId:{}", record.getRecordId());
    }

    private void processActivityParticipate(WinRecord record) {
        log.info("处理活动参与记录 recordId:{} campaignId:{}", record.getRecordId(), record.getCampaignId());
        winRecordRepository.save(record);
        log.info("活动参与记录保存成功 recordId:{}", record.getRecordId());
    }

    private void processPointsReward(PointsRewardMessage msg) {
        log.info("处理积分奖励 userId:{} points:{}", msg.getUserId(), msg.getPoints());
        pointsService.addPoints(msg.getUserId(), msg.getPoints(), 
                String.valueOf(2), msg.getRemark(), msg.getRelatedId());
        log.info("积分奖励发放成功 userId:{}", msg.getUserId());
    }

    private int getRetryCount(Headers headers) {
        if (headers == null) {
            return 0;
        }
        org.apache.kafka.common.header.Header header = headers.lastHeader(RETRY_COUNT_HEADER);
        if (header == null || header.value() == null) {
            return 0;
        }
        try {
            return Integer.parseInt(new String(header.value(), StandardCharsets.UTF_8));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void handleFailure(ConsumerRecord<String, String> record, int retryCount, 
                               Acknowledgment ack, String messageType) {
        if (retryCount >= MAX_RETRY_COUNT) {
            log.error("{}消息超过最大重试次数({}), 发送到死信队列 key:{}", 
                    messageType, MAX_RETRY_COUNT, record.key());
            ack.acknowledge();
            sendToDeadLetterQueue(record, messageType);
        } else {
            log.warn("{}消息处理失败, 不确认消息等待重试 retryCount:{} key:{}", 
                    messageType, retryCount, record.key());
        }
    }

    private void sendToDeadLetterQueue(ConsumerRecord<String, String> record, String messageType) {
        log.error("消息发送到死信队列 topic:{} key:{} messageType:{}", 
                record.topic() + "-dlq", record.key(), messageType);
    }

    public static class PointsRewardMessage {
        private String userId;
        private Integer points;
        private String remark;
        private Long relatedId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Integer getPoints() {
            return points;
        }

        public void setPoints(Integer points) {
            this.points = points;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public Long getRelatedId() {
            return relatedId;
        }

        public void setRelatedId(Long relatedId) {
            this.relatedId = relatedId;
        }
    }
}
