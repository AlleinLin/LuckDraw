package io.prizewheel.core.service;

import io.prizewheel.core.domain.entity.RuleNode;
import io.prizewheel.core.domain.entity.RuleNodeLine;
import io.prizewheel.core.domain.entity.RuleTree;
import io.prizewheel.core.port.output.RuleTreeRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 规则引擎服务单元测试
 * 
 * @author Allein
 * @since 2.0.0
 */
@ExtendWith(MockitoExtension.class)
class RuleEngineServiceImplTest {

    @Mock
    private RuleTreeRepositoryPort ruleTreeRepository;

    private RuleEngineServiceImpl ruleEngineService;

    @BeforeEach
    void setUp() {
        ruleEngineService = new RuleEngineServiceImpl(ruleTreeRepository);
    }

    @Test
    @DisplayName("规则引擎评估 - 通过树ID评估成功")
    void testEvaluate_ByTreeId_Success() {
        RuleTree tree = createTestRuleTree();
        when(ruleTreeRepository.findById(3001L)).thenReturn(tree);

        Map<String, String> attributes = new HashMap<>();
        attributes.put("age", "25");

        Long result = ruleEngineService.evaluate(3001L, attributes);

        assertNotNull(result);
        verify(ruleTreeRepository).findById(3001L);
    }

    @Test
    @DisplayName("规则引擎评估 - 通过树名称评估成功")
    void testEvaluate_ByTreeName_Success() {
        RuleTree tree = createTestRuleTree();
        when(ruleTreeRepository.findByName("用户年龄规则树")).thenReturn(tree);

        Map<String, String> attributes = new HashMap<>();
        attributes.put("age", "25");

        Long result = ruleEngineService.evaluate("用户年龄规则树", attributes);

        assertNotNull(result);
        verify(ruleTreeRepository).findByName("用户年龄规则树");
    }

    @Test
    @DisplayName("规则引擎评估 - 树不存在")
    void testEvaluate_TreeNotFound() {
        when(ruleTreeRepository.findById(9999L)).thenReturn(null);

        Map<String, String> attributes = new HashMap<>();
        attributes.put("age", "25");

        Long result = ruleEngineService.evaluate(9999L, attributes);

        assertNull(result);
    }

    @Test
    @DisplayName("规则引擎评估 - EQUAL条件匹配")
    void testEvaluate_EqualCondition() {
        RuleTree tree = createEqualConditionTree();
        when(ruleTreeRepository.findById(3001L)).thenReturn(tree);

        Map<String, String> attributes = new HashMap<>();
        attributes.put("gender", "male");

        Long result = ruleEngineService.evaluate(3001L, attributes);

        assertNotNull(result);
    }

    @Test
    @DisplayName("规则引擎评估 - GT条件匹配")
    void testEvaluate_GtCondition() {
        RuleTree tree = createGtConditionTree();
        when(ruleTreeRepository.findById(3001L)).thenReturn(tree);

        Map<String, String> attributes = new HashMap<>();
        attributes.put("score", "85");

        Long result = ruleEngineService.evaluate(3001L, attributes);

        assertNotNull(result);
    }

    @Test
    @DisplayName("规则引擎评估 - ENUM条件匹配")
    void testEvaluate_EnumCondition() {
        RuleTree tree = createEnumConditionTree();
        when(ruleTreeRepository.findById(3001L)).thenReturn(tree);

        Map<String, String> attributes = new HashMap<>();
        attributes.put("city", "beijing");

        Long result = ruleEngineService.evaluate(3001L, attributes);

        assertNotNull(result);
    }

    @Test
    @DisplayName("规则引擎评估 - 属性值不存在")
    void testEvaluate_AttributeNotFound() {
        RuleTree tree = createTestRuleTree();
        when(ruleTreeRepository.findById(3001L)).thenReturn(tree);

        Map<String, String> attributes = new HashMap<>();

        Long result = ruleEngineService.evaluate(3001L, attributes);

        assertNotNull(result);
    }

