package io.github.xiapxx.starter.caffeinewrapcache.properties;

/**
 * @Author xiapeng
 * @Date 2025-03-27 15:42
 */
public class CaffeineThreadPoolConfig {

    /**
     * 核心线程个数
     * 等于0或null:   线程数=cpu核心数
     * 等于-1:       线程数=(cpu核心数/2 + 1)
     * 大于0:        线程数=配置的值
     * 小于-1:       线程数=1
     */
    private Integer number = -1;

    private Integer queueSize = 1000;

    void init() {
        loadActualNumber();
    }

    private void loadActualNumber() {
        int cpuCores = Runtime.getRuntime().availableProcessors();
        if(number == null || number.intValue() == 0){
            number = cpuCores;
            return;
        }

        if(number > 0){
            return;
        }

        if(number.intValue() == -1){
            number = (cpuCores / 2) + 1;
            return;
        }

        if(number.intValue() < -1){
            number = 1;
        }
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

}
