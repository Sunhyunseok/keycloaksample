package com.example.demo.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SampleService {
    @Value("${keycloak.auth-server-url}")
	private String KEYCLOAK_SERVER_URI;
    
    @Value("${keycloak.realm}")
	private String KEYCLOAK_REALM;
    
    @Autowired
    private RestTemplate restTemplate;
    
    public Map<String, Object> selectUserInfo(String accessToken) throws Exception {
    	String url = this.KEYCLOAK_SERVER_URI + "/realms/" + this.KEYCLOAK_REALM + "/protocol/openid-connect/userinfo";
    	
    	UriComponents builder = UriComponentsBuilder.fromHttpUrl(url).build(false);
    	    	
    	MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("access_token", accessToken);
    	       
	    ResponseEntity<Map<String, Object>> responseEntity = null;
	    try {
	    	responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, new HttpEntity<MultiValueMap<String, Object>>(params), new ParameterizedTypeReference<Map<String, Object>>() {});
	    } catch (HttpClientErrorException hc) {
	    	throw new Exception(hc.getMessage());
	    } catch (Exception e) {
	    	throw e;
	    }
	    return responseEntity.getBody();
    }
    
    public Map<String, Object> updateUserInfo(String accessToken, String mobile) throws Exception {
    	String url = this.KEYCLOAK_SERVER_URI + "/realms/" + this.KEYCLOAK_REALM + "/account";
    	
    	UriComponents builder = UriComponentsBuilder.fromHttpUrl(url).build(false);
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	
    	MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        Map<String, List<String>> attributes = new HashMap<String, List<String>>();
        attributes.put("mobile", Arrays.asList(mobile));
        params.add("attributes", attributes);        
        
        ResponseEntity<Map<String, Object>> responseEntity = null;
        try {
        	responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, new HttpEntity<MultiValueMap<String, Object>>(params, headers), new ParameterizedTypeReference<Map<String, Object>>() {});
        } catch (HttpClientErrorException hc) {
        	throw new Exception(hc.getMessage());
        } catch (Exception e) {
        	throw e;
        }
        return responseEntity.getBody();
    }
}
