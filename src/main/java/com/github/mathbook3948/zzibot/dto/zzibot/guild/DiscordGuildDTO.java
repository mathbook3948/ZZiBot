package com.github.mathbook3948.zzibot.dto.zzibot.guild;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Alias("DiscordGuildDTO")
public class DiscordGuildDTO {
    private Integer idx;
    private String guild_id;
    private String guild_name;
    private Boolean deleted;
    private LocalDateTime discord_guild_created_time;
    private LocalDateTime discord_guild_updated_time;
}
