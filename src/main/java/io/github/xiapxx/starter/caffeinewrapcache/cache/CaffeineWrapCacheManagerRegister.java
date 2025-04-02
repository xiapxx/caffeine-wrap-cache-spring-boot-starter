package io.github.xiapxx.starter.caffeinewrapcache.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.xiapxx.starter.caffeinewrapcache.properties.CaffeineThreadPoolConfig;
import io.github.xiapxx.starter.caffeinewrapcache.properties.CaffeineWrapProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cache.CacheManager;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author xiapeng
 * @Date 2025-03-28 13:11
 */
public class CaffeineWrapCacheManagerRegister implements BeanPostProcessor {

    private CaffeineWrapProperties caffeineWrapProperties;

    private Caffeine<Object, Object> caffeine;

    public CaffeineWrapCacheManagerRegister(CaffeineWrapProperties caffeineWrapProperties) {
        this.caffeineWrapProperties = caffeineWrapProperties;
    }

    private Caffeine getCaffeine() {
        if(caffeine == null){
            CaffeineThreadPoolConfig threadPoolConfig = caffeineWrapProperties.getThreadPool();
            Executor executor = new ThreadPoolExecutor(threadPoolConfig.getNumber(),
                    threadPoolConfig.getNumber(),
                    0, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(threadPoolConfig.getQueueSize()),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.CallerRunsPolicy());
            caffeine = Caffeine.newBuilder()
                    .maximumSize(caffeineWrapProperties.getMaxSize())
                    .expireAfterAccess(caffeineWrapProperties.getExpire())
                    .expireAfterWrite(caffeineWrapProperties.getExpire())
                    .executor(executor);
        }
        return caffeine;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof CacheManager
                && !(bean instanceof CaffeineWrapCacheManager)
        ){
            String beanClassName = bean.getClass().getName();
            if(caffeineWrapProperties.getExcludeClass().contains(beanClassName)){
                return bean;
            }
            CaffeineWrapCacheManager caffeineWrapCacheManager = new CaffeineWrapCacheManager((CacheManager) bean, getCaffeine());
            CaffeineWrapCacheHolder.setCacheManager(caffeineWrapCacheManager);
            return caffeineWrapCacheManager;
        }
        return bean;
    }
}
