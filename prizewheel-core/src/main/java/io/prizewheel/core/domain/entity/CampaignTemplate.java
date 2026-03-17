package io.prizewheel.core.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动模板实体
 * 
 * @author Allein
 * @since 2.0.0
 */
public class CampaignTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long templateId;
    private String templateName;
    private String templateDesc;
    private Integer defaultDurationDays;
    private Integer defaultTotalStock;
    private Integer defaultMaxParticipations;
    private Integer drawMode;
    private Integer grantType;
    private Integer status;
    private String creator;
    private Integer useCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TemplatePrizeConfig> prizeConfigs = new ArrayList<>();

    public void addPrizeConfig(TemplatePrizeConfig config) {
        this.prizeConfigs.add(config);
    }

    public Campaign createCampaign(String campaignName, LocalDateTime startTime, int durationDays) {
        Campaign campaign = new Campaign();
        campaign.setCampaignName(campaignName);
        campaign.setCampaignDesc(this.templateDesc);
        campaign.setStartTime(startTime);
        campaign.setEndTime(startTime.plusDays(durationDays > 0 ? durationDays : this.defaultDurationDays));
        campaign.setTotalStock(this.defaultTotalStock);
        campaign.setRemainingStock(this.defaultTotalStock);
        campaign.setMaxParticipations(this.defaultMaxParticipations);
        campaign.setStatus(1);
        campaign.setCreator(this.creator);
        campaign.setCreatedAt(LocalDateTime.now());
        return campaign;
    }

    public DrawPolicy createDrawPolicy() {
        DrawPolicy policy = new DrawPolicy();
        policy.setPolicyName(this.templateName + "-策略");
        policy.setDrawMode(this.drawMode);
        policy.setGrantType(this.grantType);
        policy.setCreatedAt(LocalDateTime.now());
        return policy;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateDesc() {
        return templateDesc;
    }

    public void setTemplateDesc(String templateDesc) {
        this.templateDesc = templateDesc;
    }

    public Integer getDefaultDurationDays() {
        return defaultDurationDays;
    }

    public void setDefaultDurationDays(Integer defaultDurationDays) {
        this.defaultDurationDays = defaultDurationDays;
    }

    public Integer getDefaultTotalStock() {
        return defaultTotalStock;
    }

    public void setDefaultTotalStock(Integer defaultTotalStock) {
        this.defaultTotalStock = defaultTotalStock;
    }

    public Integer getDefaultMaxParticipations() {
        return defaultMaxParticipations;
    }

    public void setDefaultMaxParticipations(Integer defaultMaxParticipations) {
        this.defaultMaxParticipations = defaultMaxParticipations;
    }

    public Integer getDrawMode() {
        return drawMode;
    }

    public void setDrawMode(Integer drawMode) {
        this.drawMode = drawMode;
    }

    public Integer getGrantType() {
        return grantType;
    }

    public void setGrantType(Integer grantType) {
        this.grantType = grantType;
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

    public Integer getUseCount() {
        return useCount;
    }

    public void setUseCount(Integer useCount) {
        this.useCount = useCount;
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

    public List<TemplatePrizeConfig> getPrizeConfigs() {
        return prizeConfigs;
    }

    public void setPrizeConfigs(List<TemplatePrizeConfig> prizeConfigs) {
        this.prizeConfigs = prizeConfigs;
    }

    public void incrementUseCount() {
        this.useCount = (this.useCount == null ? 0 : this.useCount) + 1;
    }

    public static class TemplatePrizeConfig implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long configId;
        private Long templateId;
        private String prizeId;
        private String prizeName;
        private Integer prizeType;
        private String prizeContent;
        private Integer totalQuantity;
        private BigDecimal winRate;
        private Integer sortOrder;
        private LocalDateTime createdAt;

        public Long getConfigId() {
            return configId;
        }

        public void setConfigId(Long configId) {
            this.configId = configId;
        }

        public Long getTemplateId() {
            return templateId;
        }

        public void setTemplateId(Long templateId) {
            this.templateId = templateId;
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

        public Integer getTotalQuantity() {
            return totalQuantity;
        }

        public void setTotalQuantity(Integer totalQuantity) {
            this.totalQuantity = totalQuantity;
        }

        public BigDecimal getWinRate() {
            return winRate;
        }

        public void setWinRate(BigDecimal winRate) {
            this.winRate = winRate;
        }

        public Integer getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }
}
