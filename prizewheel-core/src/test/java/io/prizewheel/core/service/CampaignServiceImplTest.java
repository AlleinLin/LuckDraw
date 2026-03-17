package io.prizewheel.core.service;

import io.prizewheel.core.domain.entity.Campaign;
import io.prizewheel.core.port.output.CampaignRepositoryPort;
import io.prizewheel.core.port.output.IdGeneratorPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 活动服务单元测试
 * 
 * @author Allein
 * @since 2.0.0
 */
@ExtendWith(MockitoExtension.class)
class CampaignServiceImplTest {

    @Mock
    private CampaignRepositoryPort campaignRepository;

    @Mock
    private IdGeneratorPort idGenerator;

    private CampaignServiceImpl campaignService;

    @BeforeEach
    void setUp() {
        campaignService = new CampaignServiceImpl(campaignRepository, idGenerator);
    }

    @Test
    @DisplayName("创建活动 - 成功")
    void testCreateCampaign_Success() {
        Campaign campaign = createTestCampaign();
        when(idGenerator.nextId()).thenReturn(1001L);
        when(campaignRepository.save(any(Campaign.class))).thenReturn(1);

        Long campaignId = campaignService.createCampaign(campaign);

        assertNotNull(campaignId);
        assertEquals(1001L, campaignId);
        verify(campaignRepository).save(any(Campaign.class));
    }

    @Test
    @DisplayName("更新活动 - 成功")
    void testUpdateCampaign_Success() {
        Campaign campaign = createTestCampaign();
        campaign.setCampaignId(1001L);
        when(campaignRepository.update(any(Campaign.class))).thenReturn(1);

        boolean result = campaignService.updateCampaign(campaign);

        assertTrue(result);
        verify(campaignRepository).update(campaign);
    }

    @Test
    @DisplayName("查询活动 - 成功")
    void testQueryCampaign_Success() {
        Campaign campaign = createTestCampaign();
        campaign.setCampaignId(1001L);
        when(campaignRepository.findById(1001L)).thenReturn(campaign);

        Campaign result = campaignService.queryCampaign(1001L);

        assertNotNull(result);
        assertEquals(1001L, result.getCampaignId());
        assertEquals("测试活动", result.getCampaignName());
    }

    @Test
    @DisplayName("查询可用活动列表 - 成功")
    void testQueryAvailableCampaigns_Success() {
        Campaign campaign1 = createTestCampaign();
        campaign1.setCampaignId(1001L);
        Campaign campaign2 = createTestCampaign();
        campaign2.setCampaignId(1002L);
        
        when(campaignRepository.findAvailable()).thenReturn(Arrays.asList(campaign1, campaign2));

        List<Campaign> result = campaignService.queryAvailableCampaigns();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("更新活动状态 - 成功")
    void testUpdateStatus_Success() {
        when(campaignRepository.updateStatus(1001L, 5)).thenReturn(1);

        boolean result = campaignService.updateStatus(1001L, 5);

        assertTrue(result);
    }

    private Campaign createTestCampaign() {
        Campaign campaign = new Campaign();
        campaign.setCampaignName("测试活动");
        campaign.setCampaignDesc("测试活动描述");
        campaign.setStartTime(LocalDateTime.now());
        campaign.setEndTime(LocalDateTime.now().plusDays(7));
        campaign.setTotalStock(1000);
        campaign.setMaxParticipations(3);
        campaign.setCreator("admin");
        return campaign;
    }
}
