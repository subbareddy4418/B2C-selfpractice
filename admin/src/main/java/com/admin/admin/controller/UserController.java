package com.admin.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.admin.admin.dto.CommonApiResponse;
import com.admin.admin.dto.UserRequestDto;
import com.admin.admin.resource.UserResource;



@RestController
@RequestMapping("api/user")
public class UserController {
	
	
	private final Logger LOG = LoggerFactory.getLogger(UserController.class); 
	
	@Autowired
	private UserResource userResource;
	
	
	
	@PostMapping("/admin/register")
	//@Operation(summary = "Api to register Admin")
	public ResponseEntity<CommonApiResponse> registerAdmin(@RequestBody UserRequestDto request) {
	    LOG.info("Received request to register Admin");

	    try {
	        ResponseEntity<CommonApiResponse> responseEntity = userResource.registerAdmin(request);
	        LOG.info("Admin registration request processed successfully");
	        return responseEntity;
	    } catch (Exception e) {
	        LOG.error("Error occurred during admin registration: {}", e.getMessage(), e);
	        CommonApiResponse errorResponse = new CommonApiResponse();
	        errorResponse.setResponseMessage("Admin registration failed");
	        errorResponse.setSuccess(false);
	        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
    @GetMapping("/test")
    public String gettest() {
    	return "test msg";
    }
}
