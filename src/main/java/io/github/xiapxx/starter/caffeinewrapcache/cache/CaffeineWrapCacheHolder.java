package io.github.xiapxx.starter.caffeinewrapcache.cache;

import org.springframework.util.Assert;

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
     * 缓存删除
     *
     * @param name 缓存名称
     * @param key 缓存key
     */
    public static void remove(String name, Object key) {
        checkIsInit();
        CACHE_MANAGER.getCaffeineWrapCache(name).evict(key);
    }

    /**
     * 缓存清空
     *
     * @param name 缓存名称
     */
    public static void removeAll(String name) {
        checkIsInit();
        CACHE_MANAGER.getCaffeineWrapCache(name).clear();
    }


}
