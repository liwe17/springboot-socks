# ZSet说明

- ZSet是set的一个升级版本,它在set的基础上增加了顺序属性.
    - zset也是string类型元素的集合且不允许重复
    - 每个元素都会关联一个double类型的score
- ZSet是通过哈希表实现的,所以增加,删除,查找都是O(1).
- ZSet最大成员数为2的32-1次方(大约40亿),典型应用场景是排行榜.

## 操作命令
### ZADD
- 作用:将一个或多个member元素及其score值加入到有序集key当中,如果存在则更新分数
- 语法:ZADD key score member [score member ...]

### ZRANGE
- 作用:返回有序集key中,指定区间内的成员
- 语法:ZRANGE key start stop [WITHSCORES]

### ZREVRANGE
- 作用:返回有序集key中,指定区间内的成员,降序
- 语法:ZREVRANGE key start stop [WITHSCORES]

```text
案例:创业公司招进了4个员工,分别为:alex 2000,tom 5000,jack 6000元,请按照工资升序排序

127.0.0.1:6379> zadd salary 2000 alex 5000 tom 6000 jack 1000 li
(integer) 4
127.0.0.1:6379> zrange salary 0 -1 withscores
1) "li"
2) "1000"
3) "alex"
4) "2000"
5) "tom"
6) "5000"
7) "jack"
8) "6000"
127.0.0.1:6379> zrange salary 0 -1
1) "li"
2) "alex"
3) "tom"
4) "jack"
127.0.0.1:6379>

```
### ZREM
- 作用:移除有序集key中的一个或多个成员,不存在的成员将被忽略
- 语法:ZREM key member [member ...]

```text
案例:创业公司tom离职了
127.0.0.1:6379> zrange salary 0 -1 withscores
1) "li"
2) "1000"
3) "alex"
4) "2000"
5) "tom"
6) "5000"
7) "jack"
8) "6000"
127.0.0.1:6379> zrem salary tom
(integer) 1
127.0.0.1:6379> zrange salary 0 -1 withscores
1) "li"
2) "1000"
3) "alex"
4) "2000"
5) "jack"
6) "6000"
127.0.0.1:6379>
```

### ZCARD
- 作用:返回有序集key的个数
- 语法:ZCARD key

### ZCOUNT
- 作用:返回有序集key中,score值在min和max之间(默认包括score值等于min或max)的成员
- 语法:ZCOUNT key min max

```text
案例:创业公司有多少人
127.0.0.1:6379> zrange salary 0 -1 withscores
1) "li"
2) "1000"
3) "alex"
4) "2000"
5) "jack"
6) "6000"
127.0.0.1:6379> zcard salary
(integer) 3
127.0.0.1:6379>
创业公司工资在2000至3000有多少人
127.0.0.1:6379> zcount salary 2000 3000
(integer) 1
127.0.0.1:6379>
```
### ZSCORE
- 作用:返回有序集key中,成员member的score值
- 语法:ZSCORE key member

### ZINCRBY
- 作用:为有序集key的成员member的score值加上增量increment
- 语法:ZINCRBY key increment member

```text
案例:创业公司,li的工资是多少
127.0.0.1:6379> zscore salary li
"1000"
127.0.0.1:6379> 

案例:创业公司,li的工资加1000
127.0.0.1:6379> zincrby salary 1000 li
"2000"
127.0.0.1:6379> zscore salary li
"2000"
127.0.0.1:6379>

```


### ZRANGEBYSCORE
- 作用:返回有序集key中,所有score值介于min和max之间(包括等于min或max)的成员,有序集成员按score值递增(从小到大)次序排列
- 语法:ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]

### ZREVRANGEBYSCORE
- 作用:返回有序集key中,score值介于max和min之间(默认包括等于max或min)的所有的成员,有序集成员按score值递减(从大到小)的次序排列
- 语法:ZREVRANGEBYSCORE key max min [WITHSCORES] [LIMIT offset count]

