package com.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.customer.dto.CommonApiResponse;
import com.customer.resource.OrderResource;



@RestController
@RequestMapping("api/order")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {
	
	@Autowired
	private OrderResource orderResource;
	
	@PostMapping("/add")
	//@Operation(summary = "Api to order the products in Cart")
	public ResponseEntity<CommonApiResponse> placeOrder(@RequestParam("userId") int userId) {
		return orderResource.orderProductsFromCart(userId);
	}

	
	/*
	 * @GetMapping("/fetch/user-wise") //@Operation(summary =
	 * "Api to fetch user orders") public ResponseEntity<OrderResponseDto>
	 * fetchUserOrders(@RequestParam("userId") int userId) { return
	 * orderResource.fetchUserOrders(userId); }
	 */
	
	
}
