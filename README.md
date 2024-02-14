# message-spring-boot-starter

[![](https://jitpack.io/v/com.gitee.wb04307201/message-spring-boot-starter.svg)](https://jitpack.io/#com.gitee.wb04307201/message-spring-boot-starter)

> 这是一个消息中间件  
> 通过配置和编码，即可将相同的消息通过钉钉自定义机器人、钉钉消息、飞书自定义机器人、飞书消息、企业微信自定义机器人、企业微信消息以及邮箱消息通道进行发送
> 提供统一消息维护方式，发送时会按照对应的平台类型自动进行转换

## 代码示例
1. 使用[消息中间件](https://gitee.com/wb04307201/message-spring-boot-starter)、[实体SQL工具类](https://gitee.com/wb04307201/sql-util)实现的[消息发送代码示例](https://gitee.com/wb04307201/message-demo)
2. 使用[动态调度](https://gitee.com/wb04307201/dynamic-schedule-spring-boot-starter)、[消息中间件](https://gitee.com/wb04307201/message-spring-boot-starter)、[动态编译加载执行工具](https://gitee.com/wb04307201/loader-util)、[实体SQL工具类](https://gitee.com/wb04307201/sql-util)实现的[动态编码动态任务调度Demo](https://gitee.com/wb04307201/dynamic-schedule-demo)

## 第一步 增加 JitPack 仓库
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

## 第二步 引入jar
1.1.0版本后升级到jdk 17 SpringBoot 3.2.0
```xml
<dependency>
    <groupId>com.gitee.wb04307201</groupId>
    <artifactId>message-spring-boot-starter</artifactId>
    <version>1.1.1</version>
</dependency>
```

## 第三步 在启动类上加上`@EnableMessage`注解
```java
@EnableMessage
@SpringBootApplication
public class MessageDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringTestApplication.class, args);
    }
}
```

## 第四步 `application.yml`配置文件中添加以下相关配置，可以配置多个群

#### 1. 配置钉钉自定义机器人

> 钉钉自定义机器人的配置请参考
> [自定义机器人接入](https://open.dingtalk.com/document/orgapp/custom-robot-access)
> [自定义机器人安全设置](https://open.dingtalk.com/document/orgapp/customize-robot-security-settings)
> 获得access_token和secret

```yaml
message:
  dingtalk:
    customRobot:
      - alias: alias #所有通道的别名不能重复
        accessToken: accessToken
        secret: secret
```

#### 2. 配置钉钉消息

> 钉钉消息需要先注册企业内部应用，应用的配置请参考
> [基础概念](https://open.dingtalk.com/document/isvapp/basic-concepts)
> 获得appkey、appsecret、agentId

```yaml
message:
  dingtalk:
    message:
      - alias: alias #所有通道的别名不能重复
        appkey: appkey
        appsecret: appsecret
        agentId: agentId
```

#### 3. 配置飞书自定义机器人

> 飞书自定义机器人的配置请参考
> [自定义机器人使用指南](https://open.feishu.cn/document/client-docs/bot-v3/add-custom-bot)
> 获得hookid、secret

```yaml
message:
  feishu:
    message:
      - alias: alias #所有通道的别名不能重复
        hookid: hookid
        secret: secret
```

#### 4. 配置飞书消息

> 飞书消息需要先注册企业内部应用，应用的配置请参考
> [如何获取应用的 App ID](https://open.feishu.cn/document/faq/trouble-shooting/how-to-obtain-app-id)
> 获得appId、secret

```yaml
message:
  feishu:
    message:
      - alias: alias #所有通道的别名不能重复
        appId: appId
        appSecret: appSecret
```

#### 5. 企业微信自定义机器人

> 企业微信自定义机器人的配置请参考
> [群机器人配置说明](https://developer.work.weixin.qq.com/document/path/99110)
> 获得key

```yaml
message:
  weixin:
    message:
      - alias: alias #所有通道的别名不能重复
        key: key
```

#### 6. 企业微信消息

> 企业微信消息需要先注册企业内部应用，应用的配置请参考
> [基本概念介绍](https://developer.work.weixin.qq.com/document/path/90665)
> 获得corpi、corpsecret、agentid

```yaml
message:
  weixin:
    message:
      - alias: alias #所有通道的别名不能重复
        corpid: corpid
        corpsecret: corpsecret
        agentid: agentid
```

#### 7. 邮箱

```yaml
message:
  mail:
    smtp:
      - alias: alias #所有通道的别名不能重复
        host: host
        from: from
        username: username
        password: password
```

## 第五步 注入MessageService,编写消息内容，调用发送

#### 1. 编写消息内容

```java

@RestController
public class DemoController {

    @Autowired
    MessageService messageService;

    @GetMapping(value = "/message/send")
    public String send() {
        return messageService.send(
                RequestContent.buildMarkdown()
                        .addAlias("dd-1", "mail-1")  //通过配置的别名指定消息通道
                        .addMessageType(MessageType.DingtalkCustomRobot) //通过消息通道类型指定消息通道
                        .title("测试群发")
                        .addLine(SubLine.title("这是一行标题1", 1))
                        .addLine(SubLine.title("这是一行标题2", 2))
                        .addLine(SubLine.text("这是一行文本"))
                        .addLine(SubLine.link("这是一行链接", "https://gitee.com/wb04307201/message-spring-boot-starter"))
                        .addLine(SubLine.quote("这是一行引用"))
                        .addLine(SubLine.bold("这是一行加粗"))
        ).toString();
    }
}
```

行数据类型与转换格式对照表

| message       | 钉钉           | 微信           | 飞书        | 邮件                  |
|---------------|--------------|--------------|-----------|---------------------|
| SubLine.text  | Markdown: 文字 | Markdown: 文字 | 文本标签：text | html: \<p>          |
| SubLine.title | Markdown: 标题 | Markdown: 标题 | 文本标签：text | html: \<h1>~\<h6>   |
| SubLine.link  | Markdown: 链接 | Markdown: 链接 | 超链接标签：a   | html: \<a>          |
| SubLine.quote | Markdown: 引用 | Markdown: 引用 | 文本标签：text | html: \<blockquote> |
| SubLine.bold  | Markdown: 加粗 | Markdown: 加粗 | 文本标签：text | html: \<strong>     |

#### 2.根据别名、消息通道类型配置额外参数

> 在实际项目中使用消息通道时，
> 比如使用钉钉自定义机器人发送群消息时，会出现@所有人或者@某个人的需求
> 使用钉钉消息的时候，也需要指定特定用户
> 这是需要额外的参数协助消息发往正确的目标
> 下面的示例是为钉钉自定义机器人增加 @所有人 参数

```java
                RequestContent.buildMarkdown()
                        .addDingtalkCustomRobot("isAtAll", Boolean.TRUE) //根据消息通道类型添加参数
                        .addDingtalkCustomRobot("dd-1", "isAtAll", Boolean.TRUE) //根据消息通道别名添加参数，优先级更大
                        .title("测试消息")
                        .addLine(SubLine.text("这是一行文本"));
```
###### 1. 钉钉自定义机器人支持参数
| 参数        | 参数类型         | 说明           |
|-----------|--------------|--------------|
| atMobiles | List<String> | 被@人的手机号      |
| atUserIds | List<String> | 被@人的用户userid |
| isAtAll   | Boolean      | 是否@所有人       |

###### 2. 钉钉消息支持参数
| 参数           | 参数类型    | 说明                                                          |
|--------------|---------|-------------------------------------------------------------|
| userid_list  | String  | user123,user456 接收者的userid列表，最大用户列表长度100                    |
| dept_id_list | String  | 123,345 接收者的部门id列表，最大列表长度20。接收者是部门ID时，包括子部门下的所有用户。          |
| to_all_user  | Boolean | 是否发送给企业全部用户(当设置为false时必须指定userid_list或dept_id_list其中一个参数的值) |

###### 3. 飞书机器人支持参数
@所有人
.addFeishuCustomRobot("all","所有人")
@单个用户（填入用户的 Open ID，且必须是有效值，否则取名字展示，并不产生实际的 @ 效果）
.addFeishuCustomRobot("ou_xxx","名字")

###### 4. 飞书消息
| 参数              | 参数类型   | 说明                                               |
|-----------------|--------|--------------------------------------------------|
| receive_id_type | String | 消息接收者id类型 open_id/user_id/union_id/email/chat_id |
| receive_id      | String | 消息接收者的ID，ID类型应与查询参数receive_id_type 对应            |

###### 5. 企业微信自定义机器人
暂无参数

###### 6. 企业微信消息
| 参数      | 参数类型   | 说明                                                                     |
|---------|--------|------------------------------------------------------------------------|
| touser  | String | 指定接收消息的成员，成员ID列表（多个接收者用‘\|’分隔，最多支持1000个）。特殊情况：指定为"@all"，则向该企业应用的全部成员发送 |
| toparty | String | 指定接收消息的部门，部门ID列表，多个接收者用‘\|’分隔，最多支持100个。当touser为"@all"时忽略本参数            |
| totag   | String | 指定接收消息的标签，标签ID列表，多个接收者用‘\|’分隔，最多支持100个。当touser为"@all"时忽略本参数            |

###### 7. 邮箱
| 参数 | 参数类型   | 说明          |
|----|--------|-------------|
| to | String | 收件邮箱1,收件邮箱2 |

## 其他1：内置界面

发送的消息可通过http://ip:端口/message/list进行查看  
注意：如配置了context-path需要在地址中对应添加  
![img.png](img.png)

## 其他2：动态增减平台信息

```java
//可以通过如下方法添加平台信息
messageService.add

//可以通过如下方法删除平台信息
messageService.removeByAlias
```

## 其他3：实际使用中，可通过配置和实现日志接口方法将数据持久化到数据库中

继承IMessageRecordService并实现方法，例如

```java
@Component
public class H2MessageRecordImpl implements IMessageRecordService {

    static {
        MutilConnectionPool.init("main", "jdbc:h2:file:./data/demo;AUTO_SERVER=TRUE", "sa", "");
    }

    @Override
    public MessageRecord save(MessageRecord messageRecord) {
        MessageRecordHistory messageRecordHistory = MessageRecordHistory.trans(messageRecord);
        if (!StringUtils.hasLength(messageRecordHistory.getId())) {
            messageRecordHistory.setId(UUID.randomUUID().toString());
            MutilConnectionPool.run("main", conn -> ModelSqlUtils.insertSql(messageRecordHistory).executeUpdate(conn));
        } else
            MutilConnectionPool.run("main", conn -> ModelSqlUtils.updateSql(messageRecordHistory).executeUpdate(conn));
        return messageRecordHistory.getMessageRecord();
    }

    @Override
    public List<MessageRecord> list(MessageRecord messageRecord) {
        MessageRecordHistory messageRecordHistory = MessageRecordHistory.trans(messageRecord);
        return MutilConnectionPool.run("main", conn -> ModelSqlUtils.selectSql(messageRecordHistory).executeQuery(conn)).stream().map(MessageRecordHistory::getMessageRecord).collect(Collectors.toList());
    }

    @Override
    public void init() {
        if (Boolean.FALSE.equals(MutilConnectionPool.run("main", conn -> new SQL<MessageRecordHistory>() {
        }.isTableExists(conn)))) MutilConnectionPool.run("main", conn -> new SQL<MessageRecordHistory>() {
        }.create().parse().createTable(conn));
    }
}
```

并添加配置指向类

```yaml
message:
  messageRecord: cn.wubo.message.H2MessageRecordImpl
```

## 待办

- [ ] *钉钉token增加缓存*

- [ ] *添加短信平台*