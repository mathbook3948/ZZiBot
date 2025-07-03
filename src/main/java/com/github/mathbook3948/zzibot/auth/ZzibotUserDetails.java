package com.github.mathbook3948.zzibot.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class ZzibotUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;

    @Getter
    private final boolean isEnable;

    @Getter
    private final String refreshToken;

    public ZzibotUserDetails(String username, String password, List<GrantedAuthority> authorities, String refreshToken, boolean isEnable) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.refreshToken = refreshToken;
        this.isEnable = isEnable;
    }

    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
}
