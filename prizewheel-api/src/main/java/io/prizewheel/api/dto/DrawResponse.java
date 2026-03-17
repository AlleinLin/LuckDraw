package io.prizewheel.api.dto;

import java.io.Serializable;

/**
 * 抽奖响应DTO
 * 
 * @author Allein
 * @since 1.0.0
 */
public class DrawResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String message;
    private Long recordId;
    private String prizeId;
    private String prizeName;
    private Integer prizeType;
    private String prizeContent;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
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

    public Integer getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(Integer prizeType) {
        this.prizeType = prizeType;
    }

    public String getPrizeContent() {
        return prizeContent;
    }

    public void setPrizeContent(String prizeContent) {
        this.prizeContent = prizeContent;
    }
}
