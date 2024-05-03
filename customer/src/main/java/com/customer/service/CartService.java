package com.customer.service;

import java.util.List;

import com.customer.model.Cart;
import com.customer.model.User;

public interface CartService {

	List<Cart> findByUser(User user);

	void deleteCarts(List<Cart> cart);

	Cart updateCart(Cart cart);

	void deleteCart(Cart cart);

	Cart getCartById(int cartId);

}
