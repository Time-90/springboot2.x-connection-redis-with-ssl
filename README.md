# springboot2.0及以上版本访问自搭建redis或AWS ElastiCache

　　Springboot 2.x默认内置lettuce作为redis客户端，Lettuce的连接是基于Netty的，连接实例（StatefulRedisConnection）可以在多个线程间并发访问，因为StatefulRedisConnection是线程安全的，所以一个连接实例就可以满足多线程环境下的并发访问，当然这个也是可伸缩的设计，一个连接实例不够的情况也可以按需增加连接实例。

　　因此如果项目本身访问redis服务没有特殊要求，请尽量使用lettuce客户端，因为spring已经最大限度的为lettuce做了自动装配，可以仅仅通过改变配置文件来实现不同类型的redis服务。



### 1、在 pom.xml 中spring-boot-starter-data-redis的依赖，如果需要使用连接池，则同时需要引入commons-pool2，Spring Boot2.x后底层不在是Jedis如果做版本升级的同事需要注意下。

```java
<!--data-redis-->
 <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<!--lettuce.pool缓存连接池-->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
```


### 2、配置文件application-*.yml，可根据项目自身以及redis服务类型选择合适的配置参数；比如测试环境使用自搭建单节点redis，生产环境可以使用AWS集群加密带证书的Redis服务。

> 2.1 Redis单节点配置：不使用密码，不使用SSL（适用于自搭建redis或AWS ElastiCache单节点服务）
```java
spring:
  redis:
	# redis服务地址
    host: 127.0.0.1
	# 端口号
    port: 6379
	# 数据库号，不同的数据库中数据隔离保存
    database: 0
    # 连接超时timeout属性，单位： 毫秒
    timeout: 10000
    lettuce:
      # 在关闭客户端连接之前等待任务处理完成的最长时间，在这之后，无论任务是否执行完成，都会被执行器关闭，默认100ms
      shutdown-timeout: 100
      pool:
        # 连接池最大连接数（使用负值表示没有限制）
        max-active: 100
        # 连接池中的最大空闲连接
        max-idle: 20
        # 连接池中的最小空闲连接
        min-idle: 2
        # max-wait属性，单位： 毫秒
        max-wait: 1000000
```
</br>

> 2.2 Redis单节点配置：使用密码，不使用SSL（适用于自搭建redis或AWS ElastiCache单节点服务）
```java
spring:
  redis:
    # redis服务地址
    host: 127.0.0.1
	# 端口号
    port: 6379
	# 数据库号，不同的数据库中数据隔离保存
    database: 0
	# 密码
    password: 123456abcd
    # 连接超时timeout属性，单位： 毫秒
    timeout: 10000
    lettuce:
      # 在关闭客户端连接之前等待任务处理完成的最长时间，在这之后，无论任务是否执行完成，都会被执行器关闭，默认100ms
      shutdown-timeout: 100
      pool:
        # 连接池最大连接数（使用负值表示没有限制）
        max-active: 100
        # 连接池中的最大空闲连接
        max-idle: 20
        # 连接池中的最小空闲连接
        min-idle: 2
        # max-wait属性，单位： 毫秒
        max-wait: 1000000
```
</br>

> 2.3 Redis单节点配置：使用密码+使用SSL（适用于自搭建redis或AWS ElastiCache单节点服务）
```java
spring:
  redis:
    # redis服务地址
    host: 127.0.0.1
	# 端口号
    port: 6379
	# 数据库号，不同的数据库中数据隔离保存
    database: 0
	# 密码
    password: 123456abcd
	# 是否使用ssl
    ssl: true
    # 连接超时timeout属性，单位： 毫秒
    timeout: 10000
    lettuce:
      # 在关闭客户端连接之前等待任务处理完成的最长时间，在这之后，无论任务是否执行完成，都会被执行器关闭，默认100ms
      shutdown-timeout: 100
      pool:
        # 连接池最大连接数（使用负值表示没有限制）
        max-active: 100
        # 连接池中的最大空闲连接
        max-idle: 20
        # 连接池中的最小空闲连接
        min-idle: 2
        # max-wait属性，单位： 毫秒
        max-wait: 1000000
```
</br>

