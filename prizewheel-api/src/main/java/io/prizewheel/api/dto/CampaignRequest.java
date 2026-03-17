package io.prizewheel.api.dto;

import java.io.Serializable;

/**
 * 活动请求DTO
 * 
 * @author Allein
 * @since 2.0.0
 */
public class CampaignRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long campaignId;
    private String campaignName;
    private String campaignDesc;
    private String startTime;
    private String endTime;
    private Integer totalStock;
    private Integer maxParticipations;
    private Long policyId;
    private String creator;

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Integer totalStock) {
        this.totalStock = totalStock;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
