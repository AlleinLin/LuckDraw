package io.prizewheel.shared.constant;

/**
 * 系统常量定义
 * 
 * @author Allein
 * @since 1.0.0
 */
public final class SystemConst {

    private SystemConst() {}

    public static final String SUCCESS_CODE = "0000";
    public static final String ERROR_CODE = "0001";
    public static final String UNAVAILABLE_CODE = "0002";

    public static final String SUCCESS_MSG = "操作成功";
    public static final String ERROR_MSG = "操作失败";
    public static final String UNAVAILABLE_MSG = "服务暂不可用";

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    public static final String CACHE_PREFIX = "pw:";
    public static final String LOCK_PREFIX = "pw:lock:";
    public static final String STOCK_PREFIX = "pw:stock:";

    public static final int CACHE_EXPIRE_SECONDS = 3600;
    public static final int LOCK_EXPIRE_SECONDS = 30;
}
