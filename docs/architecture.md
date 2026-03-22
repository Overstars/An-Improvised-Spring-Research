# 架构设计说明（博客图床与日志服务）

本文档描述本项目在“对外 API 不变”的前提下，推荐的可维护、可扩展架构。

## 1. 设计目标

1. 对外接口与行为保持稳定（路径、请求方式、响应结构不变）。
2. 目录与职责清晰，降低协作和排错成本。
3. 支持博客业务扩展（图片处理、访问分析、审计、限流）。
4. 为后续技术演进预留空间（对象存储、消息队列、数据库迁移工具）。

## 2. 约束与原则

- **稳定优先**：先做仓库级和组织级重构，再做内部包重构。
- **职责单一**：Controller 只做请求编排，Service 只做业务逻辑，Repository/Mapper 只做数据访问。
- **向后兼容**：业务能力扩展采用“新增实现、保留入口”的方式。
- **配置外置**：运行参数走 `application-*.properties` 和启动参数，不在代码中写死。

## 3. 推荐逻辑分层

```text
接口层（controller）
  -> 应用服务层（service）
    -> 领域模型（model/entity/dto/vo）
      -> 数据访问层（repository/mapper）
        -> PostgreSQL / 其他存储
```

### 分层职责

- `controller`
  - 接收 HTTP 请求、参数校验、响应封装。
  - 不写持久化细节，不放复杂业务规则。

- `service`
  - 组织核心业务流程（上传、查询、日志写入、风控检查）。
  - 负责事务边界、幂等、重试策略。

- `repository` / `mapper`
  - JPA 统一放 `repository`。
  - MyBatis 统一放 `mapper`。
  - 避免 `dao/repository/mapper` 多名混用。

- `model`
  - `entity`：持久化模型。
  - `dto`：服务间数据传递。
  - `vo`：接口响应对象。

## 4. 推荐包组织（业务域聚合）

建议在 `spring-research/src/main/java/com/straycat` 逐步落地：

```text
com.straycat
├─ common/                   # 公共能力：异常、返回体、工具、常量
├─ config/                   # Spring 配置、拦截器、过滤器
├─ image/                    # 图床域
│  ├─ controller/
│  ├─ service/
│  ├─ repository/
│  ├─ mapper/
│  ├─ model/
│  └─ infra/
├─ visitlog/                 # 访问日志域
│  ├─ controller/
│  ├─ service/
│  ├─ repository/
│  └─ model/
└─ startup/                  # 启动初始化逻辑（可选）
```

## 5. 仓库级组织（已落地）

```text
docs/       # 说明文档
db/ddl/     # SQL DDL 脚本
scripts/    # 启动脚本
spring-research/  # 应用模块
```

### 合理性

1. 文档与代码分离，方便新人上手。
2. DDL 集中管理，方便数据库初始化与版本梳理。
3. 脚本归档，便于环境统一与自动化。
4. 构建产物/运行数据排除，避免仓库污染。

## 6. 对博客业务的扩展位

### 图床能力

- 图片元数据扩展：尺寸、格式、压缩率、来源博客标识。
- 上传链路扩展：本地存储 -> 对象存储（S3/OSS/Blob）通过 `infra` 适配器切换。
- 图片处理扩展：压缩、缩略图、水印、格式转换。

### 日志能力

- 访问日志分级：访问成功、异常、风控拦截。
- 异步化：日志写入可从同步改为消息队列消费（对外接口不变）。
- 可观测性：按接口、来源页面、UA、IP 维度聚合分析。

## 7. API 稳定策略（本次遵循）

1. 不改 URL 路径。
2. 不改请求参数名与字段语义。
3. 不改响应结构。
4. 不改启动入口与基础构建方式。

## 8. 推荐后续迭代（不影响现网）

1. 命名统一：`action -> controller`，`dao -> repository/mapper`。
2. 读写路径外置：将图片和日志本地路径从代码提取到配置。
3. 数据库版本化：引入 Flyway 或 Liquibase 管理 `db/migration`。
4. 业务域重组：按 `image` 和 `visitlog` 逐步搬迁类，分批验证。
