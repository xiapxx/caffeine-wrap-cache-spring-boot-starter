package io.github.xiapxx.starter.caffeinewrapcache.properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author xiapeng
 * @Date 2025-03-27 15:39
 */
@ConfigurationProperties(prefix = CaffeineWrapProperties.PREFIX)
public class CaffeineWrapProperties implements InitializingBean {

    public static final String PREFIX = "caffeine.wrap.cache";

    /**
     * caffeine最大存储的对象个数
     */
    private Integer maxSize = 5000;

    /**
     * 过期时间; 默认60秒
     * 如果60秒没访问缓存, 并且60秒没写缓存时自动过期
     */
    private Duration expire = Duration.ofSeconds(60);

    /**
     * 希望排除哪些缓存管理器
     */
    private List<String> excludeClass = new ArrayList<>();

    /**
     * caffeine的线程池配置
     */
    private CaffeineThreadPoolConfig threadPool = new CaffeineThreadPoolConfig();

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public Duration getExpire() {
        return expire;
    }

    public void setExpire(Duration expire) {
        this.expire = expire;
    }

    public CaffeineThreadPoolConfig getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(CaffeineThreadPoolConfig threadPool) {
        this.threadPool = threadPool;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        threadPool.init();
    }

    public List<String> getExcludeClass() {
        return excludeClass;
    }

    public void setExcludeClass(List<String> excludeClass) {
        this.excludeClass = excludeClass;
    }
}
