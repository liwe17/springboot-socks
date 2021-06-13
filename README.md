#springboot-socks
基于springboot的常用功能
##动态定时任务-springboot-schedule-task
1. 单机版定时任务
2. 集群部署需要解决两个问题 
    1>定时任务存在于DB和MAP中,修改定时任务,需要保证所有节点的MAP中P定时任务更新,需要每个节点在执行任务前进行MAP的更新操作.
    2>定时任务只能被一个节点执行,需要使用锁实现.
备注:目前只想到这两个问题

##参数校验-springboot-param-valid
`
@NotEmpty 用在集合类上面
@NotBlank 用在String上面
@NotNull  用在基本类型上或引用类型上
`
1. 统一的参数异常拦截器
2. 简化参数校验逻辑

##限流-springboot-current-limiter
1. 基于guava实现单机节点
2. 基于redis分布式实现

使用AOP切面,也可使用拦截器

##日常-springboot-daily-case
日常学习的小demo
###布隆过滤器
涉及的代码:
com.weiliai.daily.bloom.GuavaBloomFilterTest
com.weiliai.daily.bloom.RedisBloomHelper
com.weiliai.daily.utils.RedisBloomUtils
com.weiliai.daily.RedisBloomFilterTest
