package io.prizewheel.api.dto;

import java.io.Serializable;

/**
 * 积分响应DTO
 * 
 * @author Allein
 * @since 2.0.0
 */
public class PointsResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private Integer totalPoints;
    private Integer usedPoints;
    private Integer currentPoints;
    private Integer userLevel;
    private String levelName;
    private Integer continuousSignInDays;
    private boolean canSignIn;

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

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Integer getContinuousSignInDays() {
        return continuousSignInDays;
    }

    public void setContinuousSignInDays(Integer continuousSignInDays) {
        this.continuousSignInDays = continuousSignInDays;
    }

    public boolean isCanSignIn() {
        return canSignIn;
    }

    public void setCanSignIn(boolean canSignIn) {
        this.canSignIn = canSignIn;
    }
}
