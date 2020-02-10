package com.chenzj36.service;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

@Service //dubbo的service
@Component
public class ProviderServiceImpl implements ProviderService {
    @Override
    public String getTicket() {
        return "票务服务：调用成功~";
    }
}
