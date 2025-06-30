package com.github.mathbook3948.zzibot.job;

import com.github.mathbook3948.zzibot.chzzk.ChzzkLiveStatusClient;
import com.github.mathbook3948.zzibot.dto.chzzk.LiveStatusResponse;
import com.github.mathbook3948.zzibot.mapper.ChzzkLiveStatusMapper;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CheckChzzkLiveStatusJob implements Job {

    @Autowired
    private ChzzkLiveStatusMapper chzzkLiveStatusMapper;

    @Autowired
    private ChzzkLiveStatusClient chzzkLiveStatusClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            String channelId = dataMap.getString("channelId");

            LiveStatusResponse res = chzzkLiveStatusClient.getLiveStatus(channelId);

            Map<String, Object> map = chzzkLiveStatusMapper.selectChzzkLiveStatus(Map.of("channelId", channelId));

            if(map == null) {
                chzzkLiveStatusMapper.insertLiveStatus(res.getContent());
            } else {
                chzzkLiveStatusMapper.updateChzzkLiveStatus(res.getContent());
            }

            //TODO CLOSE => OPEN 일경우 알림 보내기
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }
}
