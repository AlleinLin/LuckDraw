package io.prizewheel.core.domain.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 活动状态机
 * 
 * @author Allein
 * @since 2.0.0
 */
public class CampaignState {

    private static final Logger log = LoggerFactory.getLogger(CampaignState.class);

    public static final int DRAFT = 1;
    public static final int PENDING_REVIEW = 2;
    public static final int REJECTED = 3;
    public static final int APPROVED = 4;
    public static final int RUNNING = 5;
    public static final int PAUSED = 6;
    public static final int CLOSED = 7;
    public static final int ARCHIVED = 8;

    public static boolean canSubmit(int currentStatus) {
        return currentStatus == DRAFT || currentStatus == REJECTED;
    }

    public static boolean canApprove(int currentStatus) {
        return currentStatus == PENDING_REVIEW;
    }

    public static boolean canReject(int currentStatus) {
        return currentStatus == PENDING_REVIEW;
    }

    public static boolean canRevoke(int currentStatus) {
        return currentStatus == PENDING_REVIEW;
    }

    public static boolean canStart(int currentStatus) {
        return currentStatus == APPROVED;
    }

    public static boolean canPause(int currentStatus) {
        return currentStatus == RUNNING;
    }

    public static boolean canResume(int currentStatus) {
        return currentStatus == PAUSED;
    }

    public static boolean canClose(int currentStatus) {
        return currentStatus == RUNNING || currentStatus == PAUSED;
    }

    public static boolean canArchive(int currentStatus) {
        return currentStatus == CLOSED;
    }

    public static int submit(int currentStatus) {
        if (!canSubmit(currentStatus)) {
            log.warn("状态不允许提审 currentStatus:{}", currentStatus);
            return currentStatus;
        }
        return PENDING_REVIEW;
    }

    public static int approve(int currentStatus) {
        if (!canApprove(currentStatus)) {
            log.warn("状态不允许审核通过 currentStatus:{}", currentStatus);
            return currentStatus;
        }
        return APPROVED;
    }

    public static int reject(int currentStatus) {
        if (!canReject(currentStatus)) {
            log.warn("状态不允许审核拒绝 currentStatus:{}", currentStatus);
            return currentStatus;
        }
        return REJECTED;
    }

    public static int revoke(int currentStatus) {
        if (!canRevoke(currentStatus)) {
            log.warn("状态不允许撤审 currentStatus:{}", currentStatus);
            return currentStatus;
        }
        return DRAFT;
    }

    public static int start(int currentStatus) {
        if (!canStart(currentStatus)) {
            log.warn("状态不允许开始 currentStatus:{}", currentStatus);
            return currentStatus;
        }
        return RUNNING;
    }

    public static int pause(int currentStatus) {
        if (!canPause(currentStatus)) {
            log.warn("状态不允许暂停 currentStatus:{}", currentStatus);
            return currentStatus;
        }
        return PAUSED;
    }

    public static int resume(int currentStatus) {
        if (!canResume(currentStatus)) {
            log.warn("状态不允许恢复 currentStatus:{}", currentStatus);
            return currentStatus;
        }
        return RUNNING;
    }

    public static int close(int currentStatus) {
        if (!canClose(currentStatus)) {
            log.warn("状态不允许关闭 currentStatus:{}", currentStatus);
            return currentStatus;
        }
        return CLOSED;
    }

    public static int archive(int currentStatus) {
        if (!canArchive(currentStatus)) {
            log.warn("状态不允许归档 currentStatus:{}", currentStatus);
            return currentStatus;
        }
        return ARCHIVED;
    }

    public static String getStatusName(int status) {
        switch (status) {
            case DRAFT: return "草稿";
            case PENDING_REVIEW: return "待审核";
            case REJECTED: return "已驳回";
            case APPROVED: return "已通过";
            case RUNNING: return "进行中";
            case PAUSED: return "已暂停";
            case CLOSED: return "已关闭";
            case ARCHIVED: return "已归档";
            default: return "未知";
        }
    }

    public static boolean isActive(int status) {
        return status == RUNNING;
    }

    public static boolean isEditable(int status) {
        return status == DRAFT || status == REJECTED;
    }
}
