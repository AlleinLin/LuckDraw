package io.prizewheel.adapters.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户积分持久化实体
 * 
 * @author Allein
 * @since 2.0.0
 */
public class UserPointsPO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String userId;
    private Integer totalPoints;
    private Integer usedPoints;
    private Integer currentPoints;
    private Integer userLevel;
    private LocalDateTime lastSignInTime;
    private Integer continuousSignInDays;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getUsedPoints() {
        return usedPoints;
    }

    public void setUsedPoints(Integer usedPoints) {
        this.usedPoints = usedPoints;
    }

    public Integer getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(Integer currentPoints) {
        this.currentPoints = currentPoints;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public LocalDateTime getLastSignInTime() {
        return lastSignInTime;
    }

    public void setLastSignInTime(LocalDateTime lastSignInTime) {
        this.lastSignInTime = lastSignInTime;
    }

    public Integer getContinuousSignInDays() {
        return continuousSignInDays;
    }

    public void setContinuousSignInDays(Integer continuousSignInDays) {
        this.continuousSignInDays = continuousSignInDays;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
