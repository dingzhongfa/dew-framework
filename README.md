Dew-Framework
==
*对Spring Cloud/Boot的封装扩展、整合公司现有能力、提供最佳实践，做为基础服务框架，支撑公司新项目的研发。*

> Dew [du:] 意为`露水`，希望此框架可以像晨间的露水一样透明、静谧。让使用者尽量不要感知框架的存在，专注业务实现。

## 设计理念

### 服务框架的尴尬

几乎每个软件公司都研发企业内部的服务框架以满足自身业务发展的需要，但几乎所有框架都会存在这样的尴尬：

1. 无法传承，框架的研发人员离职后没有可以接手
2. 上手难度大，很多框架喜欢重复造轮子，做出来的与业界主流思想/标准格格不入，导致学习培训成本高
3. 功能片面，不通用，服务框架讲求通用性，尽量让整个公司使用同一套规范以方便维护，但很多框架只实现了某些特定场景的功能无法通用化
4. 维护成本高，尤其是对于完全自研的框架，往往需要专职人员维护

### Dew框架思想

上述问题是Dew框架必须面对的，对应设计的核心理念是：**基于成熟框架扩展** ，具体要做到：

1. 简单容易，用最通用的、标准的、开发人员都熟悉的开发模型
2. 功能全面，尽量重用市场已有能力实现，减少框架自身的维护成本
3. 轻量，原则上不引入高侵入性的三方框架/类库
4. 可替换，只做扩展，尽量不修改基础框架代码，开发人员完全可以直接基于基础框架开发

实现上我们选择Spring Boot/Cloud这一业界主流框架。

## 功能模块

