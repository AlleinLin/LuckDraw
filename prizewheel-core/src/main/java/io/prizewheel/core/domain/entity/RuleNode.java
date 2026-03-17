package io.prizewheel.core.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 规则树节点实体
 * 
 * @author Allein
 * @since 2.0.0
 */
public class RuleNode implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long nodeId;
    private Long treeId;
    private String nodeKey;
    private String nodeDesc;
    private String nodeType;
    private String nodeValue;
    private String ruleLimitType;
    private String ruleLimitValue;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean evaluate(String attributeValue) {
        if (attributeValue == null || ruleLimitValue == null) {
            return false;
        }

        switch (ruleLimitType) {
            case "EQUAL":
                return attributeValue.equals(ruleLimitValue);
            case "GT":
                return Double.parseDouble(attributeValue) > Double.parseDouble(ruleLimitValue);
            case "LT":
                return Double.parseDouble(attributeValue) < Double.parseDouble(ruleLimitValue);
            case "GE":
                return Double.parseDouble(attributeValue) >= Double.parseDouble(ruleLimitValue);
            case "LE":
                return Double.parseDouble(attributeValue) <= Double.parseDouble(ruleLimitValue);
            case "ENUM":
                String[] values = ruleLimitValue.split(",");
                for (String v : values) {
                    if (attributeValue.equals(v.trim())) {
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getTreeId() {
        return treeId;
    }

    public void setTreeId(Long treeId) {
        this.treeId = treeId;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    public String getNodeDesc() {
        return nodeDesc;
    }

    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }

    public String getRuleLimitType() {
        return ruleLimitType;
    }

    public void setRuleLimitType(String ruleLimitType) {
        this.ruleLimitType = ruleLimitType;
    }

    public String getRuleLimitValue() {
        return ruleLimitValue;
    }

    public void setRuleLimitValue(String ruleLimitValue) {
        this.ruleLimitValue = ruleLimitValue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
