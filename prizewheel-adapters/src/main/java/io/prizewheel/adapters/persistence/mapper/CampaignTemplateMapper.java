package io.prizewheel.adapters.persistence.mapper;

import io.prizewheel.adapters.persistence.entity.CampaignTemplatePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动模板Mapper接口
 * 
 * @author Allein
 * @since 2.0.0
 */
@Mapper
public interface CampaignTemplateMapper {

    CampaignTemplatePO findById(@Param("templateId") Long templateId);

    List<CampaignTemplatePO> findAll();

    List<CampaignTemplatePO> findByCreator(@Param("creator") String creator);

    int insert(CampaignTemplatePO template);

    int update(CampaignTemplatePO template);

    int delete(@Param("templateId") Long templateId);

    int incrementUseCount(@Param("templateId") Long templateId);
}
