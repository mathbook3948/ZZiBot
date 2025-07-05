package com.github.mathbook3948.zzibot.mapper;

import com.github.mathbook3948.zzibot.dto.zzibot.guild.DiscordGuildDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiscordGuildMapper {

    void insertDiscordGuildMapper(DiscordGuildDTO params) throws Exception;

    List<DiscordGuildDTO> selectDiscordGuildMapperNotDeleted(DiscordGuildDTO params) throws Exception;

    List<DiscordGuildDTO> selectDiscordGuildMapper(DiscordGuildDTO params) throws Exception;

    void updateDiscordGuildMapper(DiscordGuildDTO params) throws Exception;

    int selectDiscordGuildNotDeletedCount() throws Exception;
}
