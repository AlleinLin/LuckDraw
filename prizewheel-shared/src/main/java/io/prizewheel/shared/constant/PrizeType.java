package io.prizewheel.shared.constant;

/**
 * 奖品类型枚举
 * 
 * @author Allein
 * @since 1.0.0
 */
public enum PrizeType {

    VIRTUAL(1, "虚拟奖品"),
    COUPON(2, "优惠券"),
    PHYSICAL(3, "实物奖品"),
    POINTS(4, "积分"),
    CASH(5, "现金红包");

    private final int code;
    private final String desc;

    PrizeType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PrizeType fromCode(int code) {
        for (PrizeType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的奖品类型码: " + code);
    }

    public static PrizeType fromCodeOrDefault(int code, PrizeType defaultValue) {
        for (PrizeType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return defaultValue;
    }
}