> 2.4 Redis集群配置：使用密码，不使用SSL（适用于自搭建redis或AWS ElastiCache集群服务）
```java
spring:
  redis:
	# 密码
    password: 123456abcd
	# 集群节点
    cluster:
      nodes: 127.0.0.1:6380,127.0.0.1:6381,127.0.0.1:6382,127.0.0.1:6383,127.0.0.1:6384,127.0.0.1:6385
    # 连接超时timeout属性，单位： 毫秒
    timeout: 10000
    lettuce:
      # 在关闭客户端连接之前等待任务处理完成的最长时间，在这之后，无论任务是否执行完成，都会被执行器关闭，默认100ms
      shutdown-timeout: 100
      pool:
        # 连接池最大连接数（使用负值表示没有限制）
        max-active: 100
        # 连接池中的最大空闲连接
        max-idle: 20
        # 连接池中的最小空闲连接
        min-idle: 2
        # max-wait属性，单位： 毫秒
        max-wait: 1000000
```
</br>

> 2.5 Redis集群配置：使用密码+使用SSL（适用于自搭建redis或AWS ElastiCache集群服务）
```java
spring:
  redis:
    # 密码
    password: 123456abcd
    # 是否使用ssl
    ssl: true
    # 集群节点
    cluster:
      nodes: xxxxxxx:6379
    # 连接超时timeout属性，单位： 毫秒
    timeout: 10000
    lettuce:
      # 在关闭客户端连接之前等待任务处理完成的最长时间，在这之后，无论任务是否执行完成，都会被执行器关闭，默认100ms
      shutdown-timeout: 100
      pool:
        # 连接池最大连接数（使用负值表示没有限制）
        max-active: 100
        # 连接池中的最大空闲连接
        max-idle: 20
        # 连接池中的最小空闲连接
        min-idle: 2
        # max-wait属性，单位： 毫秒
        max-wait: 1000000
```
</br>

### 3、注册redisTemplate bean
```java
@Configuration
public class RedisConfig {
    /**
     * Springboot 2.x默认内置lettuce作为redis客户端，因此配置属性可以自动装配
     * 构建集群redis客户端及RedisTemplate
     * 仅在生产环境生效
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        log.info("creating redisTemplate");
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        //使用Jackson2JsonRedisSerializer替换默认的JdkSerializationRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(om);

        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        log.info("create redisTemplate success");
        return template;
    }
}
```

</br>

### 4、编写RedisUtil工具类
```java
@Slf4j
@Component
public final class RedisUtil {


    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    // =============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("expire execute error", e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("hasKey execute error", e);
            return false;
        }
    }

    /**
     * 删除一个或多个缓存
     *
     * @param key 可以传一个值或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        try {
            if (key != null && key.length > 0) {
                if (key.length == 1) {
                    redisTemplate.delete(key[0]);
                } else {
                    redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
                }
            }
        } catch (Exception e) {
            log.error("del key execute error", e);
        }
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        try {
            return key == null ? null : redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("get key execute error", e);
            return null;
        }
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("set key execute error", e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("set key with expire time execute error", e);
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        try {
            return redisTemplate.opsForValue().increment(key, delta < 0 ? 1 : delta);
        } catch (Exception e) {
            log.error("incr execute error", e);
            return -1;
        }
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        try {
            return redisTemplate.opsForValue().increment(key, -(delta < 0 ? 1 : delta));
        } catch (Exception e) {
            log.error("incr execute error", e);
            return -1;
        }
    }

    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("hmset key execute error", e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("hmset key with expire time execute error", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("hset key execute error", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("hset key with expire time execute error", e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("sGet key execute error", e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("sHasKey key execute error", e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("sSet keys execute error", e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error("sSetAndTime execute error", e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("sGetSetSize execute error", e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            log.error("setRemove execute error", e);
            return 0;
        }
    }
    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("redisTemplate execute error", e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("lGetListSize execute error", e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("lGetIndex execute error", e);
            return null;
        }
    }

    /**
     * 将对象放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("lSet Object execute error", e);
            return false;
        }
    }

    /**
     * 将对象放入缓存，并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("lSet Object with expires execute error", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error("lSet List execute error", e);
            return false;
        }
    }

    /**
     * 将list放入缓存，并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("lSet List with expires execute error", e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("lUpdateIndex execute error", e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            log.error("lRemove execute error", e);
            return 0;
        }
    }
}
```
</br>

### 5、测试
```java
@SpringBootApplication
@RestController
public class RedisAuthExample {
    public static void main(String[] args) {
        SpringApplication.run(RedisAuthExample.class, args);
    }

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/get/{key}")
    public String get(@PathVariable String key) {
        return (String) redisUtil.get(key);
    }

    @GetMapping("/set/{key}/{value}")
    public String set(@PathVariable String key, @PathVariable String value) {
        redisUtil.set(key, value, 3600);
        return (String) redisUtil.get(key);
    }
}
```