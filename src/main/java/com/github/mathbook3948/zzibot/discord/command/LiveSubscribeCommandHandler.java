package com.github.mathbook3948.zzibot.discord.command;

import com.github.mathbook3948.zzibot.dto.zzibot.LiveSubscription;
import com.github.mathbook3948.zzibot.enums.DiscordLogEventContent;
import com.github.mathbook3948.zzibot.exception.DuplicateLiveSubscriptionException;
import com.github.mathbook3948.zzibot.exception.LiveSubscriptionException;
import com.github.mathbook3948.zzibot.exception.LiveSubscriptionLimitExceededException;
import com.github.mathbook3948.zzibot.job.CheckChzzkLiveStatusJob;
import com.github.mathbook3948.zzibot.mapper.LiveSubscriptionMapper;
import com.github.mathbook3948.zzibot.util.ChzzkUtil;
import com.github.mathbook3948.zzibot.util.DiscordUtil;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.util.Permission;
import jakarta.annotation.PostConstruct;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.util.List;

@Component
public class LiveSubscribeCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(LiveSubscribeCommandHandler.class);

    private final String COMMAND = "알림등록";

    @Autowired
    private GatewayDiscordClient client;

    @Autowired
    private LiveSubscriptionMapper liveSubscriptionMapper;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    @Lazy
    private LiveSubscribeCommandHandler self;

    @Autowired
    private DiscordUtil discordUtil;

    @PostConstruct
    public void init() {
        client.on(ChatInputInteractionEvent.class)
                .filter(event -> event.getCommandName().equals(COMMAND))
                .flatMap(this::handle)
                .subscribe();
    }

    private Mono<Void> handle(ChatInputInteractionEvent event) {
        return Mono.justOrEmpty(event.getInteraction().getMember())
                .flatMap(member -> member.getBasePermissions()
                        .flatMap(perms -> {
                            if (!perms.contains(Permission.ADMINISTRATOR)) {
                                discordUtil.insertDiscordLog(event, false, COMMAND, DiscordLogEventContent.REQUIRE_ADMINISTRATOR);
                                return event.reply("관리자만 사용할 수 있습니다.").withEphemeral(true);
                            }

                            return event.getInteraction().getChannel()
                                    .ofType(MessageChannel.class)
                                    .flatMap(channel -> {
                                        String message;
                                        try {
                                            self.handletransactional(event);
                                            discordUtil.insertDiscordLog(event, true, COMMAND, "");
                                            message = "구독에 성공하였습니다.";
                                        } catch (LiveSubscriptionException lslee) {
                                            discordUtil.insertDiscordLog(event, false, COMMAND, lslee.getMessage());
                                            message = lslee.getMessage();
                                        } catch (Exception e) {
                                            discordUtil.insertDiscordLog(event, false, COMMAND, e.getMessage());
                                            logger.error("error in LiveSubscribeCommandHandler: {}", e.getMessage());
                                            message = "알 수 없는 오류가 발생하였습니다.";
                                        }

                                        return event.reply()
                                                .withEphemeral(true)
                                                .withContent(message);
                                    });
                        })
                );
    }

    @Transactional
    public String handletransactional(ChatInputInteractionEvent event) throws Exception {
        String url = event.getOption("url")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        String guildId = event.getInteraction().getGuildId().map(Snowflake::asString).orElse(null);

        String channelId = ChzzkUtil.extractChannelIdFromUrl(url);

        LiveSubscription ls = LiveSubscription.builder().guildId(guildId).channelId(channelId).build();

        List<LiveSubscription> list = liveSubscriptionMapper.selectLiveSubscriptionWithGuildID(ls);

        if (list.size() >= 5) {
            throw new LiveSubscriptionLimitExceededException(null);
        }

        for(LiveSubscription liveSubscription : list) {
            if(liveSubscription.getChannelId().equals(channelId)) {
                throw new DuplicateLiveSubscriptionException(null);
            }
        }

        liveSubscriptionMapper.insertLiveSubscription(ls);
        registerJob(channelId);

        return "구독에 성공하였습니다.";
    }

    private void registerJob(String channelId) {
        Mono.fromRunnable(() -> {
            try {
                JobKey jobKey = JobKey.jobKey("job_" + channelId, "liveGroup");
                if (scheduler.checkExists(jobKey)) return;

                JobDataMap dataMap = new JobDataMap();
                dataMap.put("channelId", channelId);

                JobDetail jobDetail = JobBuilder.newJob(CheckChzzkLiveStatusJob.class)
                        .withIdentity(jobKey)
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
            } catch (Exception e) {
                logger.error("Job 등록 실패: {}", e.getMessage());
            }
        }).subscribeOn(Schedulers.boundedElastic()).subscribe();
    }
}
