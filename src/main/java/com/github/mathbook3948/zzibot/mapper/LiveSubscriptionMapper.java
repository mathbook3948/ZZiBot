package com.github.mathbook3948.zzibot.mapper;

import com.github.mathbook3948.zzibot.dto.zzibot.LiveSubscription;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LiveSubscriptionMapper {

    void insertLiveSubscription(LiveSubscription params) throws Exception;

    List<LiveSubscription> selectLiveSubscriptionWithGuildID(LiveSubscription params) throws Exception;

    List<String> selectLiveSubscriptionDistinct() throws Exception;
}
