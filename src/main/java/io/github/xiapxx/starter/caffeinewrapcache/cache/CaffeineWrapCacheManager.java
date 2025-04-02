package io.github.xiapxx.starter.caffeinewrapcache.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author xiapeng
 * @Date 2025-03-28 11:19
 */
public class CaffeineWrapCacheManager implements CacheManager {

    private final Map<String, CaffeineWrapCache> cacheMap = new ConcurrentHashMap<>();

    private CacheManager actualCacheManager;

    private Caffeine<Object, Object> caffeine;

    public CaffeineWrapCacheManager(CacheManager actualCacheManager,
                                    Caffeine<Object, Object> caffeine) {
        this.actualCacheManager = actualCacheManager;
        this.caffeine = caffeine;
    }

    @Override
    public Cache getCache(String name) {
        return getCaffeineWrapCache(name);
    }

    CaffeineWrapCache getCaffeineWrapCache(String name) {
        CaffeineWrapCache caffeineWrapCache = cacheMap.get(name);
        if(caffeineWrapCache != null){
            return caffeineWrapCache;
        }
        return cacheMap.computeIfAbsent(name, key -> new CaffeineWrapCache(actualCacheManager.getCache(key), caffeine));
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheMap.keySet();
    }
}
