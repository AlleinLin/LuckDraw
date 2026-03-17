package io.prizewheel.core.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 奖品实体
 * 
 * @author Allein
 * @since 1.0.0
 */
public class Prize implements Serializable {

    private static final long serialVersionUID = 1L;

    private String prizeId;
    private Integer prizeType;
    private String prizeName;
    private String prizeContent;
    private Integer totalQuantity;
    private Integer remainingQuantity;
    private BigDecimal winRate;

    public boolean isAvailable() {
        return remainingQuantity != null && remainingQuantity > 0;
    }

    public void decreaseQuantity() {
        if (remainingQuantity > 0) {
            remainingQuantity--;
        }
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
