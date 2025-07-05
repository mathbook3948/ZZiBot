package com.github.mathbook3948.zzibot.mapper;

import com.github.mathbook3948.zzibot.dto.zzibot.LiveSubscription;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LiveSubscriptionMapper {

    void insertLiveSubscription(LiveSubscription params) throws Exception;

    List<LiveSubscription> selectLiveSubscriptionWithGuildID(LiveSubscription params) throws Exception;

    List<String> selectLiveSubscriptionDistinct() throws Exception;

    List<Map<String, Object>> selectLiveSubscriptionByChannelId(Map<String, Object> params) throws Exception;

    List<Map<String, Object>> selectLiveSubscriptionWithGuildIDAndSearch(Map<String, Object> params) throws Exception;

    void deleteLiveSubscriptionWithChannelId(Map<String, Object> params) throws Exception;

    int selectLiveSubscriptionCount() throws Exception;

}
