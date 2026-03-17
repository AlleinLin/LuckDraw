/*
 PrizeWheel Database Schema
 Version: 1.0.0
 Author: PrizeWheel Team
*/

CREATE DATABASE IF NOT EXISTS prizewheel DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE prizewheel;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table: pw_campaign (活动表)
-- ----------------------------
DROP TABLE IF EXISTS `pw_campaign`;
CREATE TABLE `pw_campaign` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `campaign_id` BIGINT NOT NULL COMMENT '活动ID',
    `campaign_name` VARCHAR(128) NOT NULL COMMENT '活动名称',
    `campaign_desc` VARCHAR(512) DEFAULT NULL COMMENT '活动描述',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',
    `total_stock` INT NOT NULL DEFAULT 0 COMMENT '总库存',
    `remaining_stock` INT NOT NULL DEFAULT 0 COMMENT '剩余库存',
    `max_participations` INT NOT NULL DEFAULT 1 COMMENT '每人最大参与次数',
    `policy_id` BIGINT DEFAULT NULL COMMENT '策略ID',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-草稿 2-待审核 3-已驳回 4-已通过 5-进行中 6-已暂停 7-已结束',
    `creator` VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_campaign_id` (`campaign_id`),
    KEY `idx_status` (`status`),
    KEY `idx_time_range` (`start_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动表';

-- ----------------------------
-- Table: pw_draw_policy (抽奖策略表)
-- ----------------------------
DROP TABLE IF EXISTS `pw_draw_policy`;
CREATE TABLE `pw_draw_policy` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `policy_id` BIGINT NOT NULL COMMENT '策略ID',
    `policy_name` VARCHAR(128) NOT NULL COMMENT '策略名称',
    `draw_mode` TINYINT NOT NULL DEFAULT 1 COMMENT '抽奖模式: 1-单次概率 2-总体概率',
    `grant_type` TINYINT NOT NULL DEFAULT 1 COMMENT '发奖类型: 1-即时 2-定时 3-人工',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_policy_id` (`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽奖策略表';

-- ----------------------------
-- Table: pw_policy_prize (策略奖品配置表)
-- ----------------------------
DROP TABLE IF EXISTS `pw_policy_prize`;
CREATE TABLE `pw_policy_prize` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `policy_id` BIGINT NOT NULL COMMENT '策略ID',
    `prize_id` VARCHAR(64) NOT NULL COMMENT '奖品ID',
    `prize_name` VARCHAR(128) NOT NULL COMMENT '奖品名称',
    `total_quantity` INT NOT NULL DEFAULT 0 COMMENT '总数量',
    `remaining_quantity` INT NOT NULL DEFAULT 0 COMMENT '剩余数量',
    `win_rate` DECIMAL(10,4) NOT NULL COMMENT '中奖概率(百分比)',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_policy_id` (`policy_id`),
    KEY `idx_prize_id` (`prize_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='策略奖品配置表';

