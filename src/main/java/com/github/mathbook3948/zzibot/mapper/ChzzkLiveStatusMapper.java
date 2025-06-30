package com.github.mathbook3948.zzibot.mapper;

import com.github.mathbook3948.zzibot.dto.chzzk.LiveStatusResponseContent;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface ChzzkLiveStatusMapper {

    void insertLiveStatus(LiveStatusResponseContent params) throws Exception;

    Map<String, Object> selectChzzkLiveStatus(Map<String, Object> params) throws Exception;

    void updateChzzkLiveStatus(LiveStatusResponseContent params) throws Exception;
}
