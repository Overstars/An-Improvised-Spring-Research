# 构建与部署说明

## 1. 本地构建

进入模块目录：

```bash
cd spring-research
./gradlew clean bootJar --info
```

产物位置：

```text
spring-research/build/libs/
```

## 2. 运行（生产配置示例）

```bash
java -jar spring-research-0.0.1-SNAPSHOT.jar \
  -Dspring.profiles.active=prd \
  -Dserver.port=9003 \
  -Dfile.encoding=UTF-8 \
  -verbose:gc \
  -XX:+PrintGCDetails
```

## 3. Linux 后台运行示例

```bash
nohup java -jar spring-research-0.0.1-SNAPSHOT.jar \
  -Dspring.profiles.active=prd \
  -Dserver.port=9003 \
  -Dfile.encoding=UTF-8 \
  -verbose:gc \
  -XX:+PrintGCDetails > springboot.log 2>&1 &
```

查看进程：

```bash
ps -ef | grep java
```

查看日志：

```bash
tail -f springboot.log
```

停止服务：

```bash
kill -9 [PID]
```

## 4. 配置文件

配置目录：

```text
spring-research/src/main/resources/config/
```

常用环境：

- `application-dev.properties`
- `application-prd.properties`

## 5. 运行数据目录建议

以下目录属于运行时数据，不建议纳入版本控制：

- `levelDbData/`
- `image-db/`
- `visitLog-db/`
- `uploaded-images/`

推荐将以上目录迁移到仓库外路径，通过配置项挂载。
