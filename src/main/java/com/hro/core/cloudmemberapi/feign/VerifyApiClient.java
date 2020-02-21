package com.hro.core.cloudmemberapi.feign;

import com.hro.core.cloudmemberapi.config.FeignConfig;
import com.hro.core.cloudmemberapi.hystrix.VerifyApiFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * verify-api的 Feign客户端
 * 指定配置类为 FeignConfig.class
 */
@FeignClient(value = "verify-api",
        configuration = FeignConfig.class,
        fallbackFactory = VerifyApiFallbackFactory.class)
public interface VerifyApiClient {

    @GetMapping("/rsa/test")
    boolean callTest();

    @GetMapping("/rsa/test")
    String callTest2();
}
