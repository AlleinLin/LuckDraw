package io.prizewheel.api.facade;

import io.prizewheel.api.dto.DrawRequest;
import io.prizewheel.api.dto.DrawResponse;
import io.prizewheel.api.dto.WinRecordResponse;
import io.prizewheel.core.domain.entity.WinRecord;
import io.prizewheel.core.port.input.DrawServicePort;
import io.prizewheel.shared.model.ApiResult;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;

/**
 * 抽奖服务门面
 * 
 * @author Allein
 * @since 1.0.0
 */
@DubboService(version = "1.0.0")
public class DrawFacade {

    private static final Logger log = LoggerFactory.getLogger(DrawFacade.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final DrawServicePort drawService;

    public DrawFacade(DrawServicePort drawService) {
        this.drawService = drawService;
    }

    public ApiResult<DrawResponse> draw(DrawRequest request) {
        if (request == null) {
            return ApiResult.fail("请求参数不能为空");
        }
        if (request.getParticipantId() == null || request.getParticipantId().isEmpty()) {
            return ApiResult.fail("参与者ID不能为空");
        }
        if (request.getCampaignId() == null) {
            return ApiResult.fail("活动ID不能为空");
        }

        try {
            log.info("执行抽奖 participantId:{} campaignId:{}", request.getParticipantId(), request.getCampaignId());
            
            WinRecord record = drawService.executeDraw(request.getParticipantId(), request.getCampaignId());
            if (record == null) {
                log.warn("抽奖失败 participantId:{} campaignId:{}", request.getParticipantId(), request.getCampaignId());
                return ApiResult.fail("抽奖失败，请稍后重试");
            }

            DrawResponse response = new DrawResponse();
            response.setRecordId(record.getRecordId());
            if (record.getPrizeId() != null) {
                response.setPrizeId(record.getPrizeId());
                response.setPrizeName(record.getPrizeName());
                response.setPrizeType(record.getPrizeType());
                response.setPrizeContent(record.getPrizeContent());
            }

            log.info("抽奖完成 participantId:{} recordId:{} prizeId:{}", 
                    request.getParticipantId(), record.getRecordId(), record.getPrizeId());
            return ApiResult.success(response);
            
        } catch (Exception e) {
            log.error("抽奖异常 participantId:{} campaignId:{}", 
                    request.getParticipantId(), request.getCampaignId(), e);
            return ApiResult.fail("系统繁忙，请稍后重试");
        }
    }

    public ApiResult<WinRecordResponse> queryWinRecord(Long recordId) {
        if (recordId == null) {
            return ApiResult.fail("记录ID不能为空");
        }

        try {
            WinRecord record = drawService.queryWinRecord(recordId);
            if (record == null) {
                return ApiResult.fail("中奖记录不存在");
            }

            WinRecordResponse response = new WinRecordResponse();
            response.setRecordId(record.getRecordId());
            response.setParticipantId(record.getParticipantId());
            response.setCampaignId(record.getCampaignId());
            response.setPolicyId(record.getPolicyId());
            response.setPrizeId(record.getPrizeId());
            response.setPrizeName(record.getPrizeName());
            response.setPrizeType(record.getPrizeType());
            response.setPrizeContent(record.getPrizeContent());
            response.setStatus(record.getStatus());
            if (record.getWinTime() != null) {
                response.setWinTime(record.getWinTime().format(FORMATTER));
            }
            if (record.getGrantTime() != null) {
                response.setGrantTime(record.getGrantTime().format(FORMATTER));
            }

            return ApiResult.success(response);
            
        } catch (Exception e) {
            log.error("查询中奖记录异常 recordId:{}", recordId, e);
            return ApiResult.fail("系统繁忙，请稍后重试");
        }
    }
}
