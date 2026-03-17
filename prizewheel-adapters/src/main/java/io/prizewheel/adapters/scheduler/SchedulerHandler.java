package io.prizewheel.adapters.scheduler;

import io.prizewheel.core.domain.entity.Campaign;
import io.prizewheel.core.domain.entity.WinRecord;
import io.prizewheel.core.port.output.CampaignRepositoryPort;
import io.prizewheel.core.port.output.MessageQueuePort;
import io.prizewheel.core.port.output.WinRecordRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务处理器
 * 
 * 定时任务列表：
 * - 活动状态流转：每分钟执行一次
 * - 消息补偿：每5分钟执行一次
 * - 过期活动清理：每天凌晨2点执行
 * - 库存同步：每10分钟执行一次
 * 
 * @author Allein
 * @since 2.0.0
 */
@Component
public class SchedulerHandler {

    private static final Logger log = LoggerFactory.getLogger(SchedulerHandler.class);

    private final CampaignRepositoryPort campaignRepository;
    private final WinRecordRepositoryPort winRecordRepository;
    private final MessageQueuePort messageQueue;

    public SchedulerHandler(CampaignRepositoryPort campaignRepository,
                           WinRecordRepositoryPort winRecordRepository,
                           MessageQueuePort messageQueue) {
        this.campaignRepository = campaignRepository;
        this.winRecordRepository = winRecordRepository;
        this.messageQueue = messageQueue;
    }

    @Scheduled(cron = "0 * * * * ?")
    public void processCampaignStateTransition() {
        log.info("开始执行活动状态流转任务");

        List<Campaign> campaigns = campaignRepository.findActiveCampaigns();
        LocalDateTime now = LocalDateTime.now();
        int transitionCount = 0;

        for (Campaign campaign : campaigns) {
            try {
                int currentStatus = campaign.getStatus();
                int newStatus = calculateNewStatus(campaign, now);
                
                if (newStatus != currentStatus) {
                    campaignRepository.updateStatus(campaign.getCampaignId(), newStatus);
                    log.info("活动状态流转 campaignId:{} {} -> {}", 
                            campaign.getCampaignId(), getStatusName(currentStatus), getStatusName(newStatus));
                    transitionCount++;
                }
            } catch (Exception e) {
                log.error("活动状态流转异常 campaignId:{}", campaign.getCampaignId(), e);
            }
        }

        log.info("活动状态流转任务执行完成 处理数量:{} 流转数量:{}", campaigns.size(), transitionCount);
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void processMessageCompensation() {
        log.info("开始执行消息补偿任务");

        List<WinRecord> failedRecords = winRecordRepository.findPendingRecords();
        int successCount = 0;
        int failCount = 0;

        for (WinRecord record : failedRecords) {
            try {
                if (record.getStatus() == 1) {
                    messageQueue.send("prize-grant-topic", record);
                    log.info("补偿发送发奖消息 recordId:{}", record.getRecordId());
                    successCount++;
                }
            } catch (Exception e) {
                log.error("消息补偿发送失败 recordId:{}", record.getRecordId(), e);
                failCount++;
            }
        }

        log.info("消息补偿任务执行完成 总数:{} 成功:{} 失败:{}", failedRecords.size(), successCount, failCount);
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void processExpiredCampaigns() {
        log.info("开始执行过期活动清理任务");

        List<Campaign> expiredCampaigns = campaignRepository.findExpiredCampaigns();
        int processedCount = 0;

        for (Campaign campaign : expiredCampaigns) {
            try {
                campaignRepository.updateStatus(campaign.getCampaignId(), 8);
                log.info("过期活动归档 campaignId:{}", campaign.getCampaignId());
                processedCount++;
            } catch (Exception e) {
                log.error("过期活动归档失败 campaignId:{}", campaign.getCampaignId(), e);
            }
        }

        log.info("过期活动清理任务执行完成 处理数量:{}", processedCount);
    }

    @Scheduled(cron = "0 */10 * * * ?")
    public void processStockSync() {
        log.info("开始执行库存同步任务");

        List<Campaign> campaigns = campaignRepository.findActiveCampaigns();
        int syncCount = 0;

        for (Campaign campaign : campaigns) {
            try {
                if (campaign.getStatus() != 5) {
                    continue;
                }
                
                int actualStock = campaignRepository.calculateRemainingStock(campaign.getCampaignId());
                if (actualStock != campaign.getRemainingStock()) {
                    campaignRepository.updateStock(campaign.getCampaignId(), actualStock);
                    log.info("库存同步 campaignId:{} dbStock:{} cacheStock:{}", 
                            campaign.getCampaignId(), actualStock, campaign.getRemainingStock());
                    syncCount++;
                }
            } catch (Exception e) {
                log.error("库存同步失败 campaignId:{}", campaign.getCampaignId(), e);
            }
        }

        log.info("库存同步任务执行完成 检查数量:{} 同步数量:{}", campaigns.size(), syncCount);
    }

    private int calculateNewStatus(Campaign campaign, LocalDateTime now) {
        int currentStatus = campaign.getStatus();
        
        switch (currentStatus) {
            case 4:
                if (now.isAfter(campaign.getStartTime()) && now.isBefore(campaign.getEndTime())) {
                    return 5;
                }
                if (now.isAfter(campaign.getEndTime())) {
                    return 7;
                }
                break;
            case 5:
                if (now.isAfter(campaign.getEndTime())) {
                    return 7;
                }
                if (campaign.getRemainingStock() <= 0) {
                    return 7;
                }
                break;
            case 6:
                if (now.isAfter(campaign.getEndTime())) {
                    return 7;
                }
                break;
            default:
                break;
        }
        
        return currentStatus;
    }

    private String getStatusName(int status) {
        switch (status) {
            case 1: return "草稿";
            case 2: return "待审核";
            case 3: return "已驳回";
            case 4: return "已通过";
            case 5: return "进行中";
            case 6: return "已暂停";
            case 7: return "已结束";
            case 8: return "已归档";
            default: return "未知";
        }
    }
}
