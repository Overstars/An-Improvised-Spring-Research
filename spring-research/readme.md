
## 项目说明
本仓库commit message采用[gitmoji](https://gitmoji.dev/)进行记录。

## 使用说明
### 构建
项目`build.gradle`右键`Import Gradle Project`。

### 打包：
1. 进入spring-research目录下
2. 打开终端
3. 执行Gradle bootJar任务，便会在build/libs/下生成jar包
```shell
./gradlew clean bootJar --info
```


部署运行
```shell
nohup java -jar spring-research-0.0.1-SNAPSHOT.jar \
-Dspring.profiles.active=prd \
-Dserver.port=9003 \
-Dfile.encoding=UTF-8 \
-verbose:gc \
-XX:+PrintGCDetails > springboot.log 2>&1 &
```

查看运行信息
```
ps -ef | grep java
```

查看日志
```shell
tail -f springboot.log
```

停止
```shell
kill -9 [PID]
```
### 基于Spring Boot的博客图床方案

### 基于Spring Boot + LevelDB的博客访问日志记录系统设计方案

日志记录，已弃用LevelDB，改用PostgreSQL
```
┌─────────────┐       ┌───────────────┐       ┌──────────┐
│ GitHub Pages│       │ Spring Boot   │       │ LevelDB  │
│ (Hexo Blog) ├──────►│ API Service   ├──────►│ Embedded │
└──────┬──────┘       └───────┬───────┘       └──────────┘
       │           AJAX       │
       └──────────────────────┘
```


### 评分列表界面

数据暂时放在github，目前仅提供图片服务
```
前端展示层          后端服务层           数据存储层
(Hexo Blog)        (Spring Boot)      (PostgreSQL)
     │                   │                   │
     │  GET /books       │                   │
     │ ◄─────JSON─────┐  │                   │
     │                │  │  JPA/Hibernate    │
     │                └─►│ ────────────────► │
     ▼                   ▼                   ▼
 浏览器渲染           业务逻辑处理         书籍数据存储
```
