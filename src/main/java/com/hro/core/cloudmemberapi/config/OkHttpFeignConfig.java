package com.hro.core.cloudmemberapi.config;

import com.hro.core.cloudmemberapi.common.SpringContext;
import feign.Client;
import feign.okhttp.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(OkHttpClient.class)
@ConditionalOnMissingClass("com.netflix.loadbalancer.ILoadBalancer")
@ConditionalOnProperty(value = "feign.okhttp.enabled", matchIfMissing = true)
public class OkHttpFeignConfig {

    @Bean
    @ConditionalOnMissingBean(Client.class)
    public Client feignClient() {
        okhttp3.OkHttpClient okHttpClient = SpringContext.getBean(okhttp3.OkHttpClient.class);
        if (okHttpClient != null) {
            return new OkHttpClient(okHttpClient);
        }
        return new OkHttpClient();
    }

}
