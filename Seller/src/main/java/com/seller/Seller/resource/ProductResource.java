package com.seller.Seller.resource;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


import com.seller.Seller.dto.CommonApiResponse;
import com.seller.Seller.dto.ProductAddRequest;
import com.seller.Seller.dto.ProductDetailUpdateRequest;
import com.seller.Seller.dto.ProductResponseDto;
import com.seller.Seller.exception.ProductSaveFailedException;
import com.seller.Seller.model.Category;
import com.seller.Seller.model.Product;
import com.seller.Seller.model.User;
import com.seller.Seller.service.CategoryService;
import com.seller.Seller.service.ProductService;
import com.seller.Seller.service.StorageService;
import com.seller.Seller.service.UserService;
import com.seller.Seller.utility.Constants.ProductStatus;

import jakarta.transaction.Transactional;


@Component
@Transactional
public class ProductResource {
	
	private final Logger LOG = LoggerFactory.getLogger(ProductResource.class);

	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private StorageService storageService;
	

	public ResponseEntity<CommonApiResponse> addProduct(ProductAddRequest productDto) {

		LOG.info("request received for Product add");

		CommonApiResponse response = new CommonApiResponse();

		if (productDto == null || productDto.getCategoryId() == 0 || productDto.getSellerId() == 0) {
			response.setResponseMessage("missing input");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Product product = ProductAddRequest.toEntity(productDto);
		product.setStatus(ProductStatus.ACTIVE.value());

		User seller = this.userService.getUserById(productDto.getSellerId());

		if (seller == null) {
			response.setResponseMessage("Seller not found");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Category category = this.categoryService.getCategoryById(productDto.getCategoryId());

		if (category == null) {
			response.setResponseMessage("Category not found");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		product.setSeller(seller);
		product.setCategory(category);

		// store product image in Image Folder and give name to store in database
		String productImageName1 = storageService.store(productDto.getImage1());
		String productImageName2 = storageService.store(productDto.getImage2());
		String productImageName3 = storageService.store(productDto.getImage3());

		product.setImage1(productImageName1);
		product.setImage2(productImageName2);
		product.setImage3(productImageName3);

		Product savedProduct = this.productService.addProduct(product);

		if (savedProduct == null) {
			throw new ProductSaveFailedException("Failed to save the Product");
		}

		response.setResponseMessage("Product added successful");
		response.setSuccess(true);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);

	}
	
	public ResponseEntity<ProductResponseDto> fetchAllProducts() {

		LOG.info("request received for fetching all the products");

		ProductResponseDto response = new ProductResponseDto();

		List<Product> products = this.productService
				.getAllProductByStatusIn(Arrays.asList(ProductStatus.ACTIVE.value()));

		if (CollectionUtils.isEmpty(products)) {
			response.setResponseMessage("No products found");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.OK);
		}

		response.setProducts(products);
		response.setResponseMessage("Product fetched success");
		response.setSuccess(true);

		return new ResponseEntity<ProductResponseDto>(response, HttpStatus.OK);
	}
	

	public ResponseEntity<ProductResponseDto> fetchProductById(int productId) {

		LOG.info("request received for searching the seller products");

		ProductResponseDto response = new ProductResponseDto();

		if (productId == 0) {
			response.setResponseMessage("missing product id");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Product product = this.productService.getProductById(productId);

		if (product == null) {
			response.setResponseMessage("Product not found");
			response.setSuccess(false);

			return new ResponseEntity<ProductResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		response.setProducts(Arrays.asList(product));
		response.setResponseMessage("Product fetched success");
		response.setSuccess(true);

		return new ResponseEntity<ProductResponseDto>(response, HttpStatus.OK);
	}
	
	public ResponseEntity<CommonApiResponse> updateProductDetail(ProductDetailUpdateRequest request) {

		LOG.info("request received for update product");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null) {
			response.setResponseMessage("missing input");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Product product = this.productService.getProductById(request.getId());

		if (product == null) {
			response.setResponseMessage("product not found");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// it will update the category if changed
		if (product.getCategory().getId() != request.getCategoryId()) {
			Category category = this.categoryService.getCategoryById(request.getCategoryId());
			product.setCategory(category);
		}

		product.setDescription(request.getDescription());
		product.setName(request.getName());
		product.setPrice(request.getPrice());
		product.setQuantity(request.getQuantity());

		Product updatedProduct = this.productService.updateProduct(product);

		if (updatedProduct == null) {
			throw new ProductSaveFailedException("Failed to update the Product details");
		}

		response.setResponseMessage("Product added successful");
		response.setSuccess(true);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}


}
