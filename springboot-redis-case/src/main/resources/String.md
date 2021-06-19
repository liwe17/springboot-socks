# String说明
## 存储类型说明
- 三种存储类型
    - 字符串
    - 整数
    - 浮点数

## 连接方式

```text
# host 主机IP PORT 主机端口
./redis-cli -h host -p port
```

## 字符串命令
### SET
作用:设置值
语法:设置一对key value

```text
SET key value [NX] [XX] [EX<seconds>] [PX <millSeconds>]
```

- 参数说明
    - set:命令
    - key:待设置的键值
    - value:设置的值
    - NX:表示key不存在时才设置
    - XX:表示key存在才设置
    - EX seconds:设置过期时间,过期时间精确为秒
    - PX millSeconds:设置过期时间,过期时间精确为毫秒

### SETNX
- 作用:设置值(key不存在才设置) 等价于 SET key value NX
- 语法:SETNX key value

### SETEX
- 作用:设置值和有效期(秒) 等价于 SET key value EX expire
- 语法:SETEX key expire value

### PSETEX
作用:设置值和有效期(秒) 等价于 SET key value PX expire
语法:PSETEX key expire value

### MSET
- 作用:批量设置值
- 语法:MSET key value [key value ...]

### MGET
- 作用:批量取值
- 语法:MGET key1 [key2 key3 ...]

### GETSET
- 作用:先查key出value的值,然后再修改新值
- 语法:GETSET key value

### SETRANGE
- 作用:修改偏移量offset后的值为value
- 语法:SETRANGE key offset value

### GETRANGE
- 作用:截取字符串
- 语法:GETRANGE key start end

### APPEND
- 作用:字符串拼接
- 语法:APPEND key str

### SUBSTR
- 作用:字符串截取
- 语法:SUBSTR key str

### STRLEN
- 作用:获取字符串长度
- 语法:STRLEN key

## 数字操作
### INCR
- 作用:计数器,指定key做加1操作,操作成功后返回操作后的值
- 语法:INCR key

### DECR
- 作用:指定key做减1操作,操作成功后返回操作后的值
- 语法:DECR key

### INCRBY
- 作用:加法,指定key做加increment操作
- 语法:INCRBY key increment

### DECRBY
- 作用:减法,指定key做减decrement操作
- 语法:DECRBY key decrement

## 浮点数操作
### INCRBYFLOAT
- 作用:在原有的key上加上浮点数
- 语法:INCRBYFLOAT key increment

## 代码实例

- com.weiliai.redis.controller.RedisStringController