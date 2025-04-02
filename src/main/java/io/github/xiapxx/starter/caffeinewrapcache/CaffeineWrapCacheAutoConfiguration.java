package io.github.xiapxx.starter.caffeinewrapcache;

import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.xiapxx.starter.caffeinewrapcache.cache.CaffeineWrapCacheManagerRegister;
import io.github.xiapxx.starter.caffeinewrapcache.properties.CaffeineWrapProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;

/**
 * @Author xiapeng
 * @Date 2025-03-28 11:12
 */
@ConditionalOnClass({Caffeine.class, CacheManager.class})
@EnableConfigurationProperties(CaffeineWrapProperties.class)
public class CaffeineWrapCacheAutoConfiguration {

    @Bean
    public CaffeineWrapCacheManagerRegister caffeineWrapCacheManagerRegister(CaffeineWrapProperties caffeineWrapProperties) {
        return new CaffeineWrapCacheManagerRegister(caffeineWrapProperties);
    }

}
