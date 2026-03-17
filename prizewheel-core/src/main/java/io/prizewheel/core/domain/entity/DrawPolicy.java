package io.prizewheel.core.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽奖策略实体
 * 
 * @author Allein
 * @since 1.0.0
 */
public class DrawPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long policyId;
    private String policyName;
    private Integer drawMode;
    private Integer grantType;
    private LocalDateTime createdAt;
    private List<PrizeConfig> prizeConfigs = new ArrayList<>();

    public void addPrizeConfig(PrizeConfig config) {
        this.prizeConfigs.add(config);
    }

    public PrizeConfig getPrizeConfig(String prizeId) {
        return prizeConfigs.stream()
                .filter(c -> c.getPrizeId().equals(prizeId))
                .findFirst()
                .orElse(null);
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<PrizeConfig> getPrizeConfigs() {
        return prizeConfigs;
    }

    public void setPrizeConfigs(List<PrizeConfig> prizeConfigs) {
        this.prizeConfigs = prizeConfigs;
    }

    public static class PrizeConfig implements Serializable {
        private static final long serialVersionUID = 1L;
        private String prizeId;
        private String prizeName;
        private Integer totalQuantity;
        private Integer remainingQuantity;
        private BigDecimal winRate;

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
}
