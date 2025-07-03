package com.github.mathbook3948.zzibot.dto.zzibot;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Alias( "AdminDTO")
public class AdminDTO {
    private String admin_id;
    private String admin_name;
    private String admin_password;
    private String admin_refreshtoken;
    private boolean admin_is_enable;
    private LocalDateTime admin_created_at;
    private LocalDateTime admin_updated_at;
    private boolean admin_is_superadmin;

}