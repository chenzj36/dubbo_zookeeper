package com.chenzj36.service;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

@Service // springframework下的service
public class UserService {
    @Reference // 定义路径相同的接口名
    ProviderService tickerService;
    public void buyTicket(){
        String ticket = tickerService.getTicket();
        System.out.println(ticket);
    }
}
