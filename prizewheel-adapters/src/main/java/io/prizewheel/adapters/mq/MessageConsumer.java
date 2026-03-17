package io.prizewheel.adapters.mq;

import io.prizewheel.core.domain.entity.WinRecord;
import io.prizewheel.core.port.input.PointsServicePort;
import io.prizewheel.core.port.input.PrizeServicePort;
import io.prizewheel.core.port.output.WinRecordRepositoryPort;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * 消息消费者
 * 
 * @author Allein
 * @since 2.0.0
 */
@Component
public class MessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(MessageConsumer.class);

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
    public void consumePrizeGrant(ConsumerRecord<String, Object> record, Acknowledgment ack) {
        log.info("收到发奖消息 topic:{} key:{}", record.topic(), record.key());

        try {
            Object value = record.value();
            if (value instanceof WinRecord) {
                WinRecord winRecord = (WinRecord) value;
                processPrizeGrant(winRecord);
            }
            ack.acknowledge();
        } catch (Exception e) {
            log.error("发奖消息处理失败", e);
        }
    }

    @KafkaListener(topics = "activity-participate-topic", groupId = "prizewheel-participate-group")
    public void consumeActivityParticipate(ConsumerRecord<String, Object> record, Acknowledgment ack) {
        log.info("收到活动参与消息 topic:{} key:{}", record.topic(), record.key());

        try {
            Object value = record.value();
            if (value instanceof WinRecord) {
                WinRecord winRecord = (WinRecord) value;
                processActivityParticipate(winRecord);
            }
            ack.acknowledge();
        } catch (Exception e) {
            log.error("活动参与消息处理失败", e);
        }
    }

    @KafkaListener(topics = "points-reward-topic", groupId = "prizewheel-points-group")
    public void consumePointsReward(ConsumerRecord<String, Object> record, Acknowledgment ack) {
        log.info("收到积分奖励消息 topic:{} key:{}", record.topic(), record.key());

        try {
            Object value = record.value();
            if (value instanceof PointsRewardMessage) {
                PointsRewardMessage msg = (PointsRewardMessage) value;
                processPointsReward(msg);
            }
            ack.acknowledge();
        } catch (Exception e) {
            log.error("积分奖励消息处理失败", e);
        }
    }

    private void processPrizeGrant(WinRecord record) {
        log.info("处理奖品发放 recordId:{} participantId:{}", record.getRecordId(), record.getParticipantId());

        try {
            prizeService.grantPrize(record.getRecordId());
            log.info("奖品发放成功 recordId:{}", record.getRecordId());
        } catch (Exception e) {
            log.error("奖品发放失败 recordId:{}", record.getRecordId(), e);
            throw e;
        }
    }

    private void processActivityParticipate(WinRecord record) {
        log.info("处理活动参与记录 recordId:{} campaignId:{}", record.getRecordId(), record.getCampaignId());
        winRecordRepository.save(record);
        log.info("活动参与记录保存成功 recordId:{}", record.getRecordId());
    }

    private void processPointsReward(PointsRewardMessage msg) {
        log.info("处理积分奖励 userId:{} points:{}", msg.getUserId(), msg.getPoints());

        try {
            pointsService.addPoints(msg.getUserId(), msg.getPoints(), 
                    String.valueOf(2), msg.getRemark(), msg.getRelatedId());
            log.info("积分奖励发放成功 userId:{}", msg.getUserId());
        } catch (Exception e) {
            log.error("积分奖励发放失败 userId:{}", msg.getUserId(), e);
            throw e;
        }
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