模块名     | 核心功能
-------- | ---------------- 
`boot-core` | 基于Spring Boot的扩展，Dew的核心模块
`cloud-core`| 基于Spring Cloud的扩展，基于`boot-core`
`cluster-common`  | 集群能力接口
`cluster-redis` | 集群能力-Redis实现
`cluster-hazelcast` | 集群能力-Hazelcast实现
`cluster-ignite` | 集群能力-Ignite实现(开发中）
`config` | 统一配置中心组件
`registry` | 服务注册中心组件
`gateway` | 服务网关组件
`<x>-example` | 各类示例

## 使用说明

> **TIP** 所有模块均为Maven结构

    <parent>
        <groupId>com.tairanchina.csp.dew</groupId>
        <artifactId>parent</artifactId>
        <version>1.0.0-alpha</version>
    </parent>
    ...
    <dependencies>
        <dependency>
            <groupId>com.tairanchina.csp.dew</groupId>
            <artifactId><模块名></artifactId>
            <version>1.0.0-alpha</version>
        </dependency>
    </dependencies>
    ...

> **NOTE** 由于本框架并没有修改Spring Boot/Cloud的功能，所以会提供使用原生写法和Dew写法两个版本进行对照

### 构建一个最基本的工程

#### 原生版本

1. 引入Maven

       <parent>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-parent</artifactId>
           <version>1.5.2.RELEASE</version>
       </parent>
       ...
       <dependencyManagement>
           <dependencies>
               <dependency>
                   <groupId>org.springframework.cloud</groupId>
                   <artifactId>spring-cloud-dependencies</artifactId>
                   <version>Dalston.SR1</version>
                   <type>pom</type>
                   <scope>import</scope>
               </dependency>
           </dependencies>
       </dependencyManagement>
       <dependencies>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter</artifactId>
           </dependency>
       </dependencies>
       ...
    
2. 添加启动类

       @SpringBootApplication
       @EnableAutoConfiguration()
        public class BoneExampleApplication {
           public static void main(String[] args) {
               new SpringApplicationBuilder(BoneExampleApplication.class).run(args);
           }
       }

#### Dew版本

1. 引入Maven

       <parent>
           <groupId>com.tairanchina.csp.dew</groupId>
           <artifactId>parent</artifactId>
           <version>1.0.0-alpha</version>
       </parent>
       ....
       <dependencies>
           <dependency>
               <groupId>com.tairanchina.csp.dew</groupId>
               <artifactId>boot-core</artifactId>
               <version>${dew.version}</version>
           </dependency>
       </dependencies>
       ....
       
    > **TIP** Dew版本的`parent`模块已经加入Spring Boot dependencyManagement，依赖了`spring-boot-starter-actuator`

2. 添加启动类

       public class BoneExampleApplication extends DewBootApplication {
           public static void main(String[] args) {
               new SpringApplicationBuilder(BoneExampleApplication.class).run(args);
           }
       }

    > **TIP** `DewBootApplication`使用如下注解：
    @SpringBootApplication
    @EnableTransactionManagement
    @EnableAutoConfiguration(exclude = {FreeMarkerAutoConfiguration.class, GsonAutoConfiguration.class, WebSocketAutoConfiguration.class})
    @ComponentScan(basePackageClasses = {Dew.class})
    @EnableAspectJAutoProxy(proxyTargetClass = true)

> **NOTE** 由于Maven的`parent`是固定的，为节省篇幅后文只写依赖包

### 启用Web服务

#### 原生版本

1. 引入Maven

       <dependencies>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
           </dependency>
       </dependencies>
       ...
    
2. 添加启动类

       @SpringBootApplication
       @EnableAutoConfiguration()
       public class WebExampleApplication {
       
           public static void main(String[] args) {
               new SpringApplicationBuilder(WebExampleApplication.class).run(args);
           }
       
       }

3. 添加Controller

       @RestController
       public class ExampleController {
       
           @GetMapping("/example")
           public String example() {
               return "enjoy!";
           }
       
       }

#### Dew版本

1. 引入Maven

       <dependencies>
           <dependency>
               <groupId>com.tairanchina.csp.dew</groupId>
               <artifactId>boot-core</artifactId>
               <version>${dew.version}</version>
           </dependency>
           <!-- 引入web依赖 -->
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
           </dependency>
       </dependencies>
       ...
    
2. 添加启动类

       public class WebExampleApplication extends DewBootApplication {
       
           public static void main(String[] args) {
               new SpringApplicationBuilder(WebExampleApplication.class).run(args);
           }
       
       }
       
3. 添加Controller -> (同原生版本)
       
### 使用Swagger API文档

#### 原生版本

1. 引入Maven (在`启用Web服务`的基础上增加)

       <dependencies>
          <dependency>
              <groupId>io.springfox</groupId>
              <artifactId>springfox-swagger2</artifactId>
              <version>${swagger.version}</version>
              <optional>true</optional>
          </dependency>
          <dependency>
              <groupId>io.springfox</groupId>
              <artifactId>springfox-swagger-ui</artifactId>
              <version>${swagger.version}</version>
              <optional>true</optional>
          </dependency>
       </dependencies>
       ...
    
2. 添加 Docket Bean

    > **TIP** 详见 https://github.com/swagger-api/swagger-samples/blob/master/java/java-spring-boot/src/main/java/io/swagger/sample/Application.java

#### Dew版本

1. 引入Maven (同原生版本)

2. 增加配置

       dew:
         basic:
           name: # 文档名称
           version: # 文档版本
           desc: # 文档描述
           webSite: # 文档官网
           doc:
             basePackage: "" # 要扫描的包路径

### 启用JDBC功能(JdbcTemplate)

#### 原生版本

1. 引入Maven

       <dependencies>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-jdbc</artifactId>
           </dependency>
           <!-- 对应的数据库JDBC驱动 -->
       </dependencies>

2. 增加配置   
 
       spring:
         datasource:
           driver-class-name: # 驱动名
           url: # 驱动url
    
3. 使用JDBC

       @Autowired
       JdbcTemplate jdbcTemplate;

       public void test() throws Exception {

        jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE customers(" +
                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

        List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream()
                .map(name -> name.split(" "))
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);

        jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[] { "Josh" },
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
        ).forEach(customer -> log.info(customer.toString()));
        
        ...
    }

  > **TIP** JdbcTemplate知识见 https://spring.io/guides/gs/relational-data-access/

#### Dew版本

1. 引入Maven

       <dependencies>
           <dependency>
               <groupId>com.tairanchina.csp.dew</groupId>
               <artifactId>boot-core</artifactId>
               <version>${dew.version}</version>
           </dependency>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-jdbc</artifactId>
           </dependency>
           <!-- 对应的数据库JDBC驱动 -->
       </dependencies>

2. 增加配置 (同原生版本)   
           
