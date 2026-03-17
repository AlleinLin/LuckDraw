package io.prizewheel.api.facade;

import io.prizewheel.api.dto.DrawRequest;
import io.prizewheel.api.dto.DrawResponse;
import io.prizewheel.api.dto.WinRecordResponse;
import io.prizewheel.core.domain.entity.WinRecord;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;

/**
 * 抽奖服务门面
 * 
 * @author Allein
 * @since 1.0.0
 */
@DubboService
public class DrawFacade {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Resource
    private io.prizewheel.core.port.input.DrawServicePort drawService;

    public DrawResponse draw(DrawRequest request) {
        DrawResponse response = new DrawResponse();
        try {
            WinRecord record = drawService.executeDraw(request.getParticipantId(), request.getCampaignId());
            if (record == null) {
                response.setCode("0002");
                response.setMessage("抽奖失败");
                return response;
            }
            response.setCode("0000");
            response.setMessage("操作成功");
            response.setRecordId(record.getRecordId());
            if (record.getPrizeId() != null) {
                response.setPrizeId(record.getPrizeId());
                response.setPrizeName(record.getPrizeName());
                response.setPrizeType(record.getPrizeType());
                response.setPrizeContent(record.getPrizeContent());
            }
        } catch (Exception e) {
            response.setCode("0001");
            response.setMessage("系统异常: " + e.getMessage());
        }
        return response;
    }

    public WinRecordResponse queryWinRecord(Long recordId) {
        WinRecordResponse response = new WinRecordResponse();
        try {
            WinRecord record = drawService.queryWinRecord(recordId);
            if (record == null) {
                response.setStatus(-1);
                return response;
            }
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
        } catch (Exception e) {
            response.setStatus(-1);
        }
        return response;
    }
}