-- ----------------------------
-- Table: pw_win_record (中奖记录表)
-- ----------------------------
DROP TABLE IF EXISTS `pw_win_record`;
CREATE TABLE `pw_win_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `record_id` BIGINT NOT NULL COMMENT '记录ID',
    `participant_id` VARCHAR(64) NOT NULL COMMENT '参与者ID',
    `campaign_id` BIGINT NOT NULL COMMENT '活动ID',
    `policy_id` BIGINT NOT NULL COMMENT '策略ID',
    `prize_id` VARCHAR(64) DEFAULT NULL COMMENT '奖品ID',
    `prize_name` VARCHAR(128) DEFAULT NULL COMMENT '奖品名称',
    `prize_type` TINYINT DEFAULT NULL COMMENT '奖品类型',
    `prize_content` VARCHAR(512) DEFAULT NULL COMMENT '奖品内容',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-未中奖 1-待发放 2-已发放',
    `win_time` DATETIME NOT NULL COMMENT '中奖时间',
    `grant_time` DATETIME DEFAULT NULL COMMENT '发放时间',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_record_id` (`record_id`),
    KEY `idx_participant_id` (`participant_id`),
    KEY `idx_campaign_id` (`campaign_id`),
    KEY `idx_win_time` (`win_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='中奖记录表';

-- ----------------------------
-- Table: pw_prize (奖品表)
-- ----------------------------
DROP TABLE IF EXISTS `pw_prize`;
CREATE TABLE `pw_prize` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `prize_id` VARCHAR(64) NOT NULL COMMENT '奖品ID',
    `prize_type` TINYINT NOT NULL COMMENT '奖品类型: 1-虚拟 2-优惠券 3-实物 4-积分 5-现金',
    `prize_name` VARCHAR(128) NOT NULL COMMENT '奖品名称',
    `prize_content` VARCHAR(512) DEFAULT NULL COMMENT '奖品内容',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_prize_id` (`prize_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='奖品表';

-- ----------------------------
-- Table: pw_user_points (用户积分表)
-- ----------------------------
DROP TABLE IF EXISTS `pw_user_points`;
CREATE TABLE `pw_user_points` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` VARCHAR(64) NOT NULL COMMENT '用户ID',
    `total_points` INT NOT NULL DEFAULT 0 COMMENT '累计积分',
    `used_points` INT NOT NULL DEFAULT 0 COMMENT '已使用积分',
    `current_points` INT NOT NULL DEFAULT 0 COMMENT '当前积分',
    `user_level` TINYINT NOT NULL DEFAULT 1 COMMENT '用户等级: 1-新手 2-初级 3-中级 4-高级 5-专家 6-大师 7-宗师 8-传奇',
    `last_sign_in_time` DATETIME DEFAULT NULL COMMENT '最后签到时间',
    `continuous_sign_in_days` INT NOT NULL DEFAULT 0 COMMENT '连续签到天数',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户积分表';

-- ----------------------------
-- Table: pw_points_record (积分记录表)
-- ----------------------------
DROP TABLE IF EXISTS `pw_points_record`;
CREATE TABLE `pw_points_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `record_id` BIGINT NOT NULL COMMENT '记录ID',
    `user_id` VARCHAR(64) NOT NULL COMMENT '用户ID',
    `points` INT NOT NULL COMMENT '积分数量',
    `type` TINYINT NOT NULL COMMENT '类型: 1-增加 2-扣减',
    `source` VARCHAR(32) NOT NULL COMMENT '来源: 1-签到 2-抽奖 3-兑换 4-管理员 5-活动',
    `remark` VARCHAR(256) DEFAULT NULL COMMENT '备注',
    `related_id` BIGINT DEFAULT NULL COMMENT '关联ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_record_id` (`record_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分记录表';

-- ----------------------------
-- Table: pw_rule_tree (规则树表)
-- ----------------------------
DROP TABLE IF EXISTS `pw_rule_tree`;
CREATE TABLE `pw_rule_tree` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tree_id` BIGINT NOT NULL COMMENT '规则树ID',
    `tree_name` VARCHAR(128) NOT NULL COMMENT '规则树名称',
    `tree_desc` VARCHAR(512) DEFAULT NULL COMMENT '规则树描述',
    `root_node_id` BIGINT NOT NULL COMMENT '根节点ID',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tree_id` (`tree_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='规则树表';

-- ----------------------------
-- Table: pw_rule_node (规则节点表)
-- ----------------------------
DROP TABLE IF EXISTS `pw_rule_node`;
CREATE TABLE `pw_rule_node` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `node_id` BIGINT NOT NULL COMMENT '节点ID',
    `tree_id` BIGINT NOT NULL COMMENT '规则树ID',
    `node_key` VARCHAR(64) NOT NULL COMMENT '节点Key',
    `node_desc` VARCHAR(256) DEFAULT NULL COMMENT '节点描述',
    `node_type` VARCHAR(32) NOT NULL COMMENT '节点类型: LEAF-叶子节点 DECISION-决策节点',
    `node_value` VARCHAR(256) DEFAULT NULL COMMENT '节点值(叶子节点存储结果)',
    `rule_limit_type` VARCHAR(32) DEFAULT NULL COMMENT '规则限定类型: EQUAL/GT/LT/GE/LE/ENUM',
    `rule_limit_value` VARCHAR(256) DEFAULT NULL COMMENT '规则限定值',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_node_id` (`node_id`),
    KEY `idx_tree_id` (`tree_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='规则节点表';

-- ----------------------------
-- Table: pw_rule_node_line (规则节点连线表)
-- ----------------------------
DROP TABLE IF EXISTS `pw_rule_node_line`;
CREATE TABLE `pw_rule_node_line` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `line_id` BIGINT NOT NULL COMMENT '连线ID',
    `tree_id` BIGINT NOT NULL COMMENT '规则树ID',
    `node_id_from` BIGINT NOT NULL COMMENT '起始节点ID',
    `node_id_to` BIGINT NOT NULL COMMENT '目标节点ID',
    `rule_limit_type` VARCHAR(32) NOT NULL COMMENT '规则限定类型',
    `rule_limit_value` VARCHAR(256) NOT NULL COMMENT '规则限定值',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_line_id` (`line_id`),
    KEY `idx_tree_id` (`tree_id`),
    KEY `idx_node_from` (`node_id_from`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='规则节点连线表';

-- ----------------------------
-- Table: pw_statistics (统计表)
-- ----------------------------
DROP TABLE IF EXISTS `pw_statistics`;
CREATE TABLE `pw_statistics` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `dimension_type` VARCHAR(32) NOT NULL COMMENT '维度类型: CAMPAIGN/USER/PRIZE/DAILY',
    `dimension_value` VARCHAR(128) NOT NULL COMMENT '维度值',
    `campaign_id` BIGINT DEFAULT NULL COMMENT '活动ID',
    `prize_id` VARCHAR(64) DEFAULT NULL COMMENT '奖品ID',
    `total_count` INT NOT NULL DEFAULT 0 COMMENT '总次数',
    `win_count` INT NOT NULL DEFAULT 0 COMMENT '中奖次数',
    `grant_count` INT NOT NULL DEFAULT 0 COMMENT '发放次数',
    `stat_date` DATE DEFAULT NULL COMMENT '统计日期',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dimension_date` (`dimension_type`, `dimension_value`, `campaign_id`, `stat_date`),
    KEY `idx_campaign_id` (`campaign_id`),
    KEY `idx_stat_date` (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='统计表';

-- ----------------------------
-- Table: pw_campaign_template (活动模板表)
-- ----------------------------
DROP TABLE IF EXISTS `pw_campaign_template`;
CREATE TABLE `pw_campaign_template` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `template_id` BIGINT NOT NULL COMMENT '模板ID',
    `template_name` VARCHAR(128) NOT NULL COMMENT '模板名称',
    `template_desc` VARCHAR(512) DEFAULT NULL COMMENT '模板描述',
    `default_duration_days` INT NOT NULL DEFAULT 7 COMMENT '默认持续天数',
    `default_total_stock` INT NOT NULL DEFAULT 1000 COMMENT '默认总库存',
    `default_max_participations` INT NOT NULL DEFAULT 1 COMMENT '默认最大参与次数',
    `draw_mode` TINYINT NOT NULL DEFAULT 1 COMMENT '抽奖模式',
    `grant_type` TINYINT NOT NULL DEFAULT 1 COMMENT '发奖类型',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
    `creator` VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    `use_count` INT NOT NULL DEFAULT 0 COMMENT '使用次数',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_template_id` (`template_id`),
    KEY `idx_creator` (`creator`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动模板表';

-- ----------------------------
-- Table: pw_template_prize (模板奖品配置表)
-- ----------------------------
DROP TABLE IF EXISTS `pw_template_prize`;
CREATE TABLE `pw_template_prize` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `config_id` BIGINT NOT NULL COMMENT '配置ID',
    `template_id` BIGINT NOT NULL COMMENT '模板ID',
    `prize_id` VARCHAR(64) NOT NULL COMMENT '奖品ID',
    `prize_name` VARCHAR(128) NOT NULL COMMENT '奖品名称',
    `prize_type` TINYINT NOT NULL COMMENT '奖品类型',
    `prize_content` VARCHAR(512) DEFAULT NULL COMMENT '奖品内容',
    `total_quantity` INT NOT NULL DEFAULT 0 COMMENT '总数量',
    `win_rate` DECIMAL(10,4) NOT NULL COMMENT '中奖概率',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_template_id` (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板奖品配置表';

-- ----------------------------
-- Insert Demo Data
-- ----------------------------
INSERT INTO `pw_draw_policy` (`policy_id`, `policy_name`, `draw_mode`, `grant_type`) VALUES
(1001, '新年抽奖策略', 1, 1),
(1002, '周年庆抽奖策略', 2, 1);

INSERT INTO `pw_prize` (`prize_id`, `prize_type`, `prize_name`, `prize_content`) VALUES
('P001', 1, '一等奖', 'iPhone 15 Pro'),
('P002', 1, '二等奖', 'AirPods Pro'),
('P003', 2, '三等奖', '100元优惠券'),
('P004', 4, '四等奖', '100积分'),
('P005', 1, '参与奖', '谢谢参与');

INSERT INTO `pw_policy_prize` (`policy_id`, `prize_id`, `prize_name`, `total_quantity`, `remaining_quantity`, `win_rate`) VALUES
(1001, 'P001', '一等奖', 10, 10, 1.0000),
(1001, 'P002', '二等奖', 50, 50, 5.0000),
(1001, 'P003', '三等奖', 100, 100, 10.0000),
(1001, 'P004', '四等奖', 500, 500, 50.0000),
(1001, 'P005', '参与奖', 10000, 10000, 34.0000);

INSERT INTO `pw_campaign` (`campaign_id`, `campaign_name`, `campaign_desc`, `start_time`, `end_time`, `total_stock`, `remaining_stock`, `max_participations`, `policy_id`, `status`, `creator`) VALUES
(2001, '新年抽奖活动', '新年特惠抽奖活动，豪礼等你拿！', '2024-01-01 00:00:00', '2024-12-31 23:59:59', 10000, 10000, 3, 1001, 5, 'admin');

INSERT INTO `pw_rule_tree` (`tree_id`, `tree_name`, `tree_desc`, `root_node_id`, `status`) VALUES
(3001, '用户年龄规则树', '根据用户年龄决策活动', 30001, 1);

INSERT INTO `pw_rule_node` (`node_id`, `tree_id`, `node_key`, `node_desc`, `node_type`, `node_value`, `rule_limit_type`, `rule_limit_value`, `status`) VALUES
(30001, 3001, 'age', '年龄判断', 'DECISION', NULL, NULL, NULL, 1),
(30002, 3001, 'age', '青年用户活动', 'LEAF', '2001', NULL, NULL, 1),
(30003, 3001, 'age', '中年用户活动', 'LEAF', '2002', NULL, NULL, 1),
(30004, 3001, 'age', '老年用户活动', 'LEAF', '2003', NULL, NULL, 1);

INSERT INTO `pw_rule_node_line` (`line_id`, `tree_id`, `node_id_from`, `node_id_to`, `rule_limit_type`, `rule_limit_value`) VALUES
(40001, 3001, 30001, 30002, 'LE', '30'),
(40002, 3001, 30001, 30003, 'GT', '30'),
(40003, 3001, 30001, 30003, 'LE', '50'),
(40004, 3001, 30001, 30004, 'GT', '50');

INSERT INTO `pw_campaign_template` (`template_id`, `template_name`, `template_desc`, `default_duration_days`, `default_total_stock`, `default_max_participations`, `draw_mode`, `grant_type`, `status`, `creator`, `use_count`) VALUES
(5001, '标准抽奖模板', '适用于日常抽奖活动', 7, 1000, 1, 1, 1, 1, 'admin', 0),
(5002, '大型活动模板', '适用于大型营销活动', 30, 10000, 3, 2, 1, 1, 'admin', 0);

INSERT INTO `pw_user_points` (`user_id`, `total_points`, `used_points`, `current_points`, `user_level`, `continuous_sign_in_days`) VALUES
('user001', 1500, 500, 1000, 4, 5),
('user002', 500, 100, 400, 3, 2);

SET FOREIGN_KEY_CHECKS = 1;
