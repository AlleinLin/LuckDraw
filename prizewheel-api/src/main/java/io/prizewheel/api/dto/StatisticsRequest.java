package io.prizewheel.api.dto;

import java.io.Serializable;

/**
 * 统计请求DTO
 * 
 * @author Allein
 * @since 2.0.0
 */
public class StatisticsRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long campaignId;
    private String userId;
    private String prizeId;
    private Integer limit;
    private Integer page;
    private Integer size;

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(String prizeId) {
        this.prizeId = prizeId;
    }

    public Integer getLimit() {
        return limit != null ? limit : 10;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPage() {
        return page != null ? page : 1;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size != null ? size : 10;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
