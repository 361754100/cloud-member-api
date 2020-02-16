package com.hro.core.cloudmemberapi.controller;

import com.hro.core.cloudmemberapi.feign.VerifyApiFeign;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MemberController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    VerifyApiFeign verifyApiFeign;

    @HystrixCommand(fallbackMethod = "testCallBack",
            commandProperties = @HystrixProperty(
                    name = "execution.isolation.strategy",
                    value = "THREAD")
    )
    @GetMapping("/member/test")
    public String testMember() {
        String msg = "the result is ...";
//        boolean isOk = restTemplate.getForObject("http://localhost:8086/rsa/test", Boolean.class);
        boolean isOk = restTemplate.getForObject("http://verify-api/rsa/test", Boolean.class);
        return msg + String.valueOf(isOk);
    }

    @GetMapping("/member/test2")
    public String testFeignHystrix() {
        String msg = "the result is ...";
        boolean isOk = verifyApiFeign.callTest();
//        msg = msg + String.valueOf(isOk);
        msg = msg + verifyApiFeign.callTest2();

        logger.debug("msg = {}", msg);

        return msg;
    }
}
