# 🍃 Chai Admin Service

<div align="center">

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6.4.2-brightgreen.svg)](https://spring.io/projects/spring-security)
[![MyBatis Plus](https://img.shields.io/badge/MyBatis%20Plus-3.5.12-blue.svg)](https://baomidou.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-42.7.5-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**基于 Spring Boot 3 + Spring Security + JWT 的现代化后台管理系统服务端**

[English](README_EN.md) | 简体中文

</div>

## 📖 项目简介

Chai Admin Service 是一个现代化的后台管理系统服务端，采用最新的 Spring Boot 3 技术栈构建，提供完整的用户认证、权限管理、系统管理等功能。项目采用模块化设计，代码结构清晰，易于扩展和维护。

### ✨ 核心特性

- 🚀 **现代化技术栈**: Spring Boot 3.4.2 + Spring Security 6.4.2 + JDK 21
- 🔐 **安全认证**: JWT Token 认证 + Redis 分布式会话管理
- 🏗️ **模块化架构**: 清晰的模块划分，便于维护和扩展
- 📊 **数据库支持**: PostgreSQL + MyBatis Plus + Druid 连接池
- 🛡️ **权限控制**: 基于 RBAC 的细粒度权限控制
- 📝 **API 文档**: 完整的 RESTful API 文档
- 🐳 **容器化**: 支持 Docker 部署
- 📈 **监控运维**: Druid 监控 + Spring Boot Actuator

## 🛠️ 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.4.2 | 基础框架 |
| Spring Security | 6.4.2 | 安全认证框架 |
| MyBatis Plus | 3.5.12 | 持久层框架 |
| PostgreSQL | 42.7.5 | 关系型数据库 |
| Redis | - | 缓存和会话存储 |
| Druid | 1.2.23 | 数据库连接池 |
| JWT | 0.12.6 | JSON Web Token |
| Hutool | 6.0.0-M22 | Java 工具类库 |
| JDK | 21+ | Java 开发环境 |
| Maven | 3.6+ | 项目构建工具 |

## 🏗️ 项目结构

```
chai-admin-service/
├── chai-admin-dependency/      # 依赖管理模块 (BOM)
│   └── pom.xml                 # 统一依赖版本管理
├── chai-admin-common/          # 公共模块
│   ├── src/main/java/org/shamee/common/
│   │   ├── config/            # 配置类
│   │   ├── constant/          # 常量定义
│   │   ├── entity/            # 基础实体
│   │   ├── request/           # 请求对象
│   │   ├── response/          # 响应对象
│   │   └── util/              # 工具类
│   └── pom.xml
├── chai-admin-system/          # 系统核心模块
│   ├── src/main/java/org/shamee/system/
│   │   ├── config/            # 配置类
│   │   ├── controller/        # 控制器
│   │   ├── entity/            # 实体类
│   │   ├── mapper/            # 数据访问层
│   │   ├── security/          # 安全相关
│   │   └── service/           # 业务逻辑层
│   └── pom.xml
├── chai-admin-launcher/        # 启动模块
│   ├── src/main/java/org/shamee/
│   │   └── Application.java   # 启动类
│   ├── src/main/resources/
│   │   ├── application.yml    # 主配置文件
│   │   ├── application-dev.yml # 开发环境配置
│   │   └── application-prod.yml # 生产环境配置
│   └── pom.xml
├── docker-compose.yml          # Docker Compose 配置
├── Dockerfile                  # Docker 镜像构建文件
├── API.md                      # API 文档
├── DEPLOYMENT.md               # 部署文档
└── pom.xml                     # 父级 POM
```

## 🚀 快速开始

### 环境要求

- **JDK**: 21 或更高版本
- **Maven**: 3.6.0 或更高版本
- **PostgreSQL**: 12 或更高版本
- **Redis**: 6 或更高版本

### 1. 克隆项目

```bash
git clone https://github.com/your-username/chai-admin-service.git
cd chai-admin-service
```

### 2. 数据库配置

#### 创建 PostgreSQL 数据库

```sql
CREATE DATABASE chai_admin;
```

#### 执行初始化脚本

```bash
# 如果有初始化脚本，执行以下命令
psql -U postgres -d chai_admin -f init.sql
```

### 3. 配置文件

修改 `chai-admin-launcher/src/main/resources/application-dev.yml`：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/chai_admin
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password
```

### 4. 编译和运行

```bash
# 编译项目
mvn clean compile

# 打包项目
mvn clean package -DskipTests

# 运行项目
mvn spring-boot:run -pl chai-admin-launcher

# 或者运行 JAR 包
java -jar chai-admin-launcher/target/chai-admin-launcher-1.0.0-SNAPSHOT.jar
```

### 5. 访问应用

- **应用地址**: http://localhost:8000
- **API 文档**: 查看 [API.md](API.md)
- **Druid 监控**: http://localhost:8000/druid (用户名: admin, 密码: 123456)
- **健康检查**: http://localhost:8000/actuator/health

## 🔑 默认账户

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| admin | 123456 | 超级管理员 | 拥有所有权限 |
| test | 123456 | 普通用户 | 基础查询权限 |

## 📚 功能模块

### 🔐 认证授权
- JWT Token 认证
- 用户登录/登出
- 令牌刷新
- 分布式会话管理
- 登录失败锁定机制

### 👤 用户管理
- 用户增删改查
- 密码重置
- 用户状态管理
- 用户角色分配

### 🔒 角色权限
- 角色管理
- 权限分配
- 数据权限范围控制
- 菜单权限控制

### 📋 系统管理
- 菜单管理
- 部门管理
- 操作日志
- 登录日志

### 📊 系统监控
- Druid 数据库监控
- 系统健康检查
- JVM 监控
- 缓存监控

## 🐳 Docker 部署

### 快速部署

**方式一：使用部署脚本（推荐）**

```bash
# Linux/macOS
chmod +x docker-deploy.sh
./docker-deploy.sh deploy

# Windows
docker-deploy.bat deploy
```

**方式二：使用 Docker Compose**

```bash
# 1. 复制环境变量配置
cp .env.example .env

# 2. 编辑配置文件（修改密码和密钥）
vim .env

# 3. 构建并启动所有服务
docker-compose up -d --build

# 4. 查看服务状态
docker-compose ps
```

### 服务管理

```bash
# 查看日志
./docker-deploy.sh logs

# 检查服务状态
./docker-deploy.sh status

# 重启服务
./docker-deploy.sh restart

# 停止服务
./docker-deploy.sh stop
```

### 访问地址

部署成功后，可以访问：

- **应用主页**: http://localhost:8000
- **健康检查**: http://localhost:8000/actuator/health
- **Druid 监控**: http://localhost:8000/druid (admin/123456)

> 📖 详细的 Docker 部署指南请参考：[DOCKER_GUIDE.md](DOCKER_GUIDE.md)

## 📖 API 文档

详细的 API 文档请查看 [API.md](API.md)，包含：

- 认证接口
- 用户管理接口
- 角色管理接口
- 菜单管理接口
- 部门管理接口
- 系统监控接口

### 快速测试

```bash
# 登录获取 Token
curl -X POST http://localhost:8000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'

# 获取用户信息
curl -X GET http://localhost:8000/api/auth/me \
  -H "Authorization: Bearer <your-token>"
```

## ⚙️ 配置说明

### JWT 配置

```yaml
chai:
  jwt:
    secret: your-jwt-secret-key
    access-token-expire-time: 120  # 访问令牌过期时间（分钟）
    refresh-token-expire-time: 7   # 刷新令牌过期时间（天）
```

### 安全配置

```yaml
chai:
  security:
    max-login-fail-count: 5      # 最大登录失败次数
    account-lock-time: 30         # 账户锁定时间（分钟）
    password-strength: 10         # 密码加密强度
```

## 🤝 贡献指南

我们欢迎所有形式的贡献，包括但不限于：

- 🐛 报告 Bug
- 💡 提出新功能建议
- 📝 改进文档
- 🔧 提交代码

### 贡献步骤

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

### 开发规范

- 遵循阿里巴巴 Java 开发手册
- 使用 Lombok 简化代码
- 统一使用 Hutool 工具类
- 编写单元测试
- 更新相关文档

## 📄 许可证

本项目采用 [MIT](LICENSE) 许可证 - 查看 LICENSE 文件了解详情。

## 🙏 致谢

感谢以下开源项目的支持：

- [Spring Boot](https://spring.io/projects/spring-boot) - 应用框架
- [Spring Security](https://spring.io/projects/spring-security) - 安全框架
- [MyBatis Plus](https://baomidou.com/) - 持久层框架
- [Hutool](https://hutool.cn/) - Java 工具类库
- [Druid](https://github.com/alibaba/druid) - 数据库连接池
- [PostgreSQL](https://www.postgresql.org/) - 数据库
- [Redis](https://redis.io/) - 缓存数据库

## 📞 联系方式

- **作者**: shamee
- **邮箱**: [your-email@example.com]
- **项目地址**: [https://github.com/your-username/chai-admin-service]

如有问题或建议，欢迎通过以下方式联系：

- 提交 [Issue](https://github.com/your-username/chai-admin-service/issues)
- 发送邮件
- 加入讨论群

---

⭐ 如果这个项目对你有帮助，请给它一个 Star！