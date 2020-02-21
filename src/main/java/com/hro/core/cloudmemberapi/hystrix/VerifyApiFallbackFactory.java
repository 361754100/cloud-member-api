package com.hro.core.cloudmemberapi.hystrix;

import com.hro.core.cloudmemberapi.feign.VerifyApiClient;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class VerifyApiFallbackFactory implements FallbackFactory<VerifyApiClient> {

    private static Logger logger = LoggerFactory.getLogger(VerifyApiFallbackFactory.class);

    @Override
    public VerifyApiClient create(Throwable throwable) {

        logger.error("Fallback error...", throwable);

        return new VerifyApiClient(){

            @Override
            public boolean callTest() {
                return false;
            }

            @Override
            public String callTest2() {
                return "it is fail...";
            }
        };
    }
}
