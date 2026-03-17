package io.prizewheel.adapters.persistence.repository;

import io.prizewheel.adapters.persistence.entity.PointsRecordPO;
import io.prizewheel.adapters.persistence.entity.UserPointsPO;
import io.prizewheel.adapters.persistence.mapper.PointsMapper;
import io.prizewheel.core.domain.entity.PointsRecord;
import io.prizewheel.core.domain.entity.UserPoints;
import io.prizewheel.core.port.output.PointsRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 积分仓储适配器
 * 
 * @author Allein
 * @since 2.0.0
 */
@Repository
public class PointsRepositoryAdapter implements PointsRepositoryPort {

    private final PointsMapper pointsMapper;

    public PointsRepositoryAdapter(PointsMapper pointsMapper) {
        this.pointsMapper = pointsMapper;
    }

    @Override
    public UserPoints findByUserId(String userId) {
        UserPointsPO po = pointsMapper.findByUserId(userId);
        if (po == null) {
            return null;
        }
        return convertToEntity(po);
    }

    @Override
    public void save(UserPoints userPoints) {
        UserPointsPO existing = pointsMapper.findByUserId(userPoints.getUserId());
        UserPointsPO po = convertToPO(userPoints);
        if (existing == null) {
            pointsMapper.insert(po);
        } else {
            po.setId(existing.getId());
            pointsMapper.update(po);
        }
    }

    @Override
    public void saveRecord(PointsRecord record) {
        PointsRecordPO po = convertRecordToPO(record);
        pointsMapper.insertRecord(po);
    }

    @Override
    public List<PointsRecord> findRecordsByUserId(String userId, int page, int size) {
        int offset = (page - 1) * size;
        List<PointsRecordPO> pos = pointsMapper.findRecordsByUserId(userId, offset, size);
        return pos.stream().map(this::convertRecordToEntity).collect(Collectors.toList());
    }

    @Override
    public int countRecordsByUserId(String userId) {
        return pointsMapper.countRecordsByUserId(userId);
    }

    private UserPoints convertToEntity(UserPointsPO po) {
        UserPoints entity = new UserPoints();
        entity.setId(po.getId());
        entity.setUserId(po.getUserId());
        entity.setTotalPoints(po.getTotalPoints());
        entity.setUsedPoints(po.getUsedPoints());
        entity.setCurrentPoints(po.getCurrentPoints());
        entity.setUserLevel(po.getUserLevel());
        entity.setLastSignInTime(po.getLastSignInTime());
        entity.setContinuousSignInDays(po.getContinuousSignInDays());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private UserPointsPO convertToPO(UserPoints entity) {
        UserPointsPO po = new UserPointsPO();
        po.setId(entity.getId());
        po.setUserId(entity.getUserId());
        po.setTotalPoints(entity.getTotalPoints());
        po.setUsedPoints(entity.getUsedPoints());
        po.setCurrentPoints(entity.getCurrentPoints());
        po.setUserLevel(entity.getUserLevel());
        po.setLastSignInTime(entity.getLastSignInTime());
        po.setContinuousSignInDays(entity.getContinuousSignInDays());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        return po;
    }

    private PointsRecord convertRecordToEntity(PointsRecordPO po) {
        PointsRecord entity = new PointsRecord();
        entity.setRecordId(po.getRecordId());
        entity.setUserId(po.getUserId());
        entity.setPoints(po.getPoints());
        entity.setType(po.getType());
        entity.setSource(po.getSource());
        entity.setRemark(po.getRemark());
        entity.setRelatedId(po.getRelatedId());
        entity.setCreatedAt(po.getCreatedAt());
        return entity;
    }

    private PointsRecordPO convertRecordToPO(PointsRecord entity) {
        PointsRecordPO po = new PointsRecordPO();
        po.setRecordId(entity.getRecordId());
        po.setUserId(entity.getUserId());
        po.setPoints(entity.getPoints());
        po.setType(entity.getType());
        po.setSource(entity.getSource());
        po.setRemark(entity.getRemark());
        po.setRelatedId(entity.getRelatedId());
        return po;
    }
}
