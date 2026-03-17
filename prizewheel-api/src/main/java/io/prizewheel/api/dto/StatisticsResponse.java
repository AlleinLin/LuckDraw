package io.prizewheel.api.dto;

import java.io.Serializable;

/**
 * 统计响应DTO
 * 
 * @author Allein
 * @since 2.0.0
 */
public class StatisticsResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String dimensionType;
    private String dimensionValue;
    private Long campaignId;
    private String prizeId;
    private Integer totalCount;
    private Integer winCount;
    private Integer grantCount;
    private Double winRate;
    private Double grantRate;

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

    public Double getWinRate() {
        return winRate;
    }

    public void setWinRate(Double winRate) {
        this.winRate = winRate;
    }

    public Double getGrantRate() {
        return grantRate;
    }

    public void setGrantRate(Double grantRate) {
        this.grantRate = grantRate;
    }
}
