package com.hro.core.cloudmemberapi.config;

import feign.Logger;
import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 有时候我们遇到Bug，比如接口调用失败、参数没收到等
 * 问题，或者想看看调用性能，就需要配置Feign的日志了
 * ，以此让Feign把请求信息输出来。
 */
@Configuration
public class FeignConfig {

    /**
     * Feign客户端的日志级别
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * 通过Options可以配置连接超时时间和读取超时时间
     * @return
     */
    @Bean
    public Request.Options options(){
        return new Request.Options(5000, 10000);
    }

}
