package io.prizewheel.core.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 积分记录实体
 * 
 * @author Allein
 * @since 2.0.0
 */
public class PointsRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long recordId;
    private String userId;
    private Integer points;
    private Integer type;
    private String source;
    private String remark;
    private Long relatedId;
    private LocalDateTime createdAt;

    public static final int TYPE_ADD = 1;
    public static final int TYPE_DEDUCT = 2;

    public static final int SOURCE_SIGN_IN = 1;
    public static final int SOURCE_DRAW_WIN = 2;
    public static final int SOURCE_EXCHANGE = 3;
    public static final int SOURCE_ADMIN = 4;
    public static final int SOURCE_ACTIVITY = 5;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
