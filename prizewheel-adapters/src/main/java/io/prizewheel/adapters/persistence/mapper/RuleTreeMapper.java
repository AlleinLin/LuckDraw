package io.prizewheel.adapters.persistence.mapper;

import io.prizewheel.adapters.persistence.entity.RuleTreePO;
import io.prizewheel.adapters.persistence.entity.RuleNodePO;
import io.prizewheel.adapters.persistence.entity.RuleNodeLinePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 规则树Mapper
 * 
 * @author Allein
 * @since 2.0.0
 */
@Mapper
public interface RuleTreeMapper {

    RuleTreePO findByTreeId(@Param("treeId") Long treeId);

    RuleTreePO findByTreeName(@Param("treeName") String treeName);

    List<RuleNodePO> findNodesByTreeId(@Param("treeId") Long treeId);

    List<RuleNodeLinePO> findLinesByTreeId(@Param("treeId") Long treeId);

    void insert(RuleTreePO ruleTree);

    void update(RuleTreePO ruleTree);

    void insertNode(RuleNodePO node);

    void insertLine(RuleNodeLinePO line);
}
