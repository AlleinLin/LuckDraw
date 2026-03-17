package io.prizewheel.api.facade;

import io.prizewheel.api.dto.PointsRequest;
import io.prizewheel.api.dto.PointsResponse;
import io.prizewheel.core.domain.entity.PointsRecord;
import io.prizewheel.core.domain.entity.UserPoints;
import io.prizewheel.core.port.input.PointsServicePort;
import io.prizewheel.shared.model.ApiResult;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 积分服务门面
 * 
 * @author Allein
 * @since 2.0.0
 */
@DubboService(version = "1.0.0")
public class PointsFacade {

    private final PointsServicePort pointsService;

    public PointsFacade(PointsServicePort pointsService) {
        this.pointsService = pointsService;
    }

    public ApiResult<PointsResponse> queryUserPoints(String userId) {
        try {
            UserPoints userPoints = pointsService.queryUserPoints(userId);
            return ApiResult.success(toResponse(userPoints));
        } catch (Exception e) {
            return ApiResult.fail("查询用户积分失败: " + e.getMessage());
        }
    }

    public ApiResult<Boolean> addPoints(PointsRequest request) {
        try {
            boolean result = pointsService.addPoints(
                    request.getUserId(),
                    request.getPoints(),
                    request.getSource(),
                    request.getRemark(),
                    request.getRelatedId()
            );
            return ApiResult.success(result);
        } catch (Exception e) {
            return ApiResult.fail("增加积分失败: " + e.getMessage());
        }
    }

    public ApiResult<Boolean> deductPoints(PointsRequest request) {
        try {
            boolean result = pointsService.deductPoints(
                    request.getUserId(),
                    request.getPoints(),
                    request.getSource(),
                    request.getRemark(),
                    request.getRelatedId()
            );
            return ApiResult.success(result);
        } catch (Exception e) {
            return ApiResult.fail("扣减积分失败: " + e.getMessage());
        }
    }

    public ApiResult<Boolean> signIn(String userId) {
        try {
            boolean result = pointsService.signIn(userId);
            return ApiResult.success(result);
        } catch (Exception e) {
            return ApiResult.fail("签到失败: " + e.getMessage());
        }
    }

    public ApiResult<Integer> calculateLevel(int totalPoints) {
        try {
            int level = pointsService.calculateLevel(totalPoints);
            return ApiResult.success(level);
        } catch (Exception e) {
            return ApiResult.fail("计算等级失败: " + e.getMessage());
        }
    }

    public ApiResult<String> getLevelName(int level) {
        try {
            String levelName = pointsService.getLevelName(level);
            return ApiResult.success(levelName);
        } catch (Exception e) {
            return ApiResult.fail("获取等级名称失败: " + e.getMessage());
        }
    }

    private PointsResponse toResponse(UserPoints userPoints) {
        PointsResponse response = new PointsResponse();
        response.setUserId(userPoints.getUserId());
        response.setTotalPoints(userPoints.getTotalPoints());
        response.setUsedPoints(userPoints.getUsedPoints());
        response.setCurrentPoints(userPoints.getCurrentPoints());
        response.setUserLevel(userPoints.getUserLevel());
        response.setLevelName(userPoints.getLevelName());
        response.setContinuousSignInDays(userPoints.getContinuousSignInDays());
        response.setCanSignIn(userPoints.canSignIn());
        return response;
    }
}
