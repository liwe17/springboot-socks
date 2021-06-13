# hash说明
## redis的Hash结构

- redis的hash结构,其实就是value类型升级为hash(Java中的hash)
- 每个hash的存储大小: 可以存储2的(32-1)方的键值对(40多亿)

## 使用场景
- 存储Java对象,提升访问效率
- 短链接(分享短URL,短信短URL)

## 操作命令
### HSETNX
作用:将哈希表key中的字段field的值设为value
语法:HSETNX key field value

### HGET
作用:获取存储在哈希表中指定字段的值
语法:HGET key field

### HMSET
作用:同时将多个field-value(域-值)对设置到哈希表key中
语法:HMSET key field value [field value ...]

### HMGET
作用:获取所有给定字段的值
语法:HMGET key field [field ...]

### HKEYS
作用:获取指定hash中所有field值
语法:HKEYS key

### HVALS
作用:获取指定hash中所有value值
语法:HVALS key

### HGETALL
作用:获取指定hash中所有field,value值
语法:HGETALL key

### HLEN
作用:获取指定hash中元素的个数
语法:HLEN key

### HEXISTS
作用:检查指定的field是否存在
语法:HEXISTS key field

### HDEL
作用:删除一个或多个哈希表字段
语法:HDEL key field [field ...]

### HINCRBY
作用:给指定field对应的value值加上increment数值
语法:HINCRBY key field increment (整形)

### HINCRBYFLOAT
作用:给指定field对应的value值加上increment数值
语法:HINCRBYFLOAT key field increment(浮点数)

## 代码实例
- com.weiliai.redis.controller.RedisHashController