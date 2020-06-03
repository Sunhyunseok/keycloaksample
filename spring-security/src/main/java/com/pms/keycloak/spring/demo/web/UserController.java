package com.pms.keycloak.spring.demo.web;

import java.util.HashMap;
import java.util.Map;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pms.keycloak.spring.demo.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String userinfo(Model model, Authentication authentication) throws Exception {
        model.addAttribute("users", userService.selectUserInfo());
        
    	Map<String, Object> userInfo = new HashMap<String, Object>();
        if (authentication instanceof KeycloakAuthenticationToken) {
        	KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) authentication;
        	AccessToken token = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();
        	userInfo.put("id", 			token.getId());
        	userInfo.put("name", 		token.getName());
        	userInfo.put("otherClaims", token.getOtherClaims().toString());
        }
        model.addAttribute("userInfo", userInfo);
        return "user";
    }
}
