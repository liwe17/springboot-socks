# springboot-socks
基于springboot的常用功能
##动态定时任务-springboot-schedule-task
1. 单机版定时任务
2. 集群部署需要解决两个问题 
    1>定时任务存在于DB和MAP中,修改定时任务,需要保证所有节点的MAP中P定时任务更新,需要每个节点在执行任务前进行MAP的更新操作.
    2>定时任务只能被一个节点执行,需要使用锁实现.
备注:目前只想到这两个问题