3. 使用JDBC

        // ddl
        Dew.ds.jdbc().execute("CREATE TABLE example_entity\n" +
                "(\n" +
                "id int primary key auto_increment,\n" +
                "field_a varchar(255)\n" +
                ")");
        // insert
        ExampleEntity entity = new ExampleEntity();
        entity.setFieldA("测试A");
        long id = Dew.ds.insert(entity);
        // get
        logger.info(">>>> "+Dew.ds.getById(id, ExampleEntity.class).getFieldA());

#### Dew对`JdbcTemplate`做扩展

1. 支持实体与SQL的映射

       可选的注解：
       Entity: 表示此类可映射为数据库表
       PkColumn: 主键标识
         存在此注解的实体可以使用 `xxxById` 操作
       CodeColumn: 业务主键
         在工程中很多对象的主键不依赖于数据库主键而会使用code（如uuid表示）作为业务主键，
         保存（insert）时如果存在业务主键，且`value==null && uuid=true`，则会自动附加上uuid
         存在此注解的实体可以使用 `xxxByCode` 操作
       CreateUserColumn: 创建人，保存（insert）时自动附加当前操作人`code`（需要与获取操作人动作同一线程）
       CreateTimeColumn: 创建时间，保存（insert）时自动附加当前时间
       UpdateUserColumn：更新人，保存（insert）更新（updateById/updateByCode）时自动附加当前操作人`code`（需要与获取操作人动作同一线程）
       UpdateTimeColumn: 更新时间，保存（insert）更新（updateById/updateByCode）时自动附加当前时间
       EnabledColumn: 状态，启用或禁用
         存在此注解的实体可以使用 `enableByxx` `disableByxx` `xxEnabled` `xxDisabled` 操作
       Column: 普通字段

  > **IMPORTANT** 只有存在`Entity`注解的类才会被解析，只有存在`XXColumn`的字段才会被映射
  
  > **TIP** 为方便操作，框架提供了`PkEntity` `SafeEntity` `StatusEntity` `SafeStatusEntity` 四个预制的父类

2. 支持常用操作

       增加 Dew.ds.insert(Object entity) / Dew.ds.insert(Iterable<?> entities)
       更新 Dew.ds.updateById(long id, Object entity) / Dew.ds.updateByCode(String code, Object entity)
       获取单条记录 getById(long id, Class<E> entityClazz) / getByCode(String code, Class<E> entityClazz)
       获取多条记录 findAll(Class<E> entityClazz) / findAll(LinkedHashMap<String, Boolean> orderDesc, Class<E> entityClazz) / findEnabled(...) / / findDisabled(...)
       获取分页记录 paging(long pageNumber, int pageSize, LinkedHashMap<String, Boolean> orderDesc, Class<E> entityClazz) / pagingEnabled(...) / pagingDisabled(...)
       计数 countAll(Class<?> entityClazz) / countEnabled(Class<?> entityClazz) / countDisabled(Class<?> entityClazz) 
       启用 Dew.ds.enableById(long id, Class<?> entityClazz) / Dew.ds.enableByCode(String code, Class<?> entityClazz)
       禁用 Dew.ds.disableById(long id, Class<?> entityClazz) / Dew.ds.disableByCode(String code, Class<?> entityClazz)
       是否存在 Dew.ds.existById(long id, Class<?> entityClazz) / Dew.ds.existByCode(String code, Class<?> entityClazz)
       物理删除 Dew.ds.deleteById(long id, Class<?> entityClazz) / Dew.ds.deleteByCode(String code, Class<?> entityClazz)
       
  > **TIP** 您可以使用：`Dew.ds.jdbc()`获取`JdbcTemplate`原生API

> **NOTE** 以下功能原生版本支持比较繁琐或无对应实现，所以后文只描述Dew框架实现。

### 启用集群功能

> Dew的集群支持`分布式缓存` `分布式Map` `分布式锁` `MQ`，并且做了接口抽象以适配不同的实现，目前支持`Redis`和`hazelcast`，对`Ignite`的适配正在进行中。

1. 引入Maven

       <dependencies>
           <dependency>
               <groupId>com.tairanchina.csp.dew</groupId>
               <artifactId>boot-core</artifactId>
               <version>${dew.version}</version>
           </dependency>
           <!--引入集群依赖，可选redis或hazelcast-->
           <dependency>
               <groupId>com.tairanchina.csp.dew</groupId>
               <artifactId>cluster-spi-redis</artifactId>
               <version>${dew.version}</version>
           </dependency>
           <dependency>
               <groupId>com.tairanchina.csp.dew</groupId>
               <artifactId>cluster-spi-hazelcast</artifactId>
               <version>${dew.version}</version>
           </dependency>
       </dependencies>

