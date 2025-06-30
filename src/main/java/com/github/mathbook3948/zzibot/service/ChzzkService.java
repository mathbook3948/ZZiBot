package com.github.mathbook3948.zzibot.service;

import com.github.mathbook3948.zzibot.job.CheckChzzkLiveStatusJob;
import com.github.mathbook3948.zzibot.mapper.LiveSubscriptionMapper;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChzzkService {

    private static final Logger logger = LoggerFactory.getLogger(ChzzkService.class);

    public ChzzkService(LiveSubscriptionMapper liveSubscriptionMapper, Scheduler scheduler) {
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
                                .withIntervalInSeconds(120)
                                .repeatForever())
                        .build();

                scheduler.scheduleJob(jobDetail, trigger);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Exception in ChzzkService");
        }
    }
}
