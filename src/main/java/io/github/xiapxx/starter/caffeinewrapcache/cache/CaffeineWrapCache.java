package io.github.xiapxx.starter.caffeinewrapcache.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.cache.support.NullValue;
import org.springframework.cache.support.SimpleValueWrapper;
import java.util.concurrent.Callable;

/**
 * @Author xiapeng
 * @Date 2025-03-28 11:24
 */
public class CaffeineWrapCache implements Cache {

    private Cache actualCache;

    private LoadingCache<Object, Object> loadingCache;

    private boolean allowNullValues = true; // 默认true; 跟随被封装的缓存的策略

    public CaffeineWrapCache(Cache actualCache, Caffeine<Object, Object> caffeine) {
        this.actualCache = actualCache;
        if (actualCache instanceof AbstractValueAdaptingCache) {
            this.allowNullValues = ((AbstractValueAdaptingCache)actualCache).isAllowNullValues();
        }
        this.loadingCache = caffeine.build(key -> getActual(key));
    }

    private Object getActual(Object key) {
        ValueWrapper valueWrapper = actualCache.get(key);
        if (valueWrapper == null) {
           return null;
        }
        return valueWrapper.get() == null || valueWrapper.get() == NullValue.INSTANCE
                ? (allowNullValues ? NullValue.INSTANCE : null) : valueWrapper.get();
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
        return value == null ? null : new SimpleValueWrapper(value == NullValue.INSTANCE ? null : value);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Object value = getInner(key);
        if(value == null || value == NullValue.INSTANCE){
            return null;
        }
        if (type != null && !type.isInstance(value)) {
            throw new IllegalStateException(
                    "Cached value is not of required type [" + type.getName() + "]: " + value);
        }
        return (T) value;
    }


    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object value = getInner(key);
        if(value == null || value == NullValue.INSTANCE){
            try {
                value = valueLoader.call();
                put(key, value);
                return value == null || value == NullValue.INSTANCE ? null : (T) value;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return (T) value;
    }

    @Override
    public void put(Object key, Object value) {
        if(!allowNullValues && (value == null || value == NullValue.INSTANCE)){
            evict(key);
            return;
        }
        actualCache.put(key, value);
        loadingCache.put(key, value == null ? NullValue.INSTANCE : value);
    }

    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper valueWrapper = get(key);
        if(valueWrapper == null || valueWrapper.get() == null){
            put(key, value);
            return null;
        }
        return valueWrapper;
    }

    @Override
    public void evict(Object key) {
        actualCache.evict(key);
        loadingCache.invalidate(key);
    }

    /**
     * 仅仅删除本地缓存
     *
     * @param key key
     */
    void evictLocal(Object key) {
        loadingCache.invalidate(key);
    }

    @Override
    public void clear() {
        actualCache.clear();
        loadingCache.invalidateAll();
    }

    /**
     * 仅仅删除本地缓存
     */
    void clearLocal(){
        loadingCache.invalidateAll();
    }

}