2. 增加配置

       spring:
         redis:
           host: # redis主机
           port: # redis端口
           database:  # redis数据库
           password: # redis密码
         hazelcast:
           addresses: [""] # hazelcast地址，端口可选
       
       dew:
         cluster:
           cache: redis
           dist: hazelcast # 可选 redis/hazelcast
           mq: hazelcast # 可选 redis/hazelcast

3. 使用集群功能
       
       // 缓存示例，类似redis语法，支持string、list、hash
       Dew.cluster.cache.flushdb();
       Dew.cluster.cache.del("n_test");
       assert !Dew.cluster.cache.exists("n_test");
       Dew.cluster.cache.set("n_test", "{\"name\":\"jzy\"}", 1);
       assert Dew.cluster.cache.exists("n_test");
       assert "jzy".equals($.json.toJson(Dew.cluster.cache.get("n_test")).get("name").asText());
       Thread.sleep(1000);
       assert !Dew.cluster.cache.exists("n_test");
       assert null == Dew.cluster.cache.get("n_test");

       // 分布式Map，支撑常用Map操作
       ClusterDistMap<TestMapObj> mapObj = Dew.cluster.dist.map("test_obj_map", TestMapObj.class);
       mapObj.clear();
       TestMapObj obj = new TestMapObj();
       obj.a = "测试";
       mapObj.put("test", obj);
       assert "测试".equals(mapObj.get("test").a);
 
       // 分布式锁
       ClusterDistLock lock = Dew.cluster.dist.lock("test_lock");
       lock.delete();
       lock.lock();
       assert !lock.tryLock();
  
       // MQ:发布-订阅模型
       Dew.cluster.mq.subscribe("test_pub_sub", message ->
               logger.info("pub_sub>>" + message));
       Thread.sleep(1000);
       Dew.cluster.mq.publish("test_pub_sub", "msgA");
       Dew.cluster.mq.publish("test_pub_sub", "msgB");
       
       // MQ:请求-响应模型
       Dew.cluster.mq.response("test_rep_resp", message ->
               logger.info("req_resp>>" + message));
       Dew.cluster.mq.request("test_rep_resp", "msg1");
       Dew.cluster.mq.request("test_rep_resp", "msg2");

> **IMPORTANT** 实际选型要考虑不同实现的场景，比如`Redis`只能用于轻量`MQ`，如果数据比较长，那么`Hazelcast`更为理想
               
### 服务脚手架

> 一般的，我们对实体对象的操作可以有`增(C)删(D)改(U)查(R)`外加`状态变更(S)`，`服务脚手架`从`DAO`到`Service`再到`Controller`实现了上述操作。

    CRUController: 支持增改查操作
    CRUDController: 支持增删改查操作
    CRUSController: 支持增改查状态变更操作
    CRUDSController: 支持增删改查状态变更操作
    CRUVOController: 支持增改查操作（带VO-Entity转换）
    CRUSVOController: 支持增删改查操作（带VO-Entity转换）
    CRUDVOController: 支持增改查状态变更操作（带VO-Entity转换）
    CRUDSVOController: 支持增删改查状态变更操作（带VO-Entity转换）
    
    CRUService: 支持增改查操作
    CRUDService: 支持增删改查操作
    CRUSService: 支持增改查状态变更操作
    CRUDSService: 支持增删改查状态变更操作
    
    DaoImpl: 支持增删改查状态变更操作
    
    注意，上述Controller及Service都是带默认方法的接口
    
> **TIP** 详见API文档
    
### CORS支持

默认支持，在`启用Web服务`章节的基础上加上如下配置实现定制：

    dew:
      security:
        cors:
          allow-origin: # 允许来源，默认 *
          allow-methods: # 允许方法，默认 POST,GET,OPTIONS,PUT,DELETE,HEAD
          allow-headers: # 允许头信息 x-requested-with,content-type

## 统一响应格式

Dew支持两种格式：

