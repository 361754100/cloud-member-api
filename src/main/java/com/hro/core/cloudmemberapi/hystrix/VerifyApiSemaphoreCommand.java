package com.hro.core.cloudmemberapi.hystrix;

import com.hro.core.cloudmemberapi.common.SpringContext;
import com.netflix.hystrix.*;
import org.springframework.web.client.RestTemplate;

/**
 *  HystrixCommand
 *  信号量隔离
 */
public class VerifyApiSemaphoreCommand extends HystrixCommand {

    RestTemplate restTemplate;

    public VerifyApiSemaphoreCommand() {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("verifyApi-semaphoreGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("verify-api-semaphore-command"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerEnabled(true)
                        .withCircuitBreakerErrorThresholdPercentage(50)
                        .withExecutionTimeoutEnabled(true)
                        .withExecutionTimeoutInMilliseconds(4*1000)
                        .withFallbackEnabled(true)
                        // 使用信号量隔离策略
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                        .withExecutionIsolationSemaphoreMaxConcurrentRequests(3)    // 支持的并非请求数
                        .withFallbackIsolationSemaphoreMaxConcurrentRequests(2)     // 失败的最大次数
//                        .withRequestCacheEnabled(true)
                ));
        restTemplate = SpringContext.getBean(RestTemplate.class);
    }

    @Override
    protected String run() throws Exception {
        boolean isOk = restTemplate.getForObject("http://verify-api/rsa/test", Boolean.class);

        String msg = "the result is ..."+ isOk;
        return msg;
    }

    @Override
    protected String getFallback() {
        return "request fall back...";
    }
}
