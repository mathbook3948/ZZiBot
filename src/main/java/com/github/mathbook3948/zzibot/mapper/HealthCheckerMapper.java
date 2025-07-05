package com.github.mathbook3948.zzibot.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HealthCheckerMapper {
    String now();
}
