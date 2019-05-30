# RabbitMQ消息封装组件
> 对Spring与RabbitMQ整合进行封装整合，使其更简单的对应用项目提供生产、消费等功能。

# 使用文档

## 第一步: 添加依赖
> pom.xml中添加如下依赖

```xml
<dependency>
    <groupId>com.uzdz.group</groupId>
    <artifactId>rabbit-mq</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
## 第二步: 启动MQ组件
> 在核心启动类Application里添加@EnableRabbitMq(startup = true)注解

> 当startup为false时不启动MQ

```java
@SpringBootApplication
@EnableDubbo
@EnableRabbitMq(startup = true)
public class UserProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserProviderApplication.class, args);
    }
}
```

## 第三步: 生产者发送消息
> 使用集成组件已经注入Spring容器中的RabbitSender发送消息

```java
@RestController
public class ForkJoinTest {

    @Autowired
    private RabbitSender rabbitSender;

    @GetMapping("/rabbitSendFirst")
    public Object rabbitSendFirst() {

        rabbitSender.send("uzdz", "dongzhen", new Uzdz("uzdz"));

        return "first success";
    }
}
```

## 第四步: 消费者消费消息
> 通过@RabbitRegistrar注解声明交换机、队列与绑定关系，以及消费者类。

> 消费者类通过@RabbitRegistrar注解中的consumerClasses指定，并且指定的类必须继承RabbitBaseConsumer父类并重写方法

```java
@RabbitRegistrar(exchangeName = "uzdz",
        exchangeType = RabbitExchangeTypeMode.DIRECT,
        queueName = "uzdz_queue",
        routingKey = "dongzhen",
        consumerClasses = RabbitConsumerTest.class,
        registrarType = RabbitRegistrarMode.BOTH)
public class RabbitConsumerTest extends RabbitBaseConsumer {

    /**
     * 用于指定consumer方法中Object为什么类型
     * @return
     */
    @Override
    public Class<?> convertClass() {
        return Uzdz.class;
    }

    /**
     * 具体消费逻辑内容
     * @param obj
     */
    @Override
    public void consumer(Object obj) {
        Uzdz uzdz = (Uzdz) obj;
        System.out.println("Consumer接受到消息" + uzdz);
    }
}
```

# 核心配置参数类

## 交换机类型：RabbitExchangeTypeMode
> 用于决定生产者路由至交换机后，交换机转发策略

1. 广播模式 FANOUT("fanout")
   
2. 全匹配模式 DIRECT("direct"),
   
3. 主题模糊模式 TOPIC("topic");

## 注册类别：RabbitRegistrarMode
> 用于指定@RabbitRegistrar注解职能

1. 生产端 PROVIDER
   
2. 消费端 CONSUMER
   
3. 两者聚合 BOTH

# 核心注解
> 用于声明队列、交换机、绑定关系，以及为消费者监听队列。

```java
/**
 * 交换机、队列、绑定组件注册器
 * {@link RabbitRegistrar annotations} for rabbitmq.
 * @author uzdz
 * @since 0.1.0
 * @date: 2018/12/3 11:07
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Documented
@Configuration
@Import(RabbitComponentRegistrar.class)
public @interface RabbitRegistrar {

    /**
     * 交换机名称
     * @return
     */
    String exchangeName() default "";

    /**
     * 交换机类型
     * @return
     */
    RabbitExchangeTypeMode exchangeType() default RabbitExchangeTypeMode.DIRECT;

    /**
     * 队列名称
     * @return
     */
    String queueName() default "";

    /**
     * 交换机与队列的routingKey
     * @return
     */
    String routingKey() default "";

    /**
     * 消费端类
     * @return
     */
    Class<?> consumerClasses() default RabbitNoneConsumer.class;

    /**
     * 注册类型
     * @return
     */
    RabbitRegistrarMode registrarType() default RabbitRegistrarMode.BOTH;
}
```