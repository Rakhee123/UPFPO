package com.session;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CheckSessionInterceptor implements HandlerInterceptor {
	
	  @Override
	    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
	        HttpSession session = request.getSession();
	        System.err.println("helooooooo");
	        if (session == null || session.getAttribute("name") == null) {
	            response.sendError(401, "Unauthorized");
	            return false;
	        } else {
	            return true;
	        }
	    }

}
