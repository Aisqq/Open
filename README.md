# Open 项目文档

## 一、项目概述
Open 是一个基于 Spring Boot 构建的 Java 项目，主要用于物联网设备数据的管理和分析。该项目涵盖了设备数据上传、数据异常检测、缓存处理、消息通知等功能，同时集成了 OpenGauss 数据库、Redis 缓存、JWT 认证等技术。

## 二、项目结构
.gitignore
Open.iml
README.md
pom.xml
src/
├── main/
│   ├── java/
│   │   ├── com/
│   │   │   ├── me/
│   │   │   │   ├── dao/         # 数据访问层，包含与数据库交互的接口
│   │   │   │   ├── entity/      # 实体类，定义数据模型
│   │   │   │   ├── service/     # 服务层，实现业务逻辑
│   │   │   │   ├── utils/       # 工具类，提供通用的工具方法
│   │   │   │   ├── exception/   # 异常处理类
│   │   │   │   ├── controller/  # 控制器层，处理 HTTP 请求
│   │   │   │   ├── vo/          # 值对象，用于数据传输
│   │   │   │   ├── config/      # 配置类，进行项目配置
│   ├── resources/
│   │   ├── mapper/      # MyBatis 的 XML 映射文件
│   │   ├── ac.txt       # 可能是配置文件或数据文件
│   │   ├── static/
│   │   │   ├── index.html # 前端页面，用于添加设备数据
## 三、主要依赖
项目使用 Maven 进行依赖管理，主要依赖项如下：

- **数据库相关**
   - `opengauss-jdbc`：用于连接 OpenGauss 数据库。
   - `mybatis-spring-boot-starter`：集成 MyBatis 进行数据库操作。
- **Web 开发**
   - `spring-boot-starter-web`：支持 Spring Boot Web 开发。
- **安全与认证**
   - `jjwt-api`、`jjwt-impl`、`jjwt-jackson`：用于 JWT 认证。
   - `spring-security-crypto`：提供加密功能。
- **缓存**
   - `spring-boot-starter-data-redis`：集成 Redis 进行缓存处理。
- **工具类库**
   - `hutool-all`：提供丰富的工具方法。
   - `pagehelper-spring-boot-starter`：用于分页查询。
- **消息队列**
   - `qpid-jms-client` 和 `spring-jms`：支持 JMS 消息队列。
- **AI 相关**
   - `spring-ai-alibaba-starter`：提供 AI 功能。

## 四、主要功能模块

### 1. 设备数据管理
- **设备数据上传**：通过 `DeviceServerImpl` 类的 `uploadDevice` 方法，支持从 Excel 文件批量上传设备数据到数据库。
- **设备数据查询**：通过 `DeviceServerImpl` 类的 `findDevice` 方法，根据老人 ID 查询设备列表。

### 2. 异常检测
- **多窗口异常检测算法**：`ModelUtils` 类实现了多窗口异常检测算法，可对设备数据进行异常检测，并返回异常标志和异常得分。

### 3. 缓存处理
- **Spring Cache**：`CacheService` 类使用 Spring Cache 进行缓存处理，提供老人数量的缓存查询功能。

### 4. 消息通知
- **摔倒检测消息通知**：`FallServer` 类在检测到老人摔倒时，发送消息并记录报警日志。

### 5. AI 交互
- **流式聊天**：`AiController` 和 `TestController` 类提供流式聊天接口，可根据用户输入生成响应。

## 五、前端页面
`src/main/resources/static/index.html` 提供了一个简单的前端页面，用于添加设备数据。用户可以通过该页面上传不同类型传感器的数据。

## 六、配置文件
- **ac.txt**：包含各种基础常识、健康医疗、科学技术、文化习俗、动植物知识等信息，在 `ChatConfig` 类中被写入向量存储。
- **ChatConfig**：配置聊天相关的 Bean，如 `ChatMemory`、`VectorStore`，并将 `ac.txt` 文件的内容写入向量存储。

## 七、使用方法

### 环境准备
确保安装了以下组件：
- Java 17
- Maven
- OpenGauss 数据库
- Redis 缓存

### 配置文件
在 `application.properties` 中配置以下信息：
- 数据库连接信息
- Redis 连接信息
- JWT 密钥等

### 启动项目
使用 Maven 命令启动项目：
```bash
mvn spring-boot:run