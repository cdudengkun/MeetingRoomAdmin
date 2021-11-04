package com.cjack.meetingroomadmin.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.config.ErrorCodeDefine;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by root on 4/5/19.
 */
public class CheckLoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object loginUserId = session.getAttribute( CommonConfig.SESSION_NAME);
        if( loginUserId == null){
            returnRes( ErrorCodeDefine.USER_NOT_LOGIN, ErrorCodeDefine.USER_NOT_LOGIN_MSG, response);
            return false;
        }
        return super.preHandle( request, response, handler);
    }

    private void returnRes( Integer code, String msg, HttpServletResponse response) throws IOException {
        JSONObject res = new JSONObject();
        res.put( "code", code);
        res.put( "msg", msg);
        response.setCharacterEncoding( "utf-8");
        PrintWriter out = response.getWriter();
        out.append( res.toJSONString());
    }
}
