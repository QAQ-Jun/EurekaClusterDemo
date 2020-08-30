# EurekaCluster集群
#### 项目介绍：
  本项目为一个eureka集群测试demo，通过两个eureka相互注册形成集群对外提供服务治理，通过两个服务provider向集群进行注册，然后两个服务consumer进行远程调用。通过手工kill单个eureka 以及 kill 单个服务来测试eureka集群的变化。

---
#### 代码结构：
EurekaServerDemo：
+ 功能：eurek注册中心，对外提供服务治理的功能。
+ 测试相关：EurekaServerDemo内有两个配置文件，application1.yml 和 application2.yml。使用applicaiton1.yml启动，eureka注册中心为8761。
application2.yml启动，eureka注册中心为8762。并且这两个注册中心会进行相互注册，从而形成eureka集群。

EureKaProviderDemo：
+ 功能：服务提供者，通过向eureka注册服务，使得服务消费者可以进行调用。
+ 测试相关：EurekaProviderDemo内有两个配置文件，application-8001.yml 和 applcaiton-8002.yml。
application-8001.yml启动，端口为8081，默认会向端口为8761上的eureka进行注册。
application-8001.yml启动，端口为8082，默认会向端口为8762上的eureka进行注册。

EureKaConsumerDemo 
+ 功能：服务消费者，通过feign对注册在eureka中的服务进行远程调用
+ 测试相关：EureKaConsumerDemo内有两个配置文件，application-8081.yml 和 application-8082.yml，application-8081.yml启动，端口为8081，并且默认会注册到端口为8761上的eureka中。
application-8082.yml启动，端口为8082，并且默认会注册到端口为8762的eureka上。

tips：
+ idea如何通过不同的配置文件启动项目，可参考下面博客：https://blog.csdn.net/zhuwei_clark/article/details/103680293
+ eureka 如何配置多注册中心  eureak配置如下：defaultZone: http://localhost:8762/eureka/,http://127.0.0.1:8761/eureka/

---
#### 个人Eureka集群测试结果：
集群1：  
&nbsp;&nbsp;&nbsp;&nbsp;注册中心 S1  服务提供者 P1   服务消费者 C1   
&nbsp;&nbsp;&nbsp;&nbsp;注册中心 S2  服务提供者 P2   服务消费者 C2  
&nbsp;&nbsp;&nbsp;&nbsp;S1 和 S2 相互注册为服务，构成Eurek集群  
&nbsp;&nbsp;&nbsp;&nbsp;P1 和 C1 注册在S1  
&nbsp;&nbsp;&nbsp;&nbsp;P2 和 C2 注册在S2  

+ 情况1：P1、P2、C1、C2 正常进行服务注册以及下线  
        S1 和 S2 都能知晓这4个服务的注册 以及 下线，因为S1 和 S2 之间会进行信息同步

+ 情况2：S1 或者 S2 其中一个宕机。我们假设 S1 宕机， 其他服务会发生什么变化。  
      答：S2、P1、C1 会抛异常，S1 报连接被拒绝，P1 和 C1 报无法发起请求

+ 情况3：S1宕机，P1 和 C1 会是什么情况？  
    答：在S2内部缓存信息没有过期前，P1 和 C1都还存在S2的服务信息中，服务可以正常消费。缓存信息过期之后，C2则会剔除掉P1 和 C1的服务信息。
 
+ 情况4：S1宕机，此时如果 P1 下线，会是什么情况？  
      答：在S2内部缓存没过期之前，P1的服务信息还存在，但是没办法调用P1服务提供的接口，等缓存信息到了过期时间。S2 则会把P1信息剔除，并且也会剔除C1服务信息。

集群2：  
&nbsp;&nbsp;&nbsp;&nbsp;注册中心 S1  服务提供者 P1   服务消费者 C1   
&nbsp;&nbsp;&nbsp;&nbsp;注册中心 S2  服务提供者 P2   服务消费者 C2  
&nbsp;&nbsp;&nbsp;&nbsp;S1 和 S2 相互注册为服务，构成Eurek集群   
&nbsp;&nbsp;&nbsp;&nbsp;P1 和 C1 注册在S1、S2  
&nbsp;&nbsp;&nbsp;&nbsp;P2 和 C2 注册在S1、S2  

+ 情况1：P1、P2、C1、C2 正常进行服务注册以及下线  
    答：S1 和 S2 都可以看到P1 、P2、C1、C2 服务信息

+ 情况2：S1 或者 S2 其中一个宕机，我们假设 S1 宕机， 其他服务会发生什么变化。  
    答：S2  都会报错，C2 、P2、C1 、P1 之中两个会报错，取决于配置，配置多注册中心时，会采用最后一个注册中心进行注册。例如：
C2 配置的顺序为 S2、S1，那么S1宕机，C2则会报错。但是此时C2的信息会去注册到S2上，所以C2服务一样是可以被正常调用，并且不会受S2缓存的影响，因为对于S2来说，C2是注册的信息，而非从S1同步信息过来，并且C2 会向S2发送续约信息，所以C2 服务会一直处于可用状态。


  

