package io.github.xiapxx.starter.caffeinewrapcache.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import java.util.concurrent.Callable;

/**
 * @Author xiapeng
 * @Date 2025-03-28 11:24
 */
public class CaffeineWrapCache implements Cache {

    private Cache actualCache;

    private LoadingCache<Object, Object> loadingCache;

    public CaffeineWrapCache(Cache actualCache, Caffeine<Object, Object> caffeine) {
        this.actualCache = actualCache;
        this.loadingCache = caffeine.build(key -> getActual(key));
    }

    private Object getActual(Object key) {
        ValueWrapper valueWrapper = actualCache.get(key);
        if (valueWrapper == null) {
           return null;
        }
        return valueWrapper.get();
    }

    Object getInner(Object key) {
        return loadingCache.get(key);
    }

    @Override
    public String getName() {
        return actualCache.getName();
    }

    @Override
    public Object getNativeCache() {
        return loadingCache;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object value = getInner(key);
        return value == null ? null : new SimpleValueWrapper(value);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Object value = getInner(key);
        if (value != null && type != null && !type.isInstance(value)) {
            throw new IllegalStateException(
                    "Cached value is not of required type [" + type.getName() + "]: " + value);
        }
        return (T) value;
    }


    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        T value = (T) getInner(key);
        if(value == null){
            try {
                value = valueLoader.call();
                put(key, value);
                return value;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return value;
    }

    @Override
    public void put(Object key, Object value) {
        actualCache.put(key, value);
        loadingCache.put(key, value);
    }

    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper result = get(key);
        if(result == null || result.get() == null){
            put(key, value);
            return null;
        }
        return result;
    }

    @Override
    public void evict(Object key) {
        actualCache.evict(key);
        loadingCache.invalidate(key);
    }

    @Override
    public void clear() {
        actualCache.clear();
        loadingCache.invalidateAll();
    }


}
