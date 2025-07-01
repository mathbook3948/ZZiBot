package com.github.mathbook3948.zzibot.discord.registry;

import com.github.mathbook3948.zzibot.dto.zzibot.CommandDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LiveSubscriptionListRegister extends AbstractCommandRegistrar{

    private final String NAME = "알림목록";

    private final String DESCRIPTION = "치지직 라이브 알림이 등록되어 있는 채널을 조회합니다.";

    private final Integer TYPE = 1;

    private List<CommandDTO.CommandOptionDTO> OPTIONS = null;

    @PostConstruct
    public void init() {
        CommandDTO command = new CommandDTO(NAME, DESCRIPTION, TYPE, OPTIONS);

        if(System.getenv("SPRING_PROFILES_ACTIVE").equalsIgnoreCase("loc")) {
            setGuildCommand(command);
        } else {
            super.setGlobalCommand(command);
        }
    }
}
