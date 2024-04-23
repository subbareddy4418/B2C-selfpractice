package com.seller.Seller.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seller.Seller.dto.CommonApiResponse;
import com.seller.Seller.dto.ProductAddRequest;
import com.seller.Seller.dto.ProductDetailUpdateRequest;
import com.seller.Seller.dto.ProductResponseDto;
import com.seller.Seller.dto.RegisterUserRequestDto;
import com.seller.Seller.dto.UserLoginRequest;
import com.seller.Seller.dto.UserLoginResponse;
import com.seller.Seller.dto.UserResponseDto;
import com.seller.Seller.resource.ProductResource;
import com.seller.Seller.resource.UserResource;

@RestController
@RequestMapping("api/user")
public class UserController {

	private final Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserResource userResource;

	@Autowired
	private ProductResource productResource;

	// for customer and seller register
	@PostMapping("register")
	// @Operation(summary = "Api to register customer or seller user")
	public ResponseEntity<CommonApiResponse> registerUser(@RequestBody RegisterUserRequestDto request) {
		LOG.info("Received request to register user");
		try {
			ResponseEntity<CommonApiResponse> responseEntity = this.userResource.registerUser(request);
			LOG.info("User registration completed successfully");
			return responseEntity;
		} catch (Exception e) {
			LOG.error("Error occurred during user registration: {}", e.getMessage(), e);
			CommonApiResponse errorResponse = new CommonApiResponse();
			errorResponse.setResponseMessage("User registration failed");
			errorResponse.setSuccess(false);
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("login")
	// @Operation(summary = "Api to login any User")
	public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
		LOG.info("Received request to login user");

		try {
			ResponseEntity<UserLoginResponse> responseEntity = userResource.login(userLoginRequest);
			LOG.info("User login request processed successfully");
			return responseEntity;
		} catch (Exception e) {
			LOG.error("Error occurred during user login: {}", e.getMessage(), e);
			UserLoginResponse errorResponse = new UserLoginResponse();
			errorResponse.setResponseMessage("User login failed");
			errorResponse.setSuccess(false);
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/fetch/role")
	// @Operation(summary = "Api to get Users By Role")
	public ResponseEntity<UserResponseDto> fetchAllUsersByRole(@RequestParam("role") String role)
			throws JsonProcessingException {
		LOG.info("Received request to fetch users by role: {}", role);

		try {
			ResponseEntity<UserResponseDto> responseEntity = userResource.getUsersByRole(role);
			LOG.info("User fetch by role request processed successfully");
			return responseEntity;
		} catch (Exception e) {
			LOG.error("Error occurred while fetching users by role: {}", e.getMessage(), e);
			// Handle the error and return an appropriate response
			UserResponseDto errorResponse = new UserResponseDto();
			errorResponse.setResponseMessage("Failed to fetch users by role");
			errorResponse.setSuccess(false);
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("add")
	// @Operation(summary = "Api to add product")
	public ResponseEntity<CommonApiResponse> addProduct(ProductAddRequest productDto) {
		return this.productResource.addProduct(productDto);
	}

	@GetMapping("fetch/all")
	// @Operation(summary = "Api to fetch all active product")
	public ResponseEntity<ProductResponseDto> fetchAllProduct() {
		return this.productResource.fetchAllProducts();
	}

	@GetMapping("fetch")
	// @Operation(summary = "Api to fetch product by Id")
	public ResponseEntity<ProductResponseDto> fetchProductById(@RequestParam(name = "productId") int productId) {
		return this.productResource.fetchProductById(productId);
	}

	@PutMapping("update/detail")
	// @Operation(summary = "Api to update product details")
	public ResponseEntity<CommonApiResponse> updateProductDetails(@RequestBody ProductDetailUpdateRequest request) {
		System.out.println(request);
		return this.productResource.updateProductDetail(request);
	}

}
