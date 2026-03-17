package io.prizewheel.core.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 统计数据实体
 * 
 * @author Allein
 * @since 2.0.0
 */
public class Statistics implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String dimensionType;
    private String dimensionValue;
    private Long campaignId;
    private String prizeId;
    private Integer totalCount;
    private Integer winCount;
    private Integer grantCount;
    private LocalDateTime statDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static final String DIMENSION_CAMPAIGN = "CAMPAIGN";
    public static final String DIMENSION_USER = "USER";
    public static final String DIMENSION_PRIZE = "PRIZE";
    public static final String DIMENSION_DAILY = "DAILY";

    public void incrementTotal() {
        this.totalCount = (this.totalCount == null ? 0 : this.totalCount) + 1;
    }

    public void incrementWin() {
        this.winCount = (this.winCount == null ? 0 : this.winCount) + 1;
    }

    public void incrementGrant() {
        this.grantCount = (this.grantCount == null ? 0 : this.grantCount) + 1;
    }

    public double getWinRate() {
        if (totalCount == null || totalCount == 0) {
            return 0.0;
        }
        return (double) (winCount == null ? 0 : winCount) / totalCount * 100;
    }

    public double getGrantRate() {
        if (winCount == null || winCount == 0) {
            return 0.0;
        }
        return (double) (grantCount == null ? 0 : grantCount) / winCount * 100;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDimensionType() {
        return dimensionType;
    }

    public void setDimensionType(String dimensionType) {
        this.dimensionType = dimensionType;
    }

    public String getDimensionValue() {
        return dimensionValue;
    }

    public void setDimensionValue(String dimensionValue) {
        this.dimensionValue = dimensionValue;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(String prizeId) {
        this.prizeId = prizeId;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getWinCount() {
        return winCount;
    }

    public void setWinCount(Integer winCount) {
        this.winCount = winCount;
    }

    public Integer getGrantCount() {
        return grantCount;
    }

    public void setGrantCount(Integer grantCount) {
        this.grantCount = grantCount;
    }

    public LocalDateTime getStatDate() {
        return statDate;
    }

    public void setStatDate(LocalDateTime statDate) {
        this.statDate = statDate;
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
