package io.github.xiapxx.starter.caffeinewrapcache.cache;

import org.springframework.util.Assert;
import java.util.Collection;

/**
 * 缓存持有者
 *
 * @Author xiapeng
 * @Date 2025-04-02 14:03
 */
public class CaffeineWrapCacheHolder {

    private static CaffeineWrapCacheManager CACHE_MANAGER;

    /**
     * 设置缓存管理器
     *
     * @param caffeineWrapCacheManager caffeineWrapCacheManager
     */
    static void setCacheManager(CaffeineWrapCacheManager caffeineWrapCacheManager) {
        CACHE_MANAGER = caffeineWrapCacheManager;
    }

    private static void checkIsInit(){
        Assert.notNull(CACHE_MANAGER, "缓存未加载");
    }

    /**
     * 获取缓存值
     *
     * @param name 缓存名称
     * @param key 缓存key
     * @return 值
     */
    public static Object get(String name, Object key) {
        checkIsInit();
        return CACHE_MANAGER.getCaffeineWrapCache(name).getInner(key);
    }

    /**
     * 获取缓存值
     *
     * @param name 缓存名称
     * @param key 缓存key
     * @param type 值类型
     * @return 值
     */
    public static <T> T get(String name, Object key, Class<T> type) {
        checkIsInit();
        return CACHE_MANAGER.getCaffeineWrapCache(name).get(key, type);
    }

    /**
     * 设置缓存
     *
     * @param name 缓存名称
     * @param key 缓存key
     * @param value 缓存值
     */
    public static void put(String name, Object key, Object value) {
        checkIsInit();
        CACHE_MANAGER.getCaffeineWrapCache(name).put(key, value);
    }

    /**
     * 缓存删除(本地caffeine缓存和被嵌套的缓存)
     *
     * @param name 缓存名称
     * @param key 缓存key
     */
    public static void remove(String name, Object key) {
        checkIsInit();
        CACHE_MANAGER.getCaffeineWrapCache(name).evict(key);
    }

    /**
     * 缓存清空(本地caffeine缓存和被嵌套的缓存)
     *
     * @param name 缓存名称
     */
    public static void removeAll(String name) {
        checkIsInit();
        CACHE_MANAGER.getCaffeineWrapCache(name).clear();
    }

    /**
     * 仅仅删除本地缓存(本地caffeine缓存)
     *
     * @param name name
     * @param key key
     */
    public static void removeLocal(String name, Object key) {
        checkIsInit();
        CACHE_MANAGER.getCaffeineWrapCache(name).evictLocal(key);
    }

    /**
     * 缓存清空(本地caffeine缓存)
     *
     * @param name name
     */
    public static void removeAllLocal(String name) {
        checkIsInit();
        CACHE_MANAGER.getCaffeineWrapCache(name).clearLocal();
    }

    /**
     * 清空整个应用的缓存(本地caffeine缓存和被嵌套的缓存)
     */
    public static void removeAll() {
        checkIsInit();
        Collection<String> cacheNames = CACHE_MANAGER.getCacheNames();
        if(cacheNames == null || cacheNames.isEmpty()){
            return;
        }
        for (String cacheName : cacheNames) {
            removeAll(cacheName);
        }
    }

    /**
     * 清空整个应用的本地缓存(本地caffeine缓存)
     */
    public static void removeAllLocal() {
        checkIsInit();
        Collection<String> cacheNames = CACHE_MANAGER.getCacheNames();
        if(cacheNames == null || cacheNames.isEmpty()){
            return;
        }
        for (String cacheName : cacheNames) {
            removeAllLocal(cacheName);
        }
    }
}
