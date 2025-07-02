package com.github.mathbook3948.zzibot.discord.registry;

import com.github.mathbook3948.zzibot.dto.zzibot.CommandDTO;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SetChannelRegister extends AbstractCommandRegistrar{

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SetChannelRegister.class);

    private final String NAME = "알림채널설정";

    private final String DESCRIPTION = "이 채널을 봇 알림 채널로 설정합니다.";

    private final Integer TYPE = 1;

    private List<CommandDTO.CommandOptionDTO> OPTIONS = null;

    @PostConstruct
    public void init() {
        CommandDTO command = new CommandDTO(NAME, DESCRIPTION, TYPE, OPTIONS);

        if(System.getenv("SPRING_PROFILES_ACTIVE").equalsIgnoreCase("loc")) {
            super.setGuildCommand(command);
            logger.info("SetChannelRegister is registered to guild.");
        } else {
            super.setGlobalCommand(command);
            logger.info("SetChannelRegister is registered to global.");
        }
    }
}
