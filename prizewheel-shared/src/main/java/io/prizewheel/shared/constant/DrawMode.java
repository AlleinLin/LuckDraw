package io.prizewheel.shared.constant;

/**
 * 抽奖模式枚举
 * 
 * @author Allein
 * @since 1.0.0
 */
public enum DrawMode {

    SINGLE(1, "单次概率"),
    TOTAL(2, "总体概率");

    private final int code;
    private final String desc;

    DrawMode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static DrawMode fromCode(int code) {
        for (DrawMode mode : values()) {
            if (mode.code == code) {
                return mode;
            }
        }
        return null;
    }
}
