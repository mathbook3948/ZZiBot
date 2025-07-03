package com.github.mathbook3948.zzibot.service.auth;

import com.github.mathbook3948.zzibot.auth.ZzibotUserDetails;
import com.github.mathbook3948.zzibot.dto.zzibot.AdminDTO;
import com.github.mathbook3948.zzibot.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ZzibotUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminDTO detail = adminMapper.selectAdminDetail(AdminDTO.builder().admin_id(username).build());

        if(detail == null) throw new UsernameNotFoundException("해당하는 사용자를 찾을 수 없습니다.");

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        if(detail.isAdmin_is_superadmin()) authorities.add(new SimpleGrantedAuthority("SUPERADMIN"));

        return new ZzibotUserDetails(detail.getAdmin_id(), detail.getAdmin_password(), authorities, detail.getAdmin_refreshtoken(), detail.isAdmin_is_enable());

    }
}
