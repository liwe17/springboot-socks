# Set说明
## redis的Set主要功能就是求交集,并集,差集
> 概念实例

```text
A={'a','b','c'} 
B={'a','e','i','o','u'}

inter(x,y):交集,在集合x和集合y中都存在的元素
inter(A,B)={'a'}

union(x,y):并集,在集合x中或在集合y中的元素,如果一个元素在x,y中均出现,那只记录一次即可
union(A,B)={'a','b','c','e','i','o','u'}

diff(x,y):差集,在集合x中不在集合y中的元素
diff(A,B)={'b','c'}

card(x)：基数,一个集合中元素的数量
card(A)=3

```

## 操作命令
### SADD
作用:向名称为key的set中添加元素member
语法: SADD key member [member ...]

### SMEMBERS
作用:返回名称为key的set的所有元素
语法:SMEMBERS key

```text
127.0.0.1:6379> sadd users u1
(integer) 1
127.0.0.1:6379> sadd users u1 u2 u3
(integer) 2
127.0.0.1:6379> smembers users
1) "u1"
2) "u3"
3) "u2"
127.0.0.1:6379>
```

### SISMEMBER
作用:member是否是名称为key的set的元素
语法:SISMEMBER key member

## SCARD
作用:返回名称为key的set的基数,一个集合中元素的数量
语法:SCARD key

```text
127.0.0.1:6379> smembers users
1) "u1"
2) "u3"
3) "u2"
127.0.0.1:6379> sismember users u0
(integer) 0
127.0.0.1:6379> sismember users u1
(integer) 1
127.0.0.1:6379> scard users
(integer) 3
127.0.0.1:6379>
```

## SMOVE
作用:将member元素从source集合移动到destination集合
语法:SMOVE source destination member

```text
127.0.0.1:6379> smembers users
1) "u1"
2) "u3"
3) "u2"
127.0.0.1:6379> smembers blacklist
(empty list or set)
127.0.0.1:6379> smove users blacklist u1
(integer) 1
127.0.0.1:6379> smove users blacklist u0
(integer) 0
127.0.0.1:6379> smembers blacklist
1) "u1"
127.0.0.1:6379> smembers users
1) "u3"
2) "u2"
127.0.0.1:6379>
```

## SRANDMEMBER
作用:随机返回名称为key的set的元素
语法:SRANDMEMBER key [count]

```text
127.0.0.1:6379> smembers users
1) "u3"
2) "u2"
127.0.0.1:6379> srandmember users
"u3"
127.0.0.1:6379> srandmember users 1
1) "u3"
127.0.0.1:6379> srandmember users 2
1) "u2"
2) "u3"
127.0.0.1:6379> srandmember users 3
1) "u2"
2) "u3"
127.0.0.1:6379>
```

## SPOP
作用:随机返回并删除名称为key的set中一个元素
语法:SPOP key

```text
127.0.0.1:6379> smembers users
1) "u5"
2) "u4"
3) "u3"
4) "u2"
5) "u6"
127.0.0.1:6379> spop users
"u3"
127.0.0.1:6379> smembers users
1) "u2"
2) "u5"
3) "u4"
4) "u6"
127.0.0.1:6379>
```

## SINTER
作用:求交集
语法:SINTER key [key ...]

```text
127.0.0.1:6379> smembers blacklist
1) "u4"
2) "u1"
3) "u2"
127.0.0.1:6379> smembers users
1) "u2"
2) "u5"
3) "u4"
4) "u6"
127.0.0.1:6379> sinter users
1) "u2"
2) "u5"
3) "u4"
4) "u6"
127.0.0.1:6379> sinter users blacklist
1) "u4"
2) "u2"
127.0.0.1:6379> sinter users blacklist1
(empty list or set)
127.0.0.1:6379> sinter users blacklist users
1) "u4"
2) "u2"
127.0.0.1:6379> sinter users blacklist blacklist1
(empty list or set)
127.0.0.1:6379> sinter users blacklist blacklist
1) "u4"
2) "u2"
127.0.0.1:6379>
```

## SINTERSTORE
作用:求交集并将交集保存到destination key的集合
语法:SINTERSTORE destination key [key ...]

```text
127.0.0.1:6379> sadd group1 1 2 3 4
(integer) 4
127.0.0.1:6379> sadd group2 3 4 5 6
(integer) 4
127.0.0.1:6379> sinterstore group3 group1 group2
(integer) 2
127.0.0.1:6379> smembers group1
1) "1"
2) "2"
3) "3"
4) "4"
127.0.0.1:6379> smembers group2
1) "3"
2) "4"
3) "5"
4) "6"
127.0.0.1:6379> smembers group3
1) "3"
2) "4"
127.0.0.1:6379>
```

## SUNION
作用:并集
语法:SUNION key [key ...]

## SUNIONSTORE
作用:求并集并将并集保存到destination key的集合
语法:SUNIONSTORE destination key [key ...]

```text
127.0.0.1:6379> smembers group1
1) "1"
2) "2"
3) "3"
4) "4"
127.0.0.1:6379> smembers group2
1) "3"
2) "4"
3) "5"
4) "6"
127.0.0.1:6379> sunion group1 group2
1) "1"
2) "2"
3) "3"
4) "4"
5) "5"
6) "6"
127.0.0.1:6379> sunionstore group0 group1 group2
(integer) 6
127.0.0.1:6379> smembers group0
1) "1"
2) "2"
3) "3"
4) "4"
5) "5"
6) "6"
127.0.0.1:6379>
```

## SDIFF
作用:求差集
语法:SDIFF key [key ...]

## SDIFFSTORE
作用:求差集并将差集保存到destination key的集合
语法:SDIFFSTORE destination key [key ...]

```text
127.0.0.1:6379> smembers group1
1) "1"
2) "2"
3) "3"
4) "4"
127.0.0.1:6379> smembers group2
1) "3"
2) "4"
3) "5"
4) "6"
127.0.0.1:6379> smembers group3
1) "3"
2) "4"
127.0.0.1:6379> sdiff group1 group2
1) "1"
2) "2"
127.0.0.1:6379> sdiff group1 group2 group3
1) "1"
2) "2"
127.0.0.1:6379> sdiff group1
1) "1"
2) "2"
3) "3"
4) "4"
127.0.0.1:6379> sdiffstore group4 group1 group2
(integer) 2
127.0.0.1:6379> smembers group4
1) "1"
2) "2"
127.0.0.1:6379>
```

## 应用场景
### 类似淘宝黑名单
- 场景特点
    - 快速过滤
    - 数据量大
- 实现方式
    - 先同步到set中
    - 使用sismember命令判断

- 代码实例
    -  com.weiliai.redis.controller.RedisSetController

### 类似京东京豆
- 场景特点
    - 随机性
    - 同一个人可以重复抽取相同个数的京豆
- 实现方式
    - 先同步到set中
    - 使用srandmember随机返回
    
### 类似年会抽奖
- 场景特点
    - 随机性
    - 一个人只能抽中一次
- 实现方式
    - 先同步到set中
    - 使用spop随机弹出