```text
127.0.0.1:6379> zrangebyscore salary 1000 6000
1) "alex"
2) "li"
3) "jack"
127.0.0.1:6379> zrangebyscore salary 1000 6000 limit 1 2
1) "li"
2) "jack"
127.0.0.1:6379> zrangebyscore salary 1000 6000 limit 0 -1
1) "alex"
2) "li"
3) "jack"
127.0.0.1:6379> zrangebyscore salary 1000 6000 withscores limit 0 -1
1) "alex"
2) "2000"
3) "li"
4) "2000"
5) "jack"
6) "6000"
127.0.0.1:6379> zrange salary 0 -1
1) "alex"
2) "li"
3) "jack"
127.0.0.1:6379>
127.0.0.1:6379> zrevrangebyscore salary 1000 6000 withscores limit 0 -1
(empty list or set)
127.0.0.1:6379> zrevrangebyscore salary 6000 1000  withscores limit 0 -1
1) "jack"
2) "6000"
3) "li"
4) "2000"
5) "alex"
6) "2000"
127.0.0.1:6379> zrevrangebyscore salary 6000 1000  withscores limit 0 1
1) "jack"
2) "6000"
127.0.0.1:6379> zrevrangebyscore salary 6000 1000  withscores limit 1 1
1) "li"
2) "2000"
127.0.0.1:6379>

```
### ZRANK
- 作用:取某个member的排名,升序
- 语法:ZRANK key member

### ZREVRANK
- 作用:取某个member的排名,降序
- 语法:ZREVRANK key member

```text
127.0.0.1:6379> zrange salary 0 -1 withscores
1) "alex"
2) "2000"
3) "li"
4) "2000"
5) "jack"
6) "6000"
127.0.0.1:6379> zrank salary li
(integer) 1
127.0.0.1:6379> zrevrank salary li
(integer) 1
127.0.0.1:6379> zrevrank salary jack
(integer) 0
127.0.0.1:6379>
```

### ZREMRANGEBYRANK
- 作用:移除指定排名(rank)区间内的所有成员
- 语法:ZREMRANGEBYRANK key start stop

### ZREMRANGEBYSCORE
- 作用:移除指定score值介于min和max之间(包括等于min或max)的成员
- 语法:ZREMRANGEBYSCORE key min max

```text
127.0.0.1:6379> zrange salary 0 -1
1) "alex"
2) "li"
3) "jack"
127.0.0.1:6379> zrange salary 0 -1 withscores
1) "alex"
2) "2000"
3) "li"
4) "2000"
5) "jack"
6) "6000"
127.0.0.1:6379> zremrangebyscore salary 1000 2000
(integer) 2
127.0.0.1:6379> zrange salary 0 -1 withscores
1) "jack"
2) "6000"
127.0.0.1:6379> zremrangebyrank salary 0 2
(integer) 1
127.0.0.1:6379> zrange salary 0 -1 withscores
(empty list or set)
127.0.0.1:6379>
```

### ZINTERSTORE
- 作用:计算给定的一个或多个有序集的交集,其中给定key的数量必须以numkeys参数指定,并将该交集(结果集)储存到destination
- 语法:ZINTERSTORE destination numkeys key [key ...] [WEIGHTS weight] [AGGREGATE SUM|MIN|MAX]


### ZUNIONSTORE
- 作用:计算给定的一个或多个有序集的并集,其中给定key的数量必须以numkeys参数指定,并将该并集(结果集)储存到destination
- 语法:ZUNIONSTORE destination numkeys key [key ...] [WEIGHTS weight] [AGGREGATE SUM|MIN|MAX]

```text
127.0.0.1:6379> zadd group2 10 x 20 y 30 y 20 c
(integer) 3
127.0.0.1:6379> zadd group1 10 a 20 b 30 c
(integer) 3
127.0.0.1:6379> zinterstore group3 2 group1 group2
(integer) 1
127.0.0.1:6379> zrange group3 0 -1 withscores
1) "c"
2) "50"
127.0.0.1:6379>
127.0.0.1:6379> zunionstore group4 2 group1 group2
(integer) 5
127.0.0.1:6379> zrange group4 0 -1 withscores
 1) "a"
 2) "10"
 3) "x"
 4) "10"
 5) "b"
 6) "20"
 7) "y"
 8) "30"
 9) "c"
10) "50"
127.0.0.1:6379>
```

## 应用场景
### 排行榜(小时榜,周榜,月榜)
- 技术特点
    - 访问量大
    - 实时排行
- 技术方案
    - 使用ZSet实现,自带排序,合并
- 思路
    - 采用26个英文字母来实现排行,字母代表一条微薄,随机为每个字母生成一个随机数作为score
    - 先初始化1个月的历史数据
    - 定时5秒钟,模拟微薄的热度刷新(例如模拟点赞,收藏,评论的热度更新)
    - 定时1小时合并统计天,周,月的排行榜

