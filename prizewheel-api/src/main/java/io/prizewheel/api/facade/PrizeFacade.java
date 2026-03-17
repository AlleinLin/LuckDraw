package io.prizewheel.api.facade;

import io.prizewheel.api.dto.PrizeRequest;
import io.prizewheel.api.dto.PrizeResponse;
import io.prizewheel.core.domain.entity.Prize;
import io.prizewheel.core.port.input.PrizeServicePort;
import io.prizewheel.shared.model.ApiResult;
import org.apache.dubbo.config.annotation.DubboService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 奖品服务门面
 * 
 * @author Allein
 * @since 2.0.0
 */
@DubboService(version = "1.0.0")
public class PrizeFacade {

    private final PrizeServicePort prizeService;

    public PrizeFacade(PrizeServicePort prizeService) {
        this.prizeService = prizeService;
    }

    public ApiResult<String> createPrize(PrizeRequest request) {
        try {
            Prize prize = toEntity(request);
            String prizeId = prizeService.createPrize(prize);
            return ApiResult.success(prizeId);
        } catch (Exception e) {
            return ApiResult.fail("创建奖品失败: " + e.getMessage());
        }
    }

    public ApiResult<PrizeResponse> queryPrize(String prizeId) {
        try {
            Prize prize = prizeService.queryPrize(prizeId);
            if (prize == null) {
                return ApiResult.fail("奖品不存在");
            }
            return ApiResult.success(toResponse(prize));
        } catch (Exception e) {
            return ApiResult.fail("查询奖品失败: " + e.getMessage());
        }
    }

    public ApiResult<List<PrizeResponse>> queryPrizesByCampaign(Long campaignId) {
        try {
            List<Prize> prizes = prizeService.queryPrizesByCampaign(campaignId);
            List<PrizeResponse> responses = prizes.stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return ApiResult.success(responses);
        } catch (Exception e) {
            return ApiResult.fail("查询活动奖品失败: " + e.getMessage());
        }
    }

    public ApiResult<Boolean> grantPrize(Long recordId) {
        try {
            boolean result = prizeService.grantPrize(recordId);
            return ApiResult.success(result);
        } catch (Exception e) {
            return ApiResult.fail("发放奖品失败: " + e.getMessage());
        }
    }

    private Prize toEntity(PrizeRequest request) {
        Prize prize = new Prize();
        prize.setPrizeId(request.getPrizeId());
        prize.setPrizeType(request.getPrizeType());
        prize.setPrizeName(request.getPrizeName());
        prize.setPrizeContent(request.getPrizeContent());
        prize.setTotalQuantity(request.getTotalQuantity());
        prize.setRemainingQuantity(request.getRemainingQuantity() != null ? request.getRemainingQuantity() : request.getTotalQuantity());
        if (request.getWinRate() != null) {
            prize.setWinRate(new BigDecimal(request.getWinRate()));
        }
        return prize;
    }

    private PrizeResponse toResponse(Prize prize) {
        PrizeResponse response = new PrizeResponse();
        response.setPrizeId(prize.getPrizeId());
        response.setPrizeType(prize.getPrizeType());
        response.setPrizeName(prize.getPrizeName());
        response.setPrizeContent(prize.getPrizeContent());
        response.setTotalQuantity(prize.getTotalQuantity());
        response.setRemainingQuantity(prize.getRemainingQuantity());
        if (prize.getWinRate() != null) {
            response.setWinRate(prize.getWinRate().toPlainString());
        }
        response.setAvailable(prize.isAvailable());
        return response;
    }
}
