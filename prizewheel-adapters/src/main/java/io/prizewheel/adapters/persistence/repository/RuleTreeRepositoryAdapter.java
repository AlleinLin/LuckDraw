package io.prizewheel.adapters.persistence.repository;

import io.prizewheel.adapters.persistence.entity.RuleNodePO;
import io.prizewheel.adapters.persistence.entity.RuleTreePO;
import io.prizewheel.adapters.persistence.mapper.RuleTreeMapper;
import io.prizewheel.core.domain.entity.RuleNode;
import io.prizewheel.core.domain.entity.RuleNodeLine;
import io.prizewheel.core.domain.entity.RuleTree;
import io.prizewheel.core.port.output.RuleTreeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 规则树仓储适配器
 * 
 * @author Allein
 * @since 2.0.0
 */
@Repository
public class RuleTreeRepositoryAdapter implements RuleTreeRepositoryPort {

    private final RuleTreeMapper ruleTreeMapper;

    public RuleTreeRepositoryAdapter(RuleTreeMapper ruleTreeMapper) {
        this.ruleTreeMapper = ruleTreeMapper;
    }

    @Override
    public RuleTree findById(Long treeId) {
        RuleTreePO po = ruleTreeMapper.findByTreeId(treeId);
        if (po == null) {
            return null;
        }
        return convertToEntity(po);
    }

    @Override
    public RuleTree findByName(String treeName) {
        RuleTreePO po = ruleTreeMapper.findByTreeName(treeName);
        if (po == null) {
            return null;
        }
        return convertToEntity(po);
    }

    @Override
    public void save(RuleTree ruleTree) {
        RuleTreePO po = convertToPO(ruleTree);
        ruleTreeMapper.insert(po);
    }

    private RuleTree convertToEntity(RuleTreePO po) {
        RuleTree entity = new RuleTree();
        entity.setTreeId(po.getTreeId());
        entity.setTreeName(po.getTreeName());
        entity.setTreeDesc(po.getTreeDesc());
        entity.setRootNodeId(po.getRootNodeId());
        entity.setStatus(po.getStatus());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());

        List<RuleNodePO> nodePOs = ruleTreeMapper.findNodesByTreeId(po.getTreeId());
        for (RuleNodePO nodePO : nodePOs) {
            RuleNode node = new RuleNode();
            node.setNodeId(nodePO.getNodeId());
            node.setTreeId(nodePO.getTreeId());
            node.setNodeKey(nodePO.getNodeKey());
            node.setNodeDesc(nodePO.getNodeDesc());
            node.setNodeType(nodePO.getNodeType());
            node.setNodeValue(nodePO.getNodeValue());
            node.setRuleLimitType(nodePO.getRuleLimitType());
            node.setRuleLimitValue(nodePO.getRuleLimitValue());
            node.setStatus(nodePO.getStatus());
            entity.addNode(node);
        }

        return entity;
    }

    private RuleTreePO convertToPO(RuleTree entity) {
        RuleTreePO po = new RuleTreePO();
        po.setTreeId(entity.getTreeId());
        po.setTreeName(entity.getTreeName());
        po.setTreeDesc(entity.getTreeDesc());
        po.setRootNodeId(entity.getRootNodeId());
        po.setStatus(entity.getStatus());
        return po;
    }
}