    @Test
    @DisplayName("规则引擎评估 - 返回节点值")
    void testEvaluateAndReturnNodeValue() {
        RuleTree tree = createTestRuleTree();
        when(ruleTreeRepository.findById(3001L)).thenReturn(tree);

        Map<String, String> attributes = new HashMap<>();
        attributes.put("age", "25");

        String nodeValue = ruleEngineService.evaluateAndReturnNodeValue(3001L, attributes);

        assertNotNull(nodeValue);
    }

    private RuleTree createTestRuleTree() {
        RuleTree tree = new RuleTree();
        tree.setTreeId(3001L);
        tree.setTreeName("用户年龄规则树");
        tree.setRootNodeId(30001L);

        RuleNode rootNode = new RuleNode();
        rootNode.setNodeId(30001L);
        rootNode.setTreeId(3001L);
        rootNode.setNodeKey("age");
        rootNode.setNodeType("DECISION");
        tree.addNode(rootNode);

        RuleNode leafNode = new RuleNode();
        leafNode.setNodeId(30002L);
        leafNode.setTreeId(3001L);
        leafNode.setNodeType("LEAF");
        leafNode.setNodeValue("2001");
        tree.addNode(leafNode);

        RuleNodeLine line = new RuleNodeLine();
        line.setLineId(40001L);
        line.setTreeId(3001L);
        line.setNodeIdFrom(30001L);
        line.setNodeIdTo(30002L);
        line.setRuleLimitType("LE");
        line.setRuleLimitValue("30");
        tree.addLine(line);

        return tree;
    }

    private RuleTree createEqualConditionTree() {
        RuleTree tree = new RuleTree();
        tree.setTreeId(3001L);
        tree.setRootNodeId(30001L);

        RuleNode rootNode = new RuleNode();
        rootNode.setNodeId(30001L);
        rootNode.setNodeKey("gender");
        rootNode.setNodeType("DECISION");
        tree.addNode(rootNode);

        RuleNode leafNode = new RuleNode();
        leafNode.setNodeId(30002L);
        leafNode.setNodeType("LEAF");
        leafNode.setNodeValue("campaign_001");
        tree.addNode(leafNode);

        RuleNodeLine line = new RuleNodeLine();
        line.setNodeIdFrom(30001L);
        line.setNodeIdTo(30002L);
        line.setRuleLimitType("EQUAL");
        line.setRuleLimitValue("male");
        tree.addLine(line);

        return tree;
    }

    private RuleTree createGtConditionTree() {
        RuleTree tree = new RuleTree();
        tree.setTreeId(3001L);
        tree.setRootNodeId(30001L);

        RuleNode rootNode = new RuleNode();
        rootNode.setNodeId(30001L);
        rootNode.setNodeKey("score");
        rootNode.setNodeType("DECISION");
        tree.addNode(rootNode);

        RuleNode leafNode = new RuleNode();
        leafNode.setNodeId(30002L);
        leafNode.setNodeType("LEAF");
        leafNode.setNodeValue("high_score_campaign");
        tree.addNode(leafNode);

        RuleNodeLine line = new RuleNodeLine();
        line.setNodeIdFrom(30001L);
        line.setNodeIdTo(30002L);
        line.setRuleLimitType("GT");
        line.setRuleLimitValue("60");
        tree.addLine(line);

        return tree;
    }

    private RuleTree createEnumConditionTree() {
        RuleTree tree = new RuleTree();
        tree.setTreeId(3001L);
        tree.setRootNodeId(30001L);

        RuleNode rootNode = new RuleNode();
        rootNode.setNodeId(30001L);
        rootNode.setNodeKey("city");
        rootNode.setNodeType("DECISION");
        tree.addNode(rootNode);

        RuleNode leafNode = new RuleNode();
        leafNode.setNodeId(30002L);
        leafNode.setNodeType("LEAF");
        leafNode.setNodeValue("city_campaign");
        tree.addNode(leafNode);

        RuleNodeLine line = new RuleNodeLine();
        line.setNodeIdFrom(30001L);
        line.setNodeIdTo(30002L);
        line.setRuleLimitType("ENUM");
        line.setRuleLimitValue("beijing,shanghai,guangzhou");
        tree.addLine(line);

        return tree;
    }
}
