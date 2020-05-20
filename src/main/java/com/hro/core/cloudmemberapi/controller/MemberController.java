package com.hro.core.cloudmemberapi.controller;

import com.alibaba.fastjson.JSON;
import com.hro.core.cloudmemberapi.feign.VerifyApiClient;
import com.hro.core.cloudmemberapi.hystrix.VerifyApiSemaphoreCommand;
import com.hro.core.cloudmemberapi.hystrix.VerifyApiThreadCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RestController
public class MemberController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    VerifyApiClient verifyApiFeign;

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

    @GetMapping("/member/test3")
    public String testHystrixThreadCommand() {
        String msg = "";
        // 触发线程池满了，被拒绝的情况
        for(int i = 0; i < 10; i++) {
            try {
                Future<String> future = new VerifyApiThreadCommand().queue();
                logger.info("===future========" + future);
            } catch(Exception e) {
                logger.info("run()抛出HystrixBadRequestException时，被捕获到这里" + e.getCause());
            }
        }

        for(int i = 0; i < 20; i++) {
            try {
                msg = (String) new VerifyApiThreadCommand().execute();
                logger.info("===execute========" + msg);
            } catch(Exception e) {
                logger.info("run()抛出HystrixBadRequestException时，被捕获到这里" + e.getCause());
            }
        }

        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        }catch(Exception e) {}
        logger.info("------开始打印现有线程---------");
        Map<Thread, StackTraceElement[]> map=Thread.getAllStackTraces();
        for (Thread thread : map.keySet()) {
            logger.info("thread name..."+ thread.getName());
        }
        logger.info("map info..."+ JSON.toJSONString(map));
        logger.info("thread num..." + map.size());

        return msg;
    }

    @GetMapping("/member/test4")
    public String testHystrixSemaphoreCommand() {
        String msg = "test ok...";
        // 触发被拒绝的情况
        try {
            Thread.sleep(2000);
            for(int i = 0; i < 10; i++) {
                final int j = i;
                // 自主创建线程来执行command，创造并发场景
                Thread thread = new Thread(new Runnable() {
                    //	                    @Override
                    public void run() {
                        logger.info("=========== before execute ["+ j +"]==============");
                        logger.info("=========== tmpMsg ["+ j +"] = " + new VerifyApiSemaphoreCommand().execute());	// 被信号量拒绝的线程从这里抛出异常
                        logger.info("=========== after execute ["+ j +"]=============="); // 被信号量拒绝的线程，将不能执行到这里
                    }
                });
                thread.start();
            }
        } catch(Exception e) {
            logger.error("execute error...", e);
        }

        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        }catch(Exception e) {}
        logger.info("------开始打印现有线程---------");
        Map<Thread, StackTraceElement[]> map=Thread.getAllStackTraces();
        for (Thread thread : map.keySet()) {
            logger.info("thread name..."+ thread.getName());
        }
        logger.info("map info..."+ JSON.toJSONString(map));
        logger.info("thread num..." + map.size());

        return msg;
    }
}

