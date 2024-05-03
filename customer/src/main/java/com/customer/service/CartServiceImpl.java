package com.customer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customer.model.Cart;
import com.customer.model.User;
import com.customer.repository.CartRepository;


@Service
public class CartServiceImpl implements CartService {
	
	
	@Autowired
	private CartRepository cartrepo;
	
	
	
	@Override
	public List<Cart> findByUser(User user) {
		return cartrepo.findByUser(user);
	}

	
	@Override
	public void deleteCarts(List<Cart> carts) {
		this.cartrepo.deleteAll(carts);
	}
	
	
	public Cart updateCart(Cart cart) {
		return cartrepo.save(cart);
	}

	@Override
	public void deleteCart(Cart cart) {
		cartrepo.delete(cart);
	}
	

	@Override
	public Cart getCartById(int cartId) {
		
		Optional<Cart> optionalCart = this.cartrepo.findById(cartId);
		
		if(optionalCart.isPresent()) {
			return optionalCart.get();
		}
		
		else {
			return null;
		}
	}



}
