package com.github.mathbook3948.zzibot.discord.registry;

import com.github.mathbook3948.zzibot.dto.zzibot.CommandDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeleteLiveSubscriptionRegister extends AbstractCommandRegistrar{

    private final String NAME = "알림해제";

    private final String DESCRIPTION = "치지직 라이브 알림을 해제할 채널을 선택합니다.";
    
    private final Integer TYPE = 1;

    private final List<CommandDTO.CommandOptionDTO> OPTIONS = List.of(
            CommandDTO.CommandOptionDTO.builder().name("채널명").type(3).description("치지직 채널명").autocomplete(true).required(true).build()
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
