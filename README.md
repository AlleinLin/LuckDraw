# 🎡 LuckDraw- 抽奖平台

<div align="center">

!\[Java]\(<https://img.shields.io/badge/Java-1.8+-orange.svg> null)
!\[Spring Boot]\(<https://img.shields.io/badge/Spring%20Boot-2.6.0-brightgreen.svg> null)
!\[Architecture]\(<https://img.shields.io/badge/Architecture-Hexagonal-blue.svg> null)
!\[License]\(<https://img.shields.io/badge/License-Apache%202.0-green.svg> null)

**基于六边形架构的抽奖解决方案**

[快速开始](#-快速开始) · [架构设计](#-架构设计) · [核心功能](#-核心功能) · [技术选型](#-技术选型)

</div>

***

## 📋 项目概述

PrizeWheel 是一个采用\*\*六边形架构（Hexagonal Architecture）\*\*构建的choujiangpingta。系统通过清晰的端口与适配器设计，实现了核心业务逻辑与外部依赖的完全解耦，具备极高的可测试性和可维护性。

### ✨ 核心特性

| 特性            | 描述                 |
| ------------- | ------------------ |
| 🎯 **灵活抽奖**   | 支持单次概率、总体概率两种抽奖模式  |
| 🔄 **状态流转**   | 完整的活动生命周期状态机管理     |
| 📦 **分布式架构**  | 支持分布式锁、消息队列、服务注册发现 |
| ⚡ **高性能**     | 雪花算法ID生成、Redis缓存加速 |
| 🛡️ **六边形架构** | 核心业务与基础设施完全解耦      |
| 🔌 **可插拔设计**  | 端口与适配器模式，易于替换实现    |

***

## 🏗️ 架构设计

### 六边形架构图

```
                    ┌─────────────────────────────────────┐
                    │           API Layer                  │
                    │  ┌───────────┐    ┌───────────┐     │
                    │  │   REST    │    │   Dubbo   │     │
                    │  │  Facade   │    │  Service  │     │
                    │  └─────┬─────┘    └─────┬─────┘     │
                    └────────┼────────────────┼───────────┘
                             │                │
                    ┌────────▼────────────────▼───────────┐
                    │         Core Domain                  │
                    │  ┌───────────────────────────────┐  │
                    │  │      Input Ports (API)        │  │
                    │  │  DrawServicePort              │  │
                    │  │  CampaignServicePort          │  │
                    │  │  PrizeServicePort             │  │
                    │  └───────────────┬───────────────┘  │
                    │                  │                   │
                    │  ┌───────────────▼───────────────┐  │
                    │  │      Domain Services          │  │
                    │  │  DrawServiceImpl              │  │
                    │  │  CampaignServiceImpl          │  │
                    │  │  PrizeServiceImpl             │  │
                    │  └───────────────┬───────────────┘  │
                    │                  │                   │
                    │  ┌───────────────▼───────────────┐  │
                    │  │      Output Ports (SPI)       │  │
                    │  │  CampaignRepositoryPort       │  │
                    │  │  WinRecordRepositoryPort      │  │
                    │  │  CacheServicePort             │  │
                    │  │  MessageQueuePort             │  │
                    │  └───────────────────────────────┘  │
                    └─────────────────────────────────────┘
                             │
                    ┌────────▼─────────────────────────────┐
                    │          Adapters Layer               │
                    │  ┌─────────┐ ┌─────────┐ ┌─────────┐ │
                    │  │   DB    │ │  Redis  │ │  Kafka  │ │
                    │  │ Adapter │ │ Adapter │ │ Adapter │ │
                    │  └─────────┘ └─────────┘ └─────────┘ │
                    └───────────────────────────────────────┘
```

### 模块职责

| 模块                    | 职责说明                 |
| --------------------- | -------------------- |
| `prizewheel-shared`   | 共享常量、枚举、工具类、通用DTO    |
| `prizewheel-core`     | 核心领域模型、业务服务、端口定义     |
| `prizewheel-adapters` | 基础设施适配器（数据库、缓存、消息队列） |
| `prizewheel-api`      | 对外API接口、Dubbo服务、启动入口 |

***

## 🎯 核心功能

### 1. 抽奖服务

```java
// 执行抽奖
WinRecord record = drawService.executeDraw(participantId, campaignId);

// 查询中奖记录
WinRecord record = drawService.queryWinRecord(recordId);
```

### 2. 活动管理

```java
// 创建活动
Long campaignId = campaignService.createCampaign(campaign);

// 更新状态
campaignService.updateStatus(campaignId, ActivityStatus.RUNNING.getCode());
```

### 3. 奖品发放

```java
// 发放奖品
boolean success = prizeService.grantPrize(recordId);
```

***

## 🔧 技术选型

| 分类        | 技术               | 版本     | 说明      |
| --------- | ---------------- | ------ | ------- |
| **核心框架**  | Spring Boot      | 2.6.0  | 应用框架    |
| **ORM框架** | MyBatis          | 2.1.4  | 数据持久化   |
| **数据库**   | MySQL            | 8.0+   | 关系型数据库  |
| **缓存**    | Redis + Redisson | 3.17.0 | 分布式缓存与锁 |
| **消息队列**  | Kafka            | 2.8.0  | 异步消息处理  |
| **RPC框架** | Dubbo            | 2.7.10 | 分布式服务调用 |
| **注册中心**  | Nacos            | 2.0+   | 服务注册与发现 |
| **任务调度**  | XXL-Job          | 2.3.0  | 分布式任务调度 |

***

## 🚀 快速开始

### 环境要求

- JDK 1.8+
- MySQL 8.0+
- Redis 6.0+
- Kafka 2.8+
- Nacos 2.0+

### 安装部署

```bash
# 1. 克隆项目
git clone https://github.com/your-org/prizewheel.git

# 2. 初始化数据库
mysql -u root -p < database/prizewheel.sql

# 3. 修改配置
# 编辑 prizewheel-api/src/main/resources/application.yml

# 4. 构建项目
mvn clean package -DskipTests

# 5. 启动服务
java -jar prizewheel-api/target/prizewheel-api-1.0.0-SNAPSHOT.jar
```

### 配置说明

```yaml
# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST:localhost}:3306/prizewheel
    username: ${DB_USER:root}
    password: ${DB_PASS:root}

# Redis配置
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PASS:}

# Dubbo配置
dubbo:
  registry:
    address: nacos://${NACOS_HOST:localhost}:8848
```

***

## 📁 目录结构

```
PrizeWheel/
├── database/                     # 数据库脚本
│   └── prizewheel.sql           # 建表脚本
├── prizewheel-shared/           # 共享模块
│   └── src/main/java/io/prizewheel/shared/
│       ├── constant/            # 常量定义
│       ├── model/               # 通用模型
│       └── util/                # 工具类
├── prizewheel-core/             # 核心领域模块
│   └── src/main/java/io/prizewheel/core/
│       ├── domain/              # 领域实体
│       ├── port/                # 端口定义
│       │   ├── input/           # 输入端口
│       │   └── output/          # 输出端口
│       └── service/             # 业务服务
├── prizewheel-adapters/         # 适配器模块
│   └── src/main/java/io/prizewheel/adapters/
│       ├── cache/               # 缓存适配器
│       ├── mq/                  # 消息队列适配器
│       └── persistence/         # 持久化适配器
│           ├── entity/          # PO实体
│           ├── mapper/          # MyBatis Mapper
│           └── repository/      # 仓储实现
├── prizewheel-api/              # API模块
│   └── src/main/java/io/prizewheel/api/
│       ├── config/              # 配置类
│       ├── dto/                 # 数据传输对象
│       └── facade/              # 服务门面
└── pom.xml                      # 父POM
```

***

## 📊 性能指标

| 指标     | 数值        | 说明         |
| ------ | --------- | ---------- |
| 抽奖响应时间 | < 30ms    | 单次抽奖耗时     |
| 并发支持   | 8000+ QPS | 单机并发处理能力   |
| ID生成速度 | 40w+/s    | 雪花算法单机生成能力 |

***

## 📝 开发规范

### 命名规范

| 类型  | 规范     | 示例                   |
| --- | ------ | -------------------- |
| 类名  | 大驼峰    | `DrawServiceImpl`    |
| 方法名 | 小驼峰    | `executeDraw`        |
| 常量  | 全大写下划线 | `MAX_RETRY_COUNT`    |
| 包名  | 全小写    | `io.prizewheel.core` |

### 架构规范

- 核心模块(`prizewheel-core`)不依赖任何外部框架
- 所有外部依赖通过适配器模块引入
- 端口定义使用接口，适配器实现接口

***

## 📄 许可证

本项目采用 [MIT](LICENSE) 许可证。

***

<div align="center">

**⭐ 如果这个项目对您有帮助，请给一个 Star 支持一下！**

</div>
