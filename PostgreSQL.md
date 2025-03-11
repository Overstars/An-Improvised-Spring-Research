---
layout: title
title: "PostgreSQL使用"
date: 2023-12-06 13:44:04
categories: 
- 工具
- PostgreSQL
tags:
- 工作
- PostgreSQL
mathjax: true
highlight: true
top: false
hidden: false
published: true
---

### 安装后测试连接TCP/IP connections失败解决方法

```
已失败
DBMS: PostgreSQL (版本 12.16 (Ubuntu 12.16-0ubuntu0.20.04.1))  区分大小写: 普通形式=lower，分隔形式=exact 驱动程序: PostgreSQL JDBC Driver (版本 42.6.0，JDBC4.2)
 Connection to xxx.xxx.xxx.xxx:5432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.
```

TCP/IP连接建立失败，宿主机并没有监听其他的主机接口。

按如下操作进行设置：

```shell
vim /etc/postgresql/12/main/postgresql.conf
```

对配置文件进行编辑

```
 58 # - Connection Settings -
 59
 60 listen_addresses = '*'      # what IP address(es) to listen on;
 61                     # comma-separated list of addresses;
 62                     # defaults to 'localhost'; use '*' for all
 63                     # (change requires restart)
 64 port = 5432             # (change requires restart)
 65 max_connections = 100           # (change requires restart)
 66 #superuser_reserved_connections = 3 # (change requires restart)
```

修改 允许访问配置pg_hba.conf

```shell
vim /etc/postgresql/12/main/pg_hba.conf
添加
host    all             postgres        0.0.0.0/0               md5
```

可能是PostgreSQL认为“postgres”不是“all”用户，所以USER里不应该填all……重启

```
systemctl restart postgresql
```

检查

```
netstat -lantp #查看端口是否启动
ps aux | grep postgres #查看数据库服务是否启动
```

测试链接

```
已成功
  复制
DBMS: PostgreSQL (版本 12.16 (Ubuntu 12.16-0ubuntu0.20.04.1))  区分大小写: 普通形式=lower，分隔形式=exact 驱动程序: PostgreSQL JDBC Driver (版本 42.6.0，JDBC4.2)  Ping: 14毫秒 SSL: yes
```

