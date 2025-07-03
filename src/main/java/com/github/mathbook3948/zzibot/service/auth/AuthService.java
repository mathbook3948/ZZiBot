package com.github.mathbook3948.zzibot.service.auth;

import com.github.mathbook3948.zzibot.auth.ZzibotUserDetails;
import com.github.mathbook3948.zzibot.dto.zzibot.AdminDTO;
import com.github.mathbook3948.zzibot.dto.zzibot.LoginDTO;
import com.github.mathbook3948.zzibot.dto.zzibot.ResponseDTO;
import com.github.mathbook3948.zzibot.dto.zzibot.auth.LoginContentDTO;
import com.github.mathbook3948.zzibot.mapper.AdminMapper;
import com.github.mathbook3948.zzibot.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminMapper adminMapper;

    public ResponseDTO<LoginContentDTO> login(LoginDTO params) {
        ResponseDTO<LoginContentDTO> result = new ResponseDTO<>();

        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(params.getUsername(), params.getPassword());

            Authentication authentication = authenticationManager.authenticate(authToken);

            ZzibotUserDetails userDetails = (ZzibotUserDetails) authentication.getPrincipal();

            //토큰 생성
            String accessToken = JwtUtil.generateAccessToken(userDetails);
            String refreshToken = JwtUtil.generateRefreshToken(userDetails.getUsername());

            
            //refreshtoken 저장
            adminMapper.updateRefreshToken(AdminDTO.builder().admin_id(userDetails.getUsername()).admin_refreshtoken(refreshToken).build());

            LoginContentDTO content = new LoginContentDTO();
            content.setAccessToken(accessToken);
            content.setRefreshToken(refreshToken);

            result.setContent(content);
            result.setResult(true);
            result.setMsg("");
        } catch (UsernameNotFoundException | BadCredentialsException ee) {
            result.setResult(false);
            result.setMsg("아이디 또는 비밀번호가 잘못되었습니다.");
            result.setContent(null);
        } catch (Exception e) {
            logger.error("Error in AuthService: {}", e.getMessage());

            result.setResult(false);
            result.setMsg("알 수 없는 오류가 발생했습니다.");
            result.setContent(null);
        }

        return result;
    }
}
