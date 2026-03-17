package io.prizewheel.core.domain.entity;

import java.io.Serializable;

/**
 * 奖品实体
 * 
 * 奖品基本信息存储在pw_prize表
 * 数量和中奖率等策略配置存储在pw_policy_prize表
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

    public boolean isAvailable() {
        return prizeId != null && !prizeId.isEmpty();
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
}
