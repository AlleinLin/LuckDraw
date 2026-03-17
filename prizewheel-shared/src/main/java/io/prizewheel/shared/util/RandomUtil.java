package io.prizewheel.shared.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机数工具类
 * 
 * @author Allein
 * @since 1.0.0
 */
public final class RandomUtil {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private RandomUtil() {}

    public static int nextInt(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be positive, got: " + bound);
        }
        return ThreadLocalRandom.current().nextInt(bound);
    }

    public static int nextSecureInt(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be positive, got: " + bound);
        }
        return SECURE_RANDOM.nextInt(bound);
    }

    public static double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static double nextSecureDouble() {
        return SECURE_RANDOM.nextDouble();
    }

    public static BigDecimal nextPercent() {
        return BigDecimal.valueOf(nextDouble(0, 100))
                .setScale(4, RoundingMode.HALF_UP);
    }

    public static double nextDouble(double min, double max) {
        if (min >= max) {
            throw new IllegalArgumentException("min must be less than max, got min: " + min + ", max: " + max);
        }
        return min + (max - min) * ThreadLocalRandom.current().nextDouble();
    }

    public static long nextLong(long bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be positive, got: " + bound);
        }
        return ThreadLocalRandom.current().nextLong(bound);
    }

    public static boolean hitProbability(double probability) {
        if (probability <= 0) {
            return false;
        }
        if (probability >= 100) {
            return true;
        }
        return nextDouble() * 100 < probability;
    }
}
