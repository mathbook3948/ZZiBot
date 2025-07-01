package com.github.mathbook3948.zzibot.service;

import com.github.mathbook3948.zzibot.job.CheckChzzkLiveStatusJob;
import com.github.mathbook3948.zzibot.mapper.LiveSubscriptionMapper;
import jakarta.annotation.PostConstruct;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChzzkService {

    @Autowired
    private LiveSubscriptionMapper liveSubscriptionMapper;

    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void init() {
        try {
            List<String> channelIds = liveSubscriptionMapper.selectLiveSubscriptionDistinct();
            for (String channelId : channelIds) {
                JobDataMap dataMap = new JobDataMap();
                dataMap.put("channelId", channelId);

                JobDetail jobDetail = JobBuilder.newJob(CheckChzzkLiveStatusJob.class)
                        .withIdentity("job_" + channelId, "liveGroup")
                        .usingJobData(dataMap)
                        .build();

                Trigger trigger = TriggerBuilder.newTrigger()
                        .forJob(jobDetail)
                        .withIdentity("trigger_" + channelId, "liveGroup")
                        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(180)
                                .repeatForever())
                        .build();

                scheduler.scheduleJob(jobDetail, trigger);
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception in ChzzkService", e);
        }
    }
}
