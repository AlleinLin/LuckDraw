package io.prizewheel.core.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 中奖记录实体
 * 
 * @author Allein
 * @since 1.0.0
 */
public class WinRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long recordId;
    private String participantId;
    private Long campaignId;
    private Long policyId;
    private String prizeId;
    private String prizeName;
    private Integer prizeType;
    private String prizeContent;
    private Integer status;
    private LocalDateTime winTime;
    private LocalDateTime grantTime;

    public boolean isGranted() {
        return status != null && status == 2;
    }

    public void markAsGranted() {
        this.status = 2;
        this.grantTime = LocalDateTime.now();
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public String getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(String prizeId) {
        this.prizeId = prizeId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public Integer getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(Integer prizeType) {
        this.prizeType = prizeType;
    }

    public String getPrizeContent() {
        return prizeContent;
    }

    public void setPrizeContent(String prizeContent) {
        this.prizeContent = prizeContent;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getWinTime() {
        return winTime;
    }

    public void setWinTime(LocalDateTime winTime) {
        this.winTime = winTime;
    }

    public LocalDateTime getGrantTime() {
        return grantTime;
    }

    public void setGrantTime(LocalDateTime grantTime) {
        this.grantTime = grantTime;
    }
}
