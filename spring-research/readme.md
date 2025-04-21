
## 项目说明


## 使用说明

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