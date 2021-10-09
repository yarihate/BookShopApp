package com.example.BookShopApp.security;

import com.example.BookShopApp.data.repositories.JWTTokenBlackList;
import com.example.BookShopApp.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class CustomLogoutHandler implements LogoutHandler {

    private final JWTTokenBlackList jwtTokenBlackList;
    private final JWTUtil jwtUtil;

    @Autowired
    public CustomLogoutHandler(JWTTokenBlackList jwtTokenBlackList, JWTUtil jwtUtil) {
        this.jwtTokenBlackList = jwtTokenBlackList;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String tokenId = jwtUtil.extractId(cookie.getValue());
                    jwtTokenBlackList.put(tokenId, cookie.getValue());
                }
            }
        }
    }
}
