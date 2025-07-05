package com.github.mathbook3948.zzibot.service.common;

import com.github.mathbook3948.zzibot.dto.zzibot.ResponseDTO;
import com.github.mathbook3948.zzibot.dto.zzibot.common.MainContentDTO;
import com.github.mathbook3948.zzibot.mapper.DiscordGuildMapper;
import com.github.mathbook3948.zzibot.mapper.HealthCheckerMapper;
import com.github.mathbook3948.zzibot.mapper.LiveSubscriptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicService {

    private static final Logger logger =  LoggerFactory.getLogger(PublicService.class);

    @Autowired
    private HealthCheckerMapper healthCheckerMapper;

    @Autowired
    private DiscordGuildMapper discordGuildMapper;

    @Autowired
    private LiveSubscriptionMapper liveSubscriptionMapper;

    public String now() {
        return healthCheckerMapper.now();
    }

    public ResponseDTO<MainContentDTO> getMainData() {
        ResponseDTO<MainContentDTO> result =  new ResponseDTO<>();

        try {
            int guildCount = discordGuildMapper.selectDiscordGuildNotDeletedCount();
            int channelCount =liveSubscriptionMapper.selectLiveSubscriptionCount();

            MainContentDTO content = new MainContentDTO();
            content.setGuildCount(guildCount);
            content.setConnectedChannelCount(channelCount);

            result.setContent(content);
            result.setMsg(null);
            result.setResult(true);
        } catch (Exception e) {
            logger.error("error in PublicService.getMainData: {}", e.getMessage());

            result.setContent(null);
            result.setMsg("알 수 없는 오류가 발생했습니다.");
            result.setResult(false);
        }
        
        return result;
    }
}
