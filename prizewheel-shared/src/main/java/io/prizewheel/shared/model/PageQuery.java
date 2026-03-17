package io.prizewheel.shared.model;

import java.io.Serializable;

/**
 * 分页请求参数
 * 
 * @author Allein
 * @since 1.0.0
 */
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pageNum = 1;
    private int pageSize = 20;

    public PageQuery() {}

    public PageQuery(int pageNum, int pageSize) {
        this.pageNum = Math.max(1, pageNum);
        this.pageSize = Math.min(Math.max(1, pageSize), 100);
    }

    public int getOffset() {
        return (pageNum - 1) * pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = Math.max(1, pageNum);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = Math.min(Math.max(1, pageSize), 100);
    }
}
