package com.customer.service;

import java.util.List;

import com.customer.model.Category;
import com.customer.model.Product;
import com.customer.model.User;

public interface ProductService {

	Product addProduct(Product product);

	Product updateProduct(Product product);

	List<Product> updateAllProduct(List<Product> products);

	Product getProductById(int productId);

}
