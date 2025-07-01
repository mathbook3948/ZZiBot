package com.github.mathbook3948.zzibot.dto.zzibot;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommandDTO {

    private String name;
    private String description;
    private Integer type;

    private List<CommandOptionDTO> options;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class CommandOptionDTO {
        private Integer type;
        private String name;
        private String description;
        private boolean required;

        @Builder.Default
        private Boolean autocomplete = false;
    }
}
