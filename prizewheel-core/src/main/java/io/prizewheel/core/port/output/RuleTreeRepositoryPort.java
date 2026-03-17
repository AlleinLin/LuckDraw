package io.prizewheel.core.port.output;

import io.prizewheel.core.domain.entity.RuleTree;

/**
 * 规则树仓储端口
 * 
 * @author Allein
 * @since 2.0.0
 */
public interface RuleTreeRepositoryPort {

    RuleTree findById(Long treeId);

    RuleTree findByName(String treeName);

    void save(RuleTree ruleTree);
}
