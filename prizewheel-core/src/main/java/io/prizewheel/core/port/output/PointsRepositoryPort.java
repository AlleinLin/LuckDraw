package io.prizewheel.core.port.output;

import io.prizewheel.core.domain.entity.PointsRecord;
import io.prizewheel.core.domain.entity.UserPoints;

import java.util.List;

/**
 * 积分仓储端口
 * 
 * @author Allein
 * @since 2.0.0
 */
public interface PointsRepositoryPort {

    UserPoints findByUserId(String userId);

    void save(UserPoints userPoints);

    void saveRecord(PointsRecord record);

    List<PointsRecord> findRecordsByUserId(String userId, int page, int size);

    int countRecordsByUserId(String userId);
}
