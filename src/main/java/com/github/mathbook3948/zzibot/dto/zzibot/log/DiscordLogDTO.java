package com.github.mathbook3948.zzibot.dto.zzibot.log;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Getter
@Setter
@Alias("DiscordLogDTO")
public class DiscordLogDTO {
    private int discord_log_idx;
    private String discord_log_command;
    private boolean discord_log_is_success;
    private LocalDateTime discord_log_time;
    private String discord_log_content;
    private String discord_log_guild_id;
    private String discord_log_channel_id;
    private String discord_log_user_id;
    private String discord_log_user_tag;
}