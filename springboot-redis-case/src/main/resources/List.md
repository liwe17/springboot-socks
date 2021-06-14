# list说明
## redis的list数据结构
- List类型是一个双端链表的结构,容量是2的32次方减1个元素,即40多亿个
- 主要功能
    - PUSH,POP,获取元素等
    - 栈,队列,消息队列等

## 操作命令
### PUSH 
作用:以头插或尾插方式插入指定key队列中一个或多个元素
语法:[LR]PUSH key value1 [value2 ...]

### LRANGE
作用:获取列表指定范围内的元素
语法:LRANGE key start stop

```text
127.0.0.1:6379> lpush products 1 2 3
(integer) 3
127.0.0.1:6379> lpush products 4 5 6
(integer) 6
127.0.0.1:6379> lrange products 0 -1
1) "6"
2) "5"
3) "4"
4) "3"
5) "2"
6) "1"
127.0.0.1:6379>
```

### LINSERT
作用:在列表的元素前或者后插入元素
语法:LINSERT key BEFORE|AFTER pivot value

```text
127.0.0.1:6379> lrange products 0 -1
1) "6"
2) "5"
3) "4"
4) "3"
5) "2"
6) "1"
127.0.0.1:6379>
127.0.0.1:6379> linsert products before 4 a
(integer) 7
127.0.0.1:6379> lrange products 0 -1
1) "6"
2) "5"
3) "a"
4) "4"
5) "3"
6) "2"
7) "1"
127.0.0.1:6379> linsert products before 0 a
(integer) -1
127.0.0.1:6379> lrange products 0 -1
1) "6"
2) "5"
3) "a"
4) "4"
5) "3"
6) "2"
7) "1"
127.0.0.1:6379>
```

### LLEN 
作用:获取列表长度
语法:LLEN key

```text
127.0.0.1:6379> lrange products 0 -1
1) "6"
2) "5"
3) "a"
4) "4"
5) "3"
6) "2"
7) "1"
127.0.0.1:6379> llen products
(integer) 7
127.0.0.1:6379>
```

### LINDEX 
作用:通过索引获取列表中的元素
语法:LINDEX key index

```text
127.0.0.1:6379> lrange products 0 -1
1) "6"
2) "5"
3) "a"
4) "4"
5) "3"
6) "2"
7) "1"
127.0.0.1:6379> lindex products 1
"5"
127.0.0.1:6379>
```
### LSET
作用:通过索引设置列表元素的值
语法:LSET key index value

```text
127.0.0.1:6379> lrange products 0 -1
1) "6"
2) "5"
3) "a"
4) "4"
5) "3"
6) "2"
7) "1"
127.0.0.1:6379> lindex products 1
"5"
127.0.0.1:6379> lset products 3 A
OK
127.0.0.1:6379> lrange products 0 -1
1) "6"
2) "5"
3) "a"
4) "A"
5) "3"
6) "2"
7) "1"
127.0.0.1:6379> lset products 8 A
(error) ERR index out of range
127.0.0.1:6379>
```

### LTRIM
作用:截取队列指定区间的元素,其余元素都删除
语法:LTRIM key start end

```text
127.0.0.1:6379> lrange products 0 -1
1) "6"
2) "5"
3) "a"
4) "A"
5) "3"
6) "2"
7) "1"
127.0.0.1:6379> ltrim products 0 3
OK
127.0.0.1:6379> lrange products 0 -1
1) "6"
2) "5"
3) "a"
4) "A"
127.0.0.1:6379> ltrim products 0 100
OK
127.0.0.1:6379> lrange products 0 -1
1) "6"
2) "5"
3) "a"
4) "A"
127.0.0.1:6379>
```

### LREM
说明:移除列表元素
语法:LREM key count value

```text
127.0.0.1:6379> lpush test a 1 a 2 a 3 a 4 a 5 6
(integer) 11
127.0.0.1:6379> lrange test 0 -1
 1) "6"
 2) "5"
 3) "a"
 4) "4"
 5) "a"
 6) "3"
 7) "a"
 8) "2"
 9) "a"
10) "1"
11) "a"
127.0.0.1:6379> lrem test 3 a
(integer) 3
127.0.0.1:6379> lrange test 0 -1
1) "6"
2) "5"
3) "4"
4) "3"
5) "2"
6) "a"
7) "1"
8) "a"
127.0.0.1:6379> lrem test 3 a
(integer) 2
127.0.0.1:6379> lrange test 0 -1
1) "6"
2) "5"
3) "4"
4) "3"
5) "2"
6) "1"
127.0.0.1:6379>
```
### POP
作用:从队列的头或未弹出节点元素(返回该元素并从队列中删除)
语法:[LR]POP key

### RPOPLPUSH 
RPOPLPUSH source destination
移除列表的最后一个元素,并将该元素添加到另一个列表并返回

```text
127.0.0.1:6379> lpush src 1 2 3
(integer) 3
127.0.0.1:6379> lpush dst a b c
(integer) 3
127.0.0.1:6379> lrange src 0 -1
1) "3"
2) "2"
3) "1"
127.0.0.1:6379> lrange dst 0 -1
1) "c"
2) "b"
3) "a"
127.0.0.1:6379> rpoplpush src dst
"1"
127.0.0.1:6379> lrange src 0 -1
1) "3"
2) "2"
127.0.0.1:6379> lrange dst 0 -1
1) "1"
2) "c"
3) "b"
4) "a"
127.0.0.1:6379>
```

### B[LR]POP 
B[LR]POP key1 [key2 ...] timeout
移出并获取列表的第一个或最后一个元素,如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止.

```text
127.0.0.1:6379> lpush blist 1 2
(integer) 2
127.0.0.1:6379> lrange blist 0 -1
1) "2"
2) "1"
127.0.0.1:6379> blpop blist 2
1) "blist"
2) "2"
127.0.0.1:6379> blpop blist 2
1) "blist"
2) "1"
127.0.0.1:6379> blpop blist 2
(nil)
(2.09s)
127.0.0.1:6379> 
```

## 应用场景
### 类似于聚划算应用
- 场景特点
    - 数据量较少
    - 并发访问量大
- 实现方式
    - 通过定时器现将数据库中的数据抽取到redis里面
    - redis list结构天然支持这种高并发的分页查询功能
    - redis lpush以及lrange即可
    
- 实例代码
    - com.weiliai.redis.controller.RedisListController
    
### 类似高并发微信文章的阅读量PV
- 场景特点
    - 并发访问量大,如果全部依赖redis将导致CPU负载过大
    - 业务逻辑简单,仅用于记录访问次数
- 实现方式
    - 减少redis访问量
    - 通过二级缓存和定时器
        - 一级JVM缓存Map<时间块,Map<文章id,访问量PV>>,然后定时push到redis list缓存Map<文章id,访问量PV>
        - 二级缓存redis,然后定时pop保存在数据库中,并同步到redis缓存的计数器incr
        
        
        
        