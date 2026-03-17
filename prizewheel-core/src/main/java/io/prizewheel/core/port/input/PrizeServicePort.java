package io.prizewheel.core.port.input;

import io.prizewheel.core.domain.entity.Prize;

import java.util.List;

/**
 * 奖品服务输入端口
 * 
 * @author Allein
 * @since 1.0.0
 */
public interface PrizeServicePort {

    /**
     * 创建奖品
     *
     * @param prize 奖品信息
     * @return 奖品ID
     */
    String createPrize(Prize prize);

    /**
     * 查询奖品
     *
     * @param prizeId 奖品ID
     * @return 奖品信息
     */
    Prize queryPrize(String prizeId);

    /**
     * 查询活动奖品列表
     *
     * @param campaignId 活动ID
     * @return 奖品列表
     */
    List<Prize> queryPrizesByCampaign(Long campaignId);

    /**
     * 发放奖品
     *
     * @param recordId 中奖记录ID
     * @return 是否成功
     */
    boolean grantPrize(Long recordId);
}
