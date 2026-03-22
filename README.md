# An-Improvised-Spring-Research

Java Spring 后台项目，主要提供博客图床服务，并包含 PostgreSQL 日志写入能力。

## 项目状态

本次已完成**仓库级结构重组**，目标是提升可维护性与后续博客业务扩展性，且**不改动对外 API 与当前功能表现**。

### 已执行变更

1. 文档归档：`PostgreSQL.md` -> `docs/postgresql.md`
2. 数据库脚本归档：`straycat_ddl/*` -> `db/ddl/*`
3. 新增结构位：`docs/`、`db/`、`scripts/`
4. 增强根 `.gitignore`：排除构建产物与运行时数据目录

## 当前推荐目录

```text
An-Improvised-Spring-Research/
├─ README.md
├─ docs/
│  ├─ architecture.md
│  ├─ deployment.md
│  └─ postgresql.md
├─ db/
│  └─ ddl/
│     ├─ DDL_CREATE_DATEBASE_STRAYCAT.sql
│     ├─ DDL_STRAYCAT_USER_INFO.sql
│     ├─ DDL_STRAYCAT_VISIT_LOG.sql
│     ├─ DDL_TRACE_MESSAGE.sql
│     └─ DDL_TRANCODE_INFO.sql
├─ scripts/
│  ├─ run-dev.ps1
│  └─ run-prd.sh
├─ spring-research/
│  ├─ build.gradle
│  ├─ settings.gradle
│  ├─ gradlew
│  └─ src/
└─ .gitignore
```

## 架构说明入口

- 新架构详细说明：`docs/architecture.md`
- 构建与部署说明：`docs/deployment.md`
- PostgreSQL 运维记录：`docs/postgresql.md`

## 构建与运行

在 `spring-research` 目录执行：

```bash
./gradlew clean bootJar --info
```

运行示例：

```bash
java -jar build/libs/spring-research-0.0.1-SNAPSHOT.jar \
  -Dspring.profiles.active=prd \
  -Dserver.port=9003 \
  -Dfile.encoding=UTF-8
```

## 技术栈

- Java 11
- Spring Boot 2.7.12
- Gradle（Wrapper）
- PostgreSQL
- MyBatis + Spring Data JPA（并存）
- LevelDB（历史方案，建议逐步退出主路径）
