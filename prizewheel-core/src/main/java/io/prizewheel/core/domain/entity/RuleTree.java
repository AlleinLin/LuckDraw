package io.prizewheel.core.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 规则树实体
 * 
 * @author Allein
 * @since 2.0.0
 */
public class RuleTree implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long treeId;
    private String treeName;
    private String treeDesc;
    private Long rootNodeId;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RuleNode> nodes = new ArrayList<>();
    private List<RuleNodeLine> lines = new ArrayList<>();

    public void addNode(RuleNode node) {
        this.nodes.add(node);
    }

    public void addLine(RuleNodeLine line) {
        this.lines.add(line);
    }

    public RuleNode getRootNode() {
        if (nodes == null || nodes.isEmpty()) {
            return null;
        }
        return nodes.stream()
                .filter(n -> n.getNodeId().equals(rootNodeId))
                .findFirst()
                .orElse(null);
    }

    public RuleNode getNodeById(Long nodeId) {
        return nodes.stream()
                .filter(n -> n.getNodeId().equals(nodeId))
                .findFirst()
                .orElse(null);
    }

    public List<RuleNodeLine> getLinesFromNode(Long nodeId) {
        List<RuleNodeLine> result = new ArrayList<>();
        for (RuleNodeLine line : lines) {
            if (line.getNodeIdFrom().equals(nodeId)) {
                result.add(line);
            }
        }
        return result;
    }

    public Long getTreeId() {
        return treeId;
    }

    public void setTreeId(Long treeId) {
        this.treeId = treeId;
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    public String getTreeDesc() {
        return treeDesc;
    }

    public void setTreeDesc(String treeDesc) {
        this.treeDesc = treeDesc;
    }

    public Long getRootNodeId() {
        return rootNodeId;
    }

    public void setRootNodeId(Long rootNodeId) {
        this.rootNodeId = rootNodeId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<RuleNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<RuleNode> nodes) {
        this.nodes = nodes;
    }

    public List<RuleNodeLine> getLines() {
        return lines;
    }

    public void setLines(List<RuleNodeLine> lines) {
        this.lines = lines;
    }
}
