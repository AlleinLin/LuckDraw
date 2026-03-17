package io.prizewheel.adapters.persistence.mapper;

import io.prizewheel.adapters.persistence.entity.UserPointsPO;
import io.prizewheel.adapters.persistence.entity.PointsRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 积分Mapper
 * 
 * @author Allein
 * @since 2.0.0
 */
@Mapper
public interface PointsMapper {

    UserPointsPO findByUserId(@Param("userId") String userId);

    void insert(UserPointsPO userPoints);

    void update(UserPointsPO userPoints);

    void insertRecord(PointsRecordPO record);

    List<PointsRecordPO> findRecordsByUserId(@Param("userId") String userId, @Param("offset") int offset, @Param("limit") int limit);

    int countRecordsByUserId(@Param("userId") String userId);
}
