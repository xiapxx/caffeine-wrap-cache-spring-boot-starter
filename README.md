# caffeine-wrap-cache-spring-boot-starter

# 使用前提
    1.使用@EnableCaching开启缓存注解(@Cacheable, @CacheEvict...)
    2.spring.cache.type=REDIS或其他非本地缓存

# 使用场景
    以redis举例, 在请求密集的业务场景下, 本地应用频繁的请求redis, 相同的key的数据频繁序列化成一个新的对象(虽然内容相同), 导致内存极度浪费, 从而导致内存溢出。
    针对这种场景, 可以引入该组件。该组件会使用caffeine来封装redis缓存。
    一段时间内, 相同key的数据都存入caffeine, 相同key只使用一个对象。

# 如何使用？
## 引入依赖
~~~~xml
<dependency>
    <groupId>io.github.xiapxx</groupId>
    <artifactId>caffeine-wrap-cache-spring-boot-starter</artifactId>
    <version>1.0.9</version>
</dependency>
~~~~
## 开启缓存
    @EnableCaching
    @Configuration
    public class XXConfiguration{
        ...
    }  

# 缓存注解的使用
   自行百度@Cacheable, @CacheEvict的使用, 该组件并未修改缓存注解的行为。

# CaffeineWrapCacheHolder的使用
* CaffeineWrapCacheHolder.get(缓存名称, 缓存key): 获取缓存值
* CaffeineWrapCacheHolder.get(缓存名称, 缓存key, 返回值类型): 获取缓存值
* CaffeineWrapCacheHolder.remove(缓存名称, 缓存key): 删除缓存(本地caffeine缓存和被嵌套的缓存)
* CaffeineWrapCacheHolder.removeLocal(缓存名称, 缓存key): 删除缓存(本地caffeine缓存)
* CaffeineWrapCacheHolder.removeAll(缓存名称): 删除所有缓存(本地caffeine缓存和被嵌套的缓存)
* CaffeineWrapCacheHolder.removeAllLocal(缓存名称): 删除所有缓存(本地caffeine缓存)
* CaffeineWrapCacheHolder.removeAll(): 删除整个应用的缓存(本地caffeine缓存和被嵌套的缓存)
* CaffeineWrapCacheHolder.removeAllLocal(): 删除整个应用的缓存(本地caffeine缓存)
* CaffeineWrapCacheHolder.put(缓存名称, 缓存key, 缓存值): 设置缓存值

# 配置
caffeine.wrap.cache.maxSize=5000 // caffeine最大存储的对象个数, 默认5000
caffeine.wrap.cache.expire=60S // 过期时间, 默认60秒; 如果60秒没有读缓存, 并且60秒没写缓存时, 自动过期
caffeine.wrap.cache.excludeClass[n]  // 希望排除哪些缓存管理器, 排除的缓存管理器将不封装, 默认空; 例如org.springframework.cache.support.SimpleCacheManager
caffeine.wrap.cache.threadPool.number=-1 // 线程池的核心线程数; 等于0或null: 线程数=cpu核心数, 等于-1(默认值): 线程数=(cpu核心数/2 + 1),  大于0: 线程数=配置的值, 小于-1: 线程数=1
caffeine.wrap.cache.threadPool.queueSize=1000  // 线程池的阻塞队列个数, 默认1000


