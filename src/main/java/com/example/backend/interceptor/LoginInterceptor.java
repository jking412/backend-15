package com.example.backend.interceptor;

import com.example.backend.domain.Result;
import com.example.backend.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;


@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        System.out.println("preHandle...");
        String url = req.getRequestURI();
        log.info("{}",url);

        String jwt = req.getHeader("token");
        if(!StringUtils.hasLength(jwt))
        {
            log.info("token为空");

            resp.getWriter().write("notLogin");
            return false;
        }
        try {
            JwtUtils.parseJWT(jwt);
        } catch (Exception e) {

            e.printStackTrace();
            log.info("解析错误");
            resp.getWriter().write("notLogin");
            return false;
        }
        log.info("合法，放行");
        return true;//放行
    }


}

