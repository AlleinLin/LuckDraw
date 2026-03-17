package io.prizewheel.adapters.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 策略奖品配置持久化实体
 * 
 * @author Allein
 * @since 1.0.0
 */
public class PolicyPrizePO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long policyId;
    private String prizeId;
    private String prizeName;
    private Integer totalQuantity;
    private Integer remainingQuantity;
    private BigDecimal winRate;

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

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(Integer remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public BigDecimal getWinRate() {
        return winRate;
    }

    public void setWinRate(BigDecimal winRate) {
        this.winRate = winRate;
    }
}
