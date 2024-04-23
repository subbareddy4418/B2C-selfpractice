package com.seller.Seller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seller.Seller.dto.CommonApiResponse;
import com.seller.Seller.dto.ProductAddRequest;
import com.seller.Seller.resource.ProductResource;


@RestController
@RequestMapping("api/product")
public class ProductController {
	
	@Autowired
	private ProductResource productResource;

	@PostMapping("add")
	//@Operation(summary = "Api to add product")
	public ResponseEntity<CommonApiResponse> addProduct(ProductAddRequest productDto) {
		return this.productResource.addProduct(productDto);
	}


}
