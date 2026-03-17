package io.prizewheel.adapters.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 奖品持久化实体
 * 
 * 对应pw_prize表，存储奖品基本信息
 * 奖品的数量和中奖率存储在pw_policy_prize表中
 * 
 * @author Allein
 * @since 2.0.0
 */
public class PrizePO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String prizeId;
    private Integer prizeType;
    private String prizeName;
    private String prizeContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(String prizeId) {
        this.prizeId = prizeId;
    }

    public Integer getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(Integer prizeType) {
        this.prizeType = prizeType;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getPrizeContent() {
        return prizeContent;
    }

    public void setPrizeContent(String prizeContent) {
        this.prizeContent = prizeContent;
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
