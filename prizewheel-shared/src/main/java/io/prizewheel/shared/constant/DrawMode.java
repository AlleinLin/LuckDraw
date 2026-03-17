package io.prizewheel.shared.constant;

/**
 * 抽奖模式枚举
 * 
 * @author Allein
 * @since 1.0.0
 */
public enum DrawMode {

    SINGLE_PROBABILITY(1, "单次抽奖独立概率模式"),
    TOTAL_PROBABILITY(2, "总体概率分配模式");

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
        throw new IllegalArgumentException("无效的抽奖模式码: " + code);
    }

    public static DrawMode fromCodeOrDefault(int code, DrawMode defaultValue) {
        for (DrawMode mode : values()) {
            if (mode.code == code) {
                return mode;
            }
        }
        return defaultValue;
    }
}
