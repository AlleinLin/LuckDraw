package io.prizewheel.adapters.persistence.repository;

import io.prizewheel.adapters.persistence.entity.RuleNodeLinePO;
import io.prizewheel.adapters.persistence.entity.RuleNodePO;
import io.prizewheel.adapters.persistence.entity.RuleTreePO;
import io.prizewheel.adapters.persistence.mapper.RuleTreeMapper;
import io.prizewheel.core.domain.entity.RuleNode;
import io.prizewheel.core.domain.entity.RuleNodeLine;
import io.prizewheel.core.domain.entity.RuleTree;
import io.prizewheel.core.port.output.RuleTreeRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 规则树仓储适配器
 * 
 * @author Allein
 * @since 2.0.0
 */
@Repository
public class RuleTreeRepositoryAdapter implements RuleTreeRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(RuleTreeRepositoryAdapter.class);

    private final RuleTreeMapper ruleTreeMapper;

    public RuleTreeRepositoryAdapter(RuleTreeMapper ruleTreeMapper) {
        this.ruleTreeMapper = ruleTreeMapper;
    }

    @Override
    public RuleTree findById(Long treeId) {
        log.debug("查询规则树 treeId:{}", treeId);
        RuleTreePO po = ruleTreeMapper.findByTreeId(treeId);
        if (po == null) {
            log.warn("规则树不存在 treeId:{}", treeId);
            return null;
        }
        return convertToEntity(po);
    }

    @Override
    public RuleTree findByName(String treeName) {
        log.debug("查询规则树 treeName:{}", treeName);
        RuleTreePO po = ruleTreeMapper.findByTreeName(treeName);
        if (po == null) {
            log.warn("规则树不存在 treeName:{}", treeName);
            return null;
        }
        return convertToEntity(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RuleTree ruleTree) {
        log.info("保存规则树 treeId:{} treeName:{}", ruleTree.getTreeId(), ruleTree.getTreeName());
        try {
            RuleTreePO po = convertToPO(ruleTree);
            ruleTreeMapper.insert(po);
            
            if (ruleTree.getNodes() != null) {
                for (RuleNode node : ruleTree.getNodes()) {
                    RuleNodePO nodePO = convertNodeToPO(node);
                    ruleTreeMapper.insertNode(nodePO);
                }
            }
            
            if (ruleTree.getLines() != null) {
                for (RuleNodeLine line : ruleTree.getLines()) {
                    RuleNodeLinePO linePO = convertLineToPO(line);
                    ruleTreeMapper.insertLine(linePO);
                }
            }
            log.info("规则树保存成功 treeId:{} nodes:{} lines:{}", 
                    ruleTree.getTreeId(), 
                    ruleTree.getNodes() != null ? ruleTree.getNodes().size() : 0,
                    ruleTree.getLines() != null ? ruleTree.getLines().size() : 0);
        } catch (Exception e) {
            log.error("规则树保存失败 treeId:{}", ruleTree.getTreeId(), e);
            throw e;
        }
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

        List<RuleNodeLinePO> linePOs = ruleTreeMapper.findLinesByTreeId(po.getTreeId());
        for (RuleNodeLinePO linePO : linePOs) {
            RuleNodeLine line = new RuleNodeLine();
            line.setLineId(linePO.getLineId());
            line.setTreeId(linePO.getTreeId());
            line.setNodeIdFrom(linePO.getNodeIdFrom());
            line.setNodeIdTo(linePO.getNodeIdTo());
            line.setRuleLimitType(linePO.getRuleLimitType());
            line.setRuleLimitValue(linePO.getRuleLimitValue());
            line.setCreatedAt(linePO.getCreatedAt());
            line.setUpdatedAt(linePO.getUpdatedAt());
            entity.addLine(line);
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

    private RuleNodePO convertNodeToPO(RuleNode entity) {
        RuleNodePO po = new RuleNodePO();
        po.setNodeId(entity.getNodeId());
        po.setTreeId(entity.getTreeId());
        po.setNodeKey(entity.getNodeKey());
        po.setNodeDesc(entity.getNodeDesc());
        po.setNodeType(entity.getNodeType());
        po.setNodeValue(entity.getNodeValue());
        po.setRuleLimitType(entity.getRuleLimitType());
        po.setRuleLimitValue(entity.getRuleLimitValue());
        po.setStatus(entity.getStatus());
        return po;
    }

    private RuleNodeLinePO convertLineToPO(RuleNodeLine entity) {
        RuleNodeLinePO po = new RuleNodeLinePO();
        po.setLineId(entity.getLineId());
        po.setTreeId(entity.getTreeId());
        po.setNodeIdFrom(entity.getNodeIdFrom());
        po.setNodeIdTo(entity.getNodeIdTo());
        po.setRuleLimitType(entity.getRuleLimitType());
        po.setRuleLimitValue(entity.getRuleLimitValue());
        return po;
    }
}