1. 协议无关：`Resp<E>` 响应，对于`HTTP`统一返回`200`状态，使用`code`表示实际状态，`Resp`对象包含:
 
       `code`响应编码，与http状态码类似，200表示成功
       `message`响应附加消息，多有于错误描述
       `body`响应正文
       
   > **TIP**: `Resp`类提供了常用操作：详见 https://gudaoxuri.github.io/dew-common/#true-resp

2. 重用`HTTP Status Code`: 在无错误时直接返回内容，发生错误时返回`{"error":{"code":"实际错误码","message":"错误信息"}}`

启用统一响应格式支持

1. 配置如下：

       dew:
         basic:
           format:
             use-unity-error: true # 默认false
             reuse-http-state: # true:重用http状态码，false:使用协议无关格式

2. 相关代码

       // 使用协议无关格式
       public Resp<String> test(){
         return Resp.success("enjoy!");
         // or return Resp.notFound("...")/conflict("...")/badRequest("...")/...
       }
       
       // 重用http状态码
       // 与协议无关格式区别在于：
       //  1. throws 对应的异常
       //  2. 使用Dew.e(<code>,<Exception Instance>)来抛出异常
       public String test() throws IOException{
         return "enjoy!";
         // or throw Dew.e("A000", new IOException("io error"));
       }

### 认证缓存

> Dew内核不支持鉴权处理（Auth组件功能），但它支持`认证缓存`，即支持将鉴权系统生成的登录信息缓存到业务系统中方便即时调用。

1. 配置认证缓存

       dew:
         security:
           token-flag: # token key的名称
           token-in-header: # token key是否在http header中，为false是会从url query中获取
           token-hash: # token 值是否做hash（MD5）处理

2. 使用

       // 添加登录信息，optInfo封装自鉴权系统过来的登录信息
       // 一般在登录认证后操作
       Dew.Auth.setOptInfo(OptInfo optInfo);
       // 获取登录信息，要求在http请求加上token信息
       Dew.context().optInfo();
       // 删除登录信息
       // 一般在注销登录后操作
       Dew.Auth.removeOptInfo();
       
       
       // 登录信息
       public class OptInfo {
           // Token
           String token;
           // 账号编码
           String accountCode;
           // 登录ID
           String loginId;
           // 手机号
           String mobile;
           // 邮箱
           String email;
           // 姓名
           String name;
           // 角色列表
           List<RoleInfo> roles;
           // 最后一次登录时间
           Date lastLoginTime;
           // 扩展信息(Json格式)
           String ext;
           // 角色信息
           public static class RoleInfo {
               // 角色编码
               String code;
               // 角色显示名称
               String name;
               // 租户编码
               String tenantCode;
           }
       }

### 服务调用开发期优化

> 在Spring Cloud体系下，服务调用需要启动`Eureka`服务（对于Dew中的`Regstry`组件），这对开发阶段并不友好：
> 1. 开发期间会不断启停服务，`Eureka`保护机制会影响服务注册（当然这是可以关闭的）
> 2. 多人协作时可能会出现调用到他人服务的情况（同一服务多个实例）
> 3. 需要启动`Eureka`服务，多了一个依赖

为解决上述问题，Dew框架做了相应的优化，
在服务调用时使用`Dew.EB.post/get/put/delete/options/head`方法，Dew会根据传入的`URL`判断，
如果是`IPv4`则直接调用服务，否则使用Spring Cloud的`RestTemplate`调用。
所以您只需要把服务url做成配置，开发时使用`ip`，测试/生产时使用`service-id`。

## 编译部署

### 开发期热部署

1. 引入依赖

       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-devtools</artifactId>
           <optional>true</optional>
       </dependency>

2. 设置IDE(以Intellij IDEA为例)

    2.1 `Setting` -> `Build,Executiion,Deployment>Compiler`，勾选 `Build project automatically`
    2.2  `Shift+Ctrl+Alt+/` -> 选择 `Registry`，出现 `Maintenance` 窗口，勾选 `compiler.automake.allow.when.app.running`

### 打包
 
`mvn clean package -P fatjar` 即可打出一个包含所有依赖的包。

## 最佳实践

TBD

## 路线图

TBD

## 贡献

 角色   | 成员
-------- | ---------------- 
负责部门 | 架构组
开发成员 | 所有开发人员

#### SCM
https://rep.360taihe.com/csp/dew-framework/

TBD



