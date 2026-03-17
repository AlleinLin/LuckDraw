package io.prizewheel.core.service;

import io.prizewheel.core.domain.entity.RuleNode;
import io.prizewheel.core.domain.entity.RuleNodeLine;
import io.prizewheel.core.domain.entity.RuleTree;
import io.prizewheel.core.port.input.RuleEnginePort;
import io.prizewheel.core.port.output.RuleTreeRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 规则引擎服务实现
 * 
 * @author Allein
 * @since 2.0.0
 */
public class RuleEngineServiceImpl implements RuleEnginePort {

    private static final Logger log = LoggerFactory.getLogger(RuleEngineServiceImpl.class);

    private final RuleTreeRepositoryPort ruleTreeRepository;

    public RuleEngineServiceImpl(RuleTreeRepositoryPort ruleTreeRepository) {
        this.ruleTreeRepository = ruleTreeRepository;
    }

    @Override
    public Long evaluate(Long treeId, Map<String, String> userAttributes) {
        log.info("执行规则引擎 treeId:{}", treeId);
        RuleTree tree = ruleTreeRepository.findById(treeId);
        if (tree == null) {
            log.warn("规则树不存在 treeId:{}", treeId);
            return null;
        }
        return traverseTree(tree, userAttributes);
    }

    @Override
    public Long evaluate(String treeName, Map<String, String> userAttributes) {
        log.info("执行规则引擎 treeName:{}", treeName);
        RuleTree tree = ruleTreeRepository.findByName(treeName);
        if (tree == null) {
            log.warn("规则树不存在 treeName:{}", treeName);
            return null;
        }
        return traverseTree(tree, userAttributes);
    }

    @Override
    public String evaluateAndReturnNodeValue(Long treeId, Map<String, String> userAttributes) {
        Long nodeId = evaluate(treeId, userAttributes);
        if (nodeId == null) {
            return null;
        }
        RuleTree tree = ruleTreeRepository.findById(treeId);
        if (tree == null) {
            return null;
        }
        RuleNode node = tree.getNodeById(nodeId);
        return node != null ? node.getNodeValue() : null;
    }

    private Long traverseTree(RuleTree tree, Map<String, String> userAttributes) {
        RuleNode currentNode = tree.getRootNode();
        if (currentNode == null) {
            log.warn("规则树根节点为空 treeId:{}", tree.getTreeId());
            return null;
        }

        while (true) {
            if ("LEAF".equals(currentNode.getNodeType())) {
                log.info("到达叶子节点 nodeId:{} nodeValue:{}", currentNode.getNodeId(), currentNode.getNodeValue());
                return currentNode.getNodeId();
            }

            List<RuleNodeLine> lines = tree.getLinesFromNode(currentNode.getNodeId());
            if (lines == null || lines.isEmpty()) {
                log.warn("节点无连线 nodeId:{}", currentNode.getNodeId());
                return currentNode.getNodeId();
            }

            boolean matched = false;
            for (RuleNodeLine line : lines) {
                String attributeKey = currentNode.getNodeKey();
                String attributeValue = userAttributes.get(attributeKey);

                if (evaluateLine(line, attributeValue)) {
                    log.info("规则匹配成功 nodeId:{} -> nextNodeId:{}", currentNode.getNodeId(), line.getNodeIdTo());
                    currentNode = tree.getNodeById(line.getNodeIdTo());
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                log.info("无匹配规则，返回当前节点 nodeId:{}", currentNode.getNodeId());
                return currentNode.getNodeId();
            }
        }
    }

    private boolean evaluateLine(RuleNodeLine line, String attributeValue) {
        if (attributeValue == null) {
            return false;
        }

        String limitType = line.getRuleLimitType();
        String limitValue = line.getRuleLimitValue();

        if (limitType == null || limitValue == null) {
            return false;
        }

        try {
            switch (limitType) {
                case "EQUAL":
                    return attributeValue.equals(limitValue);
                case "GT":
                    return Double.parseDouble(attributeValue) > Double.parseDouble(limitValue);
                case "LT":
                    return Double.parseDouble(attributeValue) < Double.parseDouble(limitValue);
                case "GE":
                    return Double.parseDouble(attributeValue) >= Double.parseDouble(limitValue);
                case "LE":
                    return Double.parseDouble(attributeValue) <= Double.parseDouble(limitValue);
                case "ENUM":
                    String[] values = limitValue.split(",");
                    for (String v : values) {
                        if (attributeValue.equals(v.trim())) {
                            return true;
                        }
                    }
                    return false;
                default:
                    return false;
            }
        } catch (NumberFormatException e) {
            log.warn("数值解析失败 attributeValue:{} limitValue:{}", attributeValue, limitValue);
            return false;
        }
    }
}
