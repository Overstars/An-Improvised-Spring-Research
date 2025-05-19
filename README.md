# [An-Improvised-Spring-Research](https://github.com/Overstars/An-Improvised-Spring-Research)
关于spring boot的实践的科研。

先声明本人毫无Java基础，这就是一个一拍脑袋就新建的不知道什么时候跑路的垃圾项目，主要目的就是熟悉一下Spring项目结构。

### TODO

1. 接入[百度翻译接口](http://api.fanyi.baidu.com/doc/21)作为请求实验，可以用postman验证。
2. 写组装报文的服务类，和发送接收报文的服务类。
3. 对请求和返回的报文进行wsdl和xsd校验。
4. 添加对properties进行读取的api

### 依赖

|                | 版本                                                         | 下载链接                                                     | 说明     |
| -------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | -------- |
| IDEA           | IntelliJ IDEA 2023.1.2 (Ultimate Edition)<br>Build #IU-231.9011.34, built on May 16, 2023 |                                                              | 开发工具 |
| java           | openjdk-11.0.2_windows-x64_bin                               |                                                              |          |
| gradle         | gradle-6.9.3                                                 |                                                              |          |
| 消息队列中间件 | RocketMQ：5.0.0                                              | https://rocketmq.apache.org/zh/download                      | 待研究   |
| DAO            | mybatis：3.5.4                                               | https://mybatis.org/mybatis-3/zh/getting-started.html        |          |
| 数据库         | mysql-community-server-core_8.0.32-1ubuntu22.04_amd64.deb<br>psql (PostgreSQL) 12.16 (Ubuntu 12.16-0ubuntu0.20.04.1) | https://downloads.mysql.com/archives/get/p/23/file/mysql-community-server-core_8.0.32-1ubuntu22.04_amd64.deb | TODO     |
|                | redis                                                        |                                                              |          |
|                | Nginx                                                        |                                                              |          |
| 自动化运维     | Ansible：2.14.1                                              |                                                              | 可能会用 |
|                |                                                              |                                                              |          |

### 第三方库

|      |      |      |
| ---- | ---- | ---- |
|      |      |      |
|      |      |      |
|      |      |      |

## 项目配置IDEA

### build.gradle参数更改

```gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '2.7.12'
    id 'io.spring.dependency-management' version '1.0.15.RELEASE'
}

group = 'com.straycat'
version = '0.0.1-SNAPSHOT'

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
// 构建的时候添加到启动前任务中
task copyResources(type: Copy) {
    from 'src/main/resources'
    into 'build/classes/java/main'
}
// 添加自定义源文件目录，没什么用
sourceSets {
    main {
        java.srcDirs += ['src/main/resources']
    }
}
```

@SpringBootApplication注解的文件要扔到其他代码的上级或者同级，否则可能会无法成功运行。

### 关于Gradle构建时将config打包至指定位置

[Gradle构建jar包，将文件打包到lib目录外方便读取和下载](https://blog.csdn.net/w4187402/article/details/107060463)，不一定可行，可能是gradle版本问题。

### JVM参数

```
-Dspring.profiles.active=prd -Dserver.port=9003 -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError
```



### 常见中文乱码解析

![](https://img-blog.csdnimg.cn/20210323155253380.jpg)

| 名称 | 示例         |      | 产生原因                     | 解决方式                                                     |
| ---- | ------------ | ---- | ---------------------------- | ------------------------------------------------------------ |
|      | \u82f9\u679c |      | 使用ASCII编码读取unicode编码 | 通过工具函数将unicode转换为汉字                              |
|      | ���ر���      |      | UTF-8读取GB2312格式的编码    | 如果是idea的终端里的建议在idea64.exe.vmoptions加-Dfile.encoding=UTF-8<br>启动参数虚拟机参数添加-Dfile.encoding=UTF-8 |
|      |              |      |                              |                                                              |
|      |              |      |                              |                                                              |
|      |              |      |                              |                                                              |

### 函数注释生成

[IDEA中设置给每个方法加上注释的快捷键](https://blog.csdn.net/weixin_45089791/article/details/103722702)



## 数据库PostgreSQL

```
# 修改postgresql.conf文件
vim /etc/postgresql/12/main/postgresql.conf
# 修改pg_hba.conf文件
vim /etc/postgresql/12/main/pg_hba.conf
#重启服务
/etc/init.d/postgresql restart

# 查看数据库状态
sudo /etc/init.d/postgresql status
# 启动数据库
sudo /etc/init.d/postgresql start
# 停止数据库
sudo /etc/init.d/postgresql stop
# 重启数据库
sudo /etc/init.d/postgresql restart
# 完全卸载postgresql数据库
sudo service postgresql stop 
sudo apt-get --purge remove postgresql\* 
# 删除相关配置文件
sudo rm -r /etc/postgresql/
sudo rm -r /etc/postgresql-common/
sudo rm -r /var/lib/postgresql/
sudo userdel -r postgres
sudo groupdel postgres
```

## 部署

