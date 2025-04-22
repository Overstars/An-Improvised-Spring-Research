
## 项目说明


## 使用说明
打包：执行Gradle bootJar任务，便会在build/libs/下生成jar包
```shell
./gradlew clean bootJar --info
```


部署运行
```shell
nohup java -jar spring-research-0.0.1-SNAPSHOT.jar \
-Dspring.profiles.active=main \
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

```
┌─────────────┐       ┌───────────────┐       ┌──────────┐
│ GitHub Pages│       │ Spring Boot   │       │ LevelDB  │
│ (Hexo Blog) ├──────►│ API Service   ├──────►│ Embedded │
└──────┬──────┘       └───────┬───────┘       └──────────┘
       │           AJAX       │
       └──────────────────────┘
```