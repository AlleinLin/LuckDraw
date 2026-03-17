package io.prizewheel.api.dto;

import java.io.Serializable;

/**
 * 活动模板响应DTO
 * 
 * @author Allein
 * @since 2.0.0
 */
public class TemplateResponse implements Serializable {

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
}
