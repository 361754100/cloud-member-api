package com.hro.core.cloudmemberapi.service;

import com.hro.core.cloudmemberapi.hystrix.VerifyApiThreadCommand;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    public String test() {
        VerifyApiThreadCommand verifyApiCommand = new VerifyApiThreadCommand();
        String msg = (String) verifyApiCommand.execute();
        return msg;
    }

}
