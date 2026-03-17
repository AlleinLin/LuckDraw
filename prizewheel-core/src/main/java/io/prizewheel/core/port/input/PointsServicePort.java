package io.prizewheel.core.port.input;

import io.prizewheel.core.domain.entity.PointsRecord;
import io.prizewheel.core.domain.entity.UserPoints;

import java.util.List;

/**
 * 积分服务端口
 * 
 * @author Allein
 * @since 2.0.0
 */
public interface PointsServicePort {

    UserPoints queryUserPoints(String userId);

    boolean addPoints(String userId, int points, String source, String remark, Long relatedId);

    boolean deductPoints(String userId, int points, String source, String remark, Long relatedId);

    boolean signIn(String userId);

    List<PointsRecord> queryPointsRecords(String userId, int page, int size);

    int calculateLevel(int totalPoints);

    String getLevelName(int level);
}
