package io.prizewheel.core.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 活动实体
 * 
 * @author Allein
 * @since 1.0.0
 */
public class Campaign implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long campaignId;
    private String campaignName;
    private String campaignDesc;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalStock;
    private Integer remainingStock;
    private Integer maxParticipations;
    private Long policyId;
    private Integer status;
    private String creator;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isAvailable() {
        LocalDateTime now = LocalDateTime.now();
        return status != null 
                && status == 5
                && now.isAfter(startTime) 
                && now.isBefore(endTime)
                && remainingStock > 0;
    }

    public boolean canParticipate(String participantId, int participatedCount) {
        return isAvailable() && participatedCount < maxParticipations;
    }

    public void decreaseStock() {
        if (remainingStock > 0) {
            remainingStock--;
        }
    }

    public void increaseStock() {
        remainingStock++;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignDesc() {
        return campaignDesc;
    }

    public void setCampaignDesc(String campaignDesc) {
        this.campaignDesc = campaignDesc;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Integer totalStock) {
        this.totalStock = totalStock;
    }

    public Integer getRemainingStock() {
        return remainingStock;
    }

    public void setRemainingStock(Integer remainingStock) {
        this.remainingStock = remainingStock;
    }

    public Integer getMaxParticipations() {
        return maxParticipations;
    }

    public void setMaxParticipations(Integer maxParticipations) {
        this.maxParticipations = maxParticipations;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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
