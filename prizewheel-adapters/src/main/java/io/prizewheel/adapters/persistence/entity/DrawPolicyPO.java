package io.prizewheel.adapters.persistence.entity;

import java.io.Serializable;

/**
 * 抽奖策略持久化实体
 * 
 * @author Allein
 * @since 1.0.0
 */
public class DrawPolicyPO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long policyId;
    private String policyName;
    private Integer drawMode;
    private Integer grantType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
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
}
