package com.hro.core.cloudmemberapi.hystrix;

import com.hro.core.cloudmemberapi.common.SpringContext;
import com.netflix.hystrix.*;
import org.springframework.web.client.RestTemplate;

/**
 *  HystrixCommand
 *  线程隔离
 */
public class VerifyApiThreadCommand extends HystrixCommand {

    RestTemplate restTemplate;

    public VerifyApiThreadCommand() {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("verifyApi-threadGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("verify-api-thread-command"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerEnabled(true)
                        .withCircuitBreakerErrorThresholdPercentage(50)
                        .withExecutionTimeoutEnabled(true)
                        .withExecutionTimeoutInMilliseconds(3*1000)
                        .withFallbackEnabled(true)
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
//                        .withRequestCacheEnabled(true)
                ).andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
//                        .withCoreSize(15)
                        .withCoreSize(2)
                        .withMaximumSize(15)
                        .withMaxQueueSize(15)
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
