package com.github.mathbook3948.zzibot.mapper;

import com.github.mathbook3948.zzibot.dto.zzibot.log.DiscordLogDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiscordLogMapper {

    void insertDiscordLog(DiscordLogDTO params);
}
