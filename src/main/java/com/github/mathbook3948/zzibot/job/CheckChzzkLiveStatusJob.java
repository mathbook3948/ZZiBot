package com.github.mathbook3948.zzibot.job;

import com.github.mathbook3948.zzibot.chzzk.ChzzkLiveStatusClient;
import com.github.mathbook3948.zzibot.dto.chzzk.LiveStatusResponse;
import com.github.mathbook3948.zzibot.mapper.ChzzkLiveStatusMapper;
import com.github.mathbook3948.zzibot.mapper.LiveSubscriptionMapper;
import com.github.mathbook3948.zzibot.util.DiscordUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@DisallowConcurrentExecution
public class CheckChzzkLiveStatusJob implements Job {

    @Autowired
    private ChzzkLiveStatusMapper chzzkLiveStatusMapper;

    @Autowired
    private ChzzkLiveStatusClient chzzkLiveStatusClient;

    @Autowired
    private LiveSubscriptionMapper liveSubscriptionMapper;

    @Autowired
    private DiscordUtil discordUtil;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            String channelId = dataMap.getString("channelId");

            LiveStatusResponse res = chzzkLiveStatusClient.getLiveStatus(channelId);

            Map<String, Object> map = chzzkLiveStatusMapper.selectChzzkLiveStatus(Map.of("channelId", channelId));

            if (map == null) {
                chzzkLiveStatusMapper.insertLiveStatus(res.getContent());
            } else {
                chzzkLiveStatusMapper.updateChzzkLiveStatus(res.getContent());

                String status = map.get("status").toString();

                if (status.equalsIgnoreCase("CLOSE") && res.getContent().getStatus().equalsIgnoreCase("OPEN")) {
                    broadcast(channelId);
                }
            }
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    private void broadcast(String channel_id) throws Exception {
        List<Map<String, Object>> list = liveSubscriptionMapper.selectLiveSubscriptionByChannelId(Map.of("channelId", channel_id));

        for (Map<String, Object> map : list) {
            String discordChannelId = map.get("channel_id").toString();

            discordUtil.sendMessageAsync(discordChannelId, "https://chzzk.naver.com/" + channel_id);
        }
    }
}
