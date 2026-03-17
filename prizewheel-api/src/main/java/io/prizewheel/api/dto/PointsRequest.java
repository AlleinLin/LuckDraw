package io.prizewheel.api.dto;

import java.io.Serializable;

/**
 * 积分请求DTO
 * 
 * @author Allein
 * @since 2.0.0
 */
public class PointsRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private Integer points;
    private String source;
    private String remark;
    private Long relatedId;
    private Integer page;
    private Integer size;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
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
