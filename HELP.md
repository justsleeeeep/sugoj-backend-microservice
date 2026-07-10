# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.13/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.13/maven-plugin/reference/html/#build-image)
* [Nacos Service Discovery](https://spring-cloud-alibaba-group.github.io/github-pages/hoxton/en-us/index.html#_spring_cloud_alibaba_nacos_discovery)
* [Spring Cloud Alibaba Sentinel](https://spring-cloud-alibaba-group.github.io/github-pages/hoxton/en-us/index.html#_spring_cloud_alibaba_sentinel)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.6.13/reference/htmlsingle/#using.devtools)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.6.13/reference/htmlsingle/#web)
* [MyBatis Framework](https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)

### Guides
The following guides illustrate how to use some features concretely:

* [Nacos Service Discovery Example](https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-examples/nacos-example/nacos-discovery-example/readme.md)
* [Sentinel Example](https://github.com/alibaba/spring-cloud-alibaba/tree/master/spring-cloud-alibaba-examples/sentinel-example/sentinel-core-example)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [MyBatis Quick Start](https://github.com/mybatis/spring-boot-starter/wiki/Quick-Start)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

https://start.aliyun.com/ 云原生脚手架

用户服务:没有调用其他服务

题目服务调用:
userService.getLoginUser()
userService.isAdmin()
userService.getOne()
judgeService.doJudge()

判题服务调用:
questionSubmitService.getById()
questionSubmitService.updateById()
questionService.getById()
questionService.updateById()

        - 接口文档:localhost:8101/doc.html
nacos 要下载2.2.0

ribbitMQ:
```
docker pull rabbitmq:3.10-management

docker run -d \
  --name my-rabbitmq \
  --restart=always \
  -p 5672:5672 \
  -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=admin \
  -e RABBITMQ_DEFAULT_PASS=admin \
  -v /data/rabbitmq:/var/lib/rabbitmq \
  rabbitmq:3.10-management
  
firewall-cmd --zone=public --add-port=5672/tcp --permanent
firewall-cmd --zone=public --add-port=15672/tcp --permanent
firewall-cmd --reload

mkdir -p /data/rabbitmq
chmod 777 /data/rabbitmq
sudo chmod 777 /data/rabbitmq

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>

spring:
  rabbitmq:
    host: 服务器IP
    port: 5672
    username: admin
    password: admin
```
```
分布式升级：
将 synchronized 替换为 Redisson 分布式锁，确保跨 JVM 的线程安全性。
缓存策略落地：
实现热门题目的缓存以提升响应速度；
使用 ZSet 实现用户积分或题目榜单排名；
引入 布隆过滤器 有效拦截缓存穿透。
消息幂等保障：
在判题逻辑中引入状态机或唯一请求 ID 校验，确保同一条判题请求即便被重复消费，也不会导致脏数据。
服务治理配置：
定义限流熔断规则，确保在流量峰值时系统具备自愈和自我保护能力。
沙箱功能闭环：
补齐 JavaDockerCodeSandbox 中的容器执行逻辑（挂载、限额、安全运行）；
完成 MainController 的 REST 接口开发，实现从提交到判题结果返回的完整链路。
```

sugoj 如何启动
```
1.先把前后端还有代码沙箱运行起来
2.后端的微服务要开nacos
3.虚拟机要开mysql,redis,rabbitmq
4.虚拟机里运行code-sandbox 命令[1.(cd sugoj-code-sandbox)2.(./mvnw spring-boot:run)]
Nacos http://127.0.0.1:8848/nacos 账号密码(nacos / nacos) 
RabbitMQ http://192.168.40.135:15672 账号密码(admin / admin)
```