# GEOHASH

Geohash是一种地址编码,它能把二维的经纬度编码成一维的字符串.比如,世界之窗的编码是ws101xy1rp0

## 主要命令
### GEOADD
- 作用:将给定的位置对象(纬度,经度,名字)添加到指定的key
- 注意:
    - 这里我们采用的是中文存储,如果出现了乱码,redis命令的登录命令加上 --raw 例 ./redis-cli --raw
    - 查看某个地址的经纬度,建议用http://www.gpsspg.com/maps.htm
    
```text
127.0.0.1:6379> geoadd hotel 113.9807127428 22.5428248089 "世界之窗" 113.9832042690 22.5408496326 "南山威尼斯酒店" 114.0684865267 22.5412294122 "福田喜来登酒店" 114.3135524539 22.5999265998 "大梅沙海景酒店" 113.9349465491 22.5305488659 "南山新年酒店" 114.0926367279 22.5497917634 "深圳华强广场酒店"
6
127.0.0.1:6379> zrange hotel 0 -1
南山新年酒店
世界之窗
南山威尼斯酒店
福田喜来登酒店
深圳华强广场酒店
大梅沙海景酒店
```

### GEOPOS
- 作用:从key里面返回所有给定位置对象的位置(经度和纬度);

```text
127.0.0.1:6379> GEOPOS hotel "世界之窗"
113.98071080446243286
22.54282525199023013
```

### GEOHASH
- 作用:返回一个或多个位置对象的GEOHASH表示

```text
127.0.0.1:6379> GEOHASH hotel "世界之窗"
ws101xy1rp0
```
### GEODIST
- 作用:返回两个给定位置之间的距离
- 语法:### GEODIST key member1 member2 [unit]
    - UNIT单位
        - m 表示单位为米
        - km 表示单位为千米
        - mi 表示单位为英里
        - ft 表示单位为英尺

```text
127.0.0.1:6379> GEODIST hotel "世界之窗"  "南山威尼斯酒店" m
337.4887
```

### GEORADIUS
- 作用:给定一个经纬度,然后以半径为中心,计算出半径内的数据
- 语法:GEORADIUS key longitude latitude radius m|km|ft|mi [WITHCOORD] [WITHDIST] [WITHHASH] [COUNT count] [ASC|DESC] [STORE key] [STOREDIST key]

```text
127.0.0.1:6379> GEORADIUS hotel 113.9410499639 22.5461508801 10 km WITHDIST WITHCOORD count 10
南山新年酒店
1.8451
113.93494695425033569
22.53054959741555052
世界之窗
4.0910
113.98071080446243286
22.54282525199023013
南山威尼斯酒店
4.3704
113.98320525884628296
22.54085070420710224
```

```text
-- WITHDIST:在返回位置元素的同时,将位置元素与中心之间的距离也一并返回.距离的单位和用户给定的范围单位保持一致.
-- WITHCOORD:将位置元素的经度和维度也一并返回.
-- WITHHASH:以52位有符号整数的形式,返回位置元素经过原始 geohash编码的有序集合分值. 这个选项主要用于底层应用或者调试,实际中的作用并不大.
-- ASC,DESC	排序方式,按照距离的升序,降序排列
-- STORE key1 把结果存入key1,zset格式,以坐标hash为score
-- STOREDIST key2	把结果存入key2,zset格式,以距离为score

```

### GEORADIUSBYMEMBER
- 作用:GEORADIUSBYMEMBER和GEORADIUS一样的功能,区别在于,GEORADIUS是以经纬度去查询,而GEORADIUSBYMEMBER是以当前集合中的某个member元素来查询
- 语法:GEORADIUSBYMEMBER key longitude latitude radius m|km|ft|mi [WITHCOORD] [WITHDIST] [WITHHASH] [COUNT count] [ASC|DESC] [STORE key] [STOREDIST key]
```
127.0.0.1:6379> GEORADIUSBYMEMBER hotel "世界之窗" 10 km WITHDIST WITHCOORD count 10
世界之窗
0.0000
113.98071080446243286
22.54282525199023013
南山威尼斯酒店
0.3375
113.98320525884628296
22.54085070420710224
南山新年酒店
4.8957
113.93494695425033569
22.53054959741555052
福田喜来登酒店
9.0190
114.06848877668380737
22.54122837765984144
```