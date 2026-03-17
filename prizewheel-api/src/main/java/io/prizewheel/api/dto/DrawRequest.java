package io.prizewheel.api.dto;

import java.io.Serializable;

/**
 * 抽奖请求DTO
 * 
 * @author Allein
 * @since 1.0.0
 */
public class DrawRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String participantId;
    private Long campaignId;

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
}
