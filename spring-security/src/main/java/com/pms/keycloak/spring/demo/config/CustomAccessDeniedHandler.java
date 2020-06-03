package com.pms.keycloak.spring.demo.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	 
    @Override
    public void handle(HttpServletRequest request,
    				   HttpServletResponse response,
    				   AccessDeniedException exc) throws IOException, ServletException {
    	response.setStatus(HttpStatus.SC_FORBIDDEN);
    	
    	request.setAttribute("errMsg", exc.getMessage());
        request.getRequestDispatcher("/denied").forward(request, response);
    }
}
