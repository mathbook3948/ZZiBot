package com.github.mathbook3948.zzibot.discord.registry;

import com.github.mathbook3948.zzibot.dto.zzibot.CommandDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LiveSubscriptionRegister extends AbstractCommandRegistrar{

    private final String NAME = "알림등록";

    private final String DESCRIPTION = "치지직 라이브 알림을 받을 채널을 등록합니다.";

    private final Integer TYPE = 1;

    private List<CommandDTO.CommandOptionDTO> OPTIONS = List.of(
            CommandDTO.CommandOptionDTO.builder().name("url").type(3).description("치지직 채널 URL").required(true).build()
    );

    @PostConstruct
    public void registerPingCommand() {
        CommandDTO command = new CommandDTO(NAME, DESCRIPTION, TYPE, OPTIONS);

        if(System.getenv("SPRING_PROFILES_ACTIVE").equalsIgnoreCase("loc")) {
            setGuildCommand(command);
        } else {
            super.setGlobalCommand(command);
        }
    }
}
