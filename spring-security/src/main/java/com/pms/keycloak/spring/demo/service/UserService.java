package com.pms.keycloak.spring.demo.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class UserService {
	@Autowired
    private RestTemplate restTemplate;
    
    public Map<String, Object> selectUserInfo() throws Exception {
        String url = "http://localhost:8082/user";
        
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(url).build(false);
        
        ResponseEntity<Map<String, Object>> responseEntity = null;
        try {
        	responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});
        } catch (HttpClientErrorException hc) {
        	throw new Exception(hc.getMessage());
        } catch (Exception e) {
        	throw e;
        }
        return responseEntity.getBody();
    }
}