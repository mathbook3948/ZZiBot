package com.github.mathbook3948.zzibot.mapper;

import com.github.mathbook3948.zzibot.dto.zzibot.LiveAlarmChannel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LiveAlarmChannelMapper {

    void insertLiveAlarmChannel(LiveAlarmChannel params) throws Exception;
}
