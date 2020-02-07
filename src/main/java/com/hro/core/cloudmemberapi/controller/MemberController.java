package com.hro.core.cloudmemberapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MemberController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/member/test")
    public String testMember() {
        String msg = "the result is ...";
//        boolean isOk = restTemplate.getForObject("http://localhost:8086/test", Boolean.class);
        boolean isOk = restTemplate.getForObject("http://verify-api/test", Boolean.class);
        return msg + String.valueOf(isOk);
    }
}
