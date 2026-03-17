package io.prizewheel.core.service;

import io.prizewheel.core.domain.entity.PointsRecord;
import io.prizewheel.core.domain.entity.UserPoints;
import io.prizewheel.core.port.input.PointsServicePort;
import io.prizewheel.core.port.output.IdGeneratorPort;
import io.prizewheel.core.port.output.PointsRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 积分服务实现
 * 
 * @author Allein
 * @since 2.0.0
 */
public class PointsServiceImpl implements PointsServicePort {

    private static final Logger log = LoggerFactory.getLogger(PointsServiceImpl.class);

    private static final int SIGN_IN_POINTS = 10;
    private static final int CONTINUOUS_BONUS_THRESHOLD = 7;
    private static final int CONTINUOUS_BONUS_MULTIPLIER = 2;

    private final PointsRepositoryPort pointsRepository;
    private final IdGeneratorPort idGenerator;

    public PointsServiceImpl(PointsRepositoryPort pointsRepository, IdGeneratorPort idGenerator) {
        this.pointsRepository = pointsRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    public UserPoints queryUserPoints(String userId) {
        log.info("查询用户积分 userId:{}", userId);
        UserPoints userPoints = pointsRepository.findByUserId(userId);
        if (userPoints == null) {
            userPoints = createNewUserPoints(userId);
        }
        return userPoints;
    }

    @Override
    public boolean addPoints(String userId, int points, String source, String remark, Long relatedId) {
        log.info("增加积分 userId:{} points:{} source:{}", userId, points, source);

        if (points <= 0) {
            log.warn("积分数量无效 points:{}", points);
            return false;
        }

        UserPoints userPoints = pointsRepository.findByUserId(userId);
        if (userPoints == null) {
            userPoints = createNewUserPoints(userId);
        }

        userPoints.addPoints(points);
        userPoints.setUpdatedAt(LocalDateTime.now());
        pointsRepository.save(userPoints);

        PointsRecord record = new PointsRecord();
        record.setRecordId(idGenerator.nextId());
        record.setUserId(userId);
        record.setPoints(points);
        record.setType(PointsRecord.TYPE_ADD);
        record.setSource(source);
        record.setRemark(remark);
        record.setRelatedId(relatedId);
        record.setCreatedAt(LocalDateTime.now());
        pointsRepository.saveRecord(record);

        log.info("积分增加成功 userId:{} totalPoints:{} level:{}", userId, userPoints.getTotalPoints(), userPoints.getUserLevel());
        return true;
    }

    @Override
    public boolean deductPoints(String userId, int points, String source, String remark, Long relatedId) {
        log.info("扣减积分 userId:{} points:{} source:{}", userId, points, source);

        if (points <= 0) {
            log.warn("积分数量无效 points:{}", points);
            return false;
        }

        UserPoints userPoints = pointsRepository.findByUserId(userId);
        if (userPoints == null) {
            log.warn("用户积分记录不存在 userId:{}", userId);
            return false;
        }

        if (!userPoints.deductPoints(points)) {
            log.warn("积分不足 userId:{} currentPoints:{} required:{}", userId, userPoints.getCurrentPoints(), points);
            return false;
        }

        userPoints.setUpdatedAt(LocalDateTime.now());
        pointsRepository.save(userPoints);

        PointsRecord record = new PointsRecord();
        record.setRecordId(idGenerator.nextId());
        record.setUserId(userId);
        record.setPoints(points);
        record.setType(PointsRecord.TYPE_DEDUCT);
        record.setSource(source);
        record.setRemark(remark);
        record.setRelatedId(relatedId);
        record.setCreatedAt(LocalDateTime.now());
        pointsRepository.saveRecord(record);

        log.info("积分扣减成功 userId:{} currentPoints:{}", userId, userPoints.getCurrentPoints());
        return true;
    }

    @Override
    public boolean signIn(String userId) {
        log.info("用户签到 userId:{}", userId);

        UserPoints userPoints = pointsRepository.findByUserId(userId);
        if (userPoints == null) {
            userPoints = createNewUserPoints(userId);
        }

        if (!userPoints.canSignIn()) {
            log.info("今日已签到 userId:{}", userId);
            return false;
        }

        int signInPoints = SIGN_IN_POINTS;
        String remark = "每日签到";

        if (userPoints.getContinuousSignInDays() + 1 >= CONTINUOUS_BONUS_THRESHOLD) {
            signInPoints = SIGN_IN_POINTS * CONTINUOUS_BONUS_MULTIPLIER;
            remark = "连续签到奖励";
        }

        userPoints.signIn();
        userPoints.addPoints(signInPoints);
        userPoints.setUpdatedAt(LocalDateTime.now());
        pointsRepository.save(userPoints);

        PointsRecord record = new PointsRecord();
        record.setRecordId(idGenerator.nextId());
        record.setUserId(userId);
        record.setPoints(signInPoints);
        record.setType(PointsRecord.TYPE_ADD);
        record.setSource(String.valueOf(PointsRecord.SOURCE_SIGN_IN));
        record.setRemark(remark + "(连续" + userPoints.getContinuousSignInDays() + "天)");
        record.setCreatedAt(LocalDateTime.now());
        pointsRepository.saveRecord(record);

        log.info("签到成功 userId:{} points:{} continuousDays:{}", userId, signInPoints, userPoints.getContinuousSignInDays());
        return true;
    }

    @Override
    public List<PointsRecord> queryPointsRecords(String userId, int page, int size) {
        log.info("查询积分记录 userId:{} page:{} size:{}", userId, page, size);
        return pointsRepository.findRecordsByUserId(userId, page, size);
    }

    @Override
    public int calculateLevel(int totalPoints) {
        int level = 1;
        for (int i = 0; i < UserPoints.LEVEL_THRESHOLDS.length; i++) {
            if (totalPoints >= UserPoints.LEVEL_THRESHOLDS[i]) {
                level = i + 1;
            }
        }
        return level;
    }

    @Override
    public String getLevelName(int level) {
        int levelIndex = Math.min(level - 1, UserPoints.LEVEL_NAMES.length - 1);
        return UserPoints.LEVEL_NAMES[Math.max(0, levelIndex)];
    }

    private UserPoints createNewUserPoints(String userId) {
        UserPoints userPoints = new UserPoints();
        userPoints.setUserId(userId);
        userPoints.setTotalPoints(0);
        userPoints.setUsedPoints(0);
        userPoints.setCurrentPoints(0);
        userPoints.setUserLevel(1);
        userPoints.setContinuousSignInDays(0);
        userPoints.setCreatedAt(LocalDateTime.now());
        userPoints.setUpdatedAt(LocalDateTime.now());
        pointsRepository.save(userPoints);
        return userPoints;
    }
}
