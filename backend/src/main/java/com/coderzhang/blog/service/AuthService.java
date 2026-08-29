package com.coderzhang.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coderzhang.blog.common.BizException;
import com.coderzhang.blog.dto.LoginRequest;
import com.coderzhang.blog.dto.LoginResponse;
import com.coderzhang.blog.entity.User;
import com.coderzhang.blog.mapper.UserMapper;
import com.coderzhang.blog.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserMapper userMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest req) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, req.getUsername()));
        if (user == null || !encoder.matches(req.getPassword(), user.getPassword())) {
            throw new BizException("用户名或密码错误");
        }
        return new LoginResponse(jwtUtil.generate(user.getId(), user.getUsername()),
                user.getUsername(), user.getNickname());
    }

    public String encode(String raw) { return encoder.encode(raw); }
}
