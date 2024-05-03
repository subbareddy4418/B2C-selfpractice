package com.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.customer.dto.CartRequestDto;
import com.customer.dto.CartResponseDto;
import com.customer.dto.CommonApiResponse;
import com.customer.resource.CartResource;

@RestController
@RequestMapping("api/cart")
public class CartController {

	@Autowired
	private CartResource cartResource;

	@PostMapping("/add")
	// @Operation(summary = "Api to add cart")
	public ResponseEntity<CommonApiResponse> addCategory(@RequestBody CartRequestDto request) {
		return cartResource.addToCart(request);
	}

	@PutMapping("/update") // @Operation(summary = "Api to update cart") public
	ResponseEntity<CartResponseDto> updateCart(@RequestBody CartRequestDto request) {
		return cartResource.updateCart(request);
	}

	@GetMapping("/fetch")
	// @Operation(summary = "Api to fetch the user cart")
	public ResponseEntity<CartResponseDto> fetchUserCart(@RequestParam("userId") int userId) {
		return cartResource.fetchUserCartDetails(userId);
	}

	@DeleteMapping("/delete") // @Operation(summary = "Api to delete cart")
	public ResponseEntity<CartResponseDto> deleteCart(@RequestBody CartRequestDto request) {
		return cartResource.deleteCart(request);
	}

}
