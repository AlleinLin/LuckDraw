package io.prizewheel.core.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户积分实体
 * 
 * @author Allein
 * @since 2.0.0
 */
public class UserPoints implements Serializable {

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

    public static final int[] LEVEL_THRESHOLDS = {0, 100, 500, 1000, 5000, 10000, 50000, 100000};
    public static final String[] LEVEL_NAMES = {"新手", "初级", "中级", "高级", "专家", "大师", "宗师", "传奇"};

    public void addPoints(int points) {
        if (points <= 0) {
            return;
        }
        this.totalPoints += points;
        this.currentPoints += points;
        this.userLevel = calculateLevel(this.totalPoints);
    }

    public boolean deductPoints(int points) {
        if (points <= 0 || this.currentPoints < points) {
            return false;
        }
        this.usedPoints += points;
        this.currentPoints -= points;
        return true;
    }

    public int calculateLevel(int totalPoints) {
        int level = 1;
        for (int i = 0; i < LEVEL_THRESHOLDS.length; i++) {
            if (totalPoints >= LEVEL_THRESHOLDS[i]) {
                level = i + 1;
            }
        }
        return level;
    }

    public String getLevelName() {
        int levelIndex = Math.min(userLevel - 1, LEVEL_NAMES.length - 1);
        return LEVEL_NAMES[Math.max(0, levelIndex)];
    }

    public boolean canSignIn() {
        if (lastSignInTime == null) {
            return true;
        }
        LocalDateTime now = LocalDateTime.now();
        return !lastSignInTime.toLocalDate().equals(now.toLocalDate());
    }

    public void signIn() {
        LocalDateTime now = LocalDateTime.now();
        if (lastSignInTime != null && lastSignInTime.toLocalDate().plusDays(1).equals(now.toLocalDate())) {
            this.continuousSignInDays++;
        } else {
            this.continuousSignInDays = 1;
        }
        this.lastSignInTime = now;
    }

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
