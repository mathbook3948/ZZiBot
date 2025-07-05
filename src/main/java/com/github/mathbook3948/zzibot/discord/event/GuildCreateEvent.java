package com.github.mathbook3948.zzibot.discord.event;

import com.github.mathbook3948.zzibot.dto.zzibot.guild.DiscordGuildDTO;
import com.github.mathbook3948.zzibot.dto.zzibot.log.DiscordLogDTO;
import com.github.mathbook3948.zzibot.mapper.DiscordGuildMapper;
import com.github.mathbook3948.zzibot.mapper.DiscordLogMapper;
import com.github.mathbook3948.zzibot.util.DiscordUtil;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Guild;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GuildCreateEvent {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(GuildCreateEvent.class);

    private final String COMMAND = "GUILD_CREATE";

    @Autowired
    private GatewayDiscordClient gatewayDiscordClient;

    @Autowired
    private DiscordGuildMapper discordGuildMapper;

    @Autowired
    private DiscordLogMapper discordLogMapper;

    @PostConstruct
    public void init() {
        gatewayDiscordClient.on(discord4j.core.event.domain.guild.GuildCreateEvent.class)
                .subscribe(event -> {
                    Guild guild = event.getGuild();

                    String guildId = guild.getId().asString();
                    String guildName = guild.getName();

                    DiscordLogDTO log = new DiscordLogDTO();
                    log.setDiscord_log_command(COMMAND);
                    log.setDiscord_log_guild_id(guildId);
                    log.setDiscord_log_channel_id(null);

                    try {
                        List<DiscordGuildDTO> guilds = discordGuildMapper.selectDiscordGuildMapperNotDeleted(DiscordGuildDTO.builder().guild_id(guildId).build());
                        if(!guilds.isEmpty()) return;

                        discordGuildMapper.insertDiscordGuildMapper(DiscordGuildDTO.builder().guild_id(guildId).guild_name(guildName).build());

                        log.setDiscord_log_is_success(true);
                        log.setDiscord_log_content("");
                    } catch (Exception e) {
                        logger.error("Error in GuildCreateEvent: {}", e.getMessage());

                        log.setDiscord_log_is_success(false);
                        log.setDiscord_log_content(e.getMessage());
                    }

                    discordLogMapper.insertDiscordLog(log);
                });
    }
}
