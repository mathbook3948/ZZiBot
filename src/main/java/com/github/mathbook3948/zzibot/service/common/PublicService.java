package com.github.mathbook3948.zzibot.service.common;

import com.github.mathbook3948.zzibot.mapper.HealthCheckerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicService {

    @Autowired
    private HealthCheckerMapper healthCheckerMapper;

    public String now() {
        return healthCheckerMapper.now();
    }
}
