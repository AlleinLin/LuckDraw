package io.prizewheel.shared.constant;

/**
 * 活动状态枚举
 * 
 * @author Allein
 * @since 1.0.0
 */
public enum ActivityStatus {

    DRAFT(1, "草稿"),
    PENDING(2, "待审核"),
    REJECTED(3, "已驳回"),
    APPROVED(4, "已通过"),
    RUNNING(5, "进行中"),
    PAUSED(6, "已暂停"),
    FINISHED(7, "已结束");

    private final int code;
    private final String desc;

    ActivityStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ActivityStatus fromCode(int code) {
        for (ActivityStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
