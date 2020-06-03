package com.example.demo.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.SampleService;

@RestController
public class SampleController {
	@Autowired
	private SampleService sampleService;
	
    @RequestMapping("/user")
    public Map<String, Object> selectUserInfo(@RequestHeader(value = "Authorization") String accessToken) throws Exception {
    	String[] split = accessToken.split(" ");
        //String type = split[0];
        String credential = split[1];
    	return sampleService.selectUserInfo(credential);
    }
    
    @RequestMapping("/user/userInfo/{mobile}")
    public Map<String, Object> updateUserInfo(@RequestHeader(value = "Authorization") String accessToken,
    										  @PathVariable String mobile) throws Exception {
    	String[] split = accessToken.split(" ");
        //String type = split[0];
        String credential = split[1];
        return sampleService.updateUserInfo(credential, mobile);
    }
}
