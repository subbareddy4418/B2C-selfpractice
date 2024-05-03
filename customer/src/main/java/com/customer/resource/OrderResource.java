package com.customer.resource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.customer.dto.CommonApiResponse;
import com.customer.exception.OrderSaveFailedException;
import com.customer.model.Cart;
import com.customer.model.Orders;
import com.customer.model.Product;
import com.customer.model.User;
import com.customer.service.CartService;
import com.customer.service.OrderService;
import com.customer.service.ProductService;
import com.customer.service.UserService;
import com.customer.utility.Constants.DeliveryStatus;
import com.customer.utility.Helper;

import jakarta.transaction.Transactional;

@Component
@Transactional
public class OrderResource {

	private final Logger LOG = LoggerFactory.getLogger(OrderResource.class);

	@Autowired
	private CartService cartService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderService orderService;

	public ResponseEntity<CommonApiResponse> orderProductsFromCart(int userId) {

		LOG.info("Request received for ordering the products from the Cart");

		CommonApiResponse response = new CommonApiResponse();

		if (userId == 0) {
			response.setResponseMessage("user id missing");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User user = this.userService.getUserById(userId);

		if (user == null) {
			response.setResponseMessage("User not found");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		List<Cart> carts = this.cartService.findByUser(user);

		if (CollectionUtils.isEmpty(carts)) {
			response.setResponseMessage("No products found in Cart");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		List<Product> products = new ArrayList<>(); // products to update in db after reducing the quantity

		String orderTime = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		// Create a map to group cart items by seller
		Map<User, List<Cart>> cartItemsBySeller = new HashMap<>();

		for (Cart cart : carts) {
		    User seller = cart.getProduct().getSeller();

		    // Check if the seller is already in the map
		    if (!cartItemsBySeller.containsKey(seller)) {
		        cartItemsBySeller.put(seller, new ArrayList<>());
		    }

		    // Add the cart item to the corresponding seller's list
		    cartItemsBySeller.get(seller).add(cart);
		}

		// Generate orders for each seller
		List<Orders> orders = new ArrayList<>();

		for (Map.Entry<User, List<Cart>> entry : cartItemsBySeller.entrySet()) {
		  
		    List<Cart> sellerCartItems = entry.getValue();

		    // Generate a unique order ID for each seller
		    String orderId = Helper.generateOrderId();

		    for (Cart cart : sellerCartItems) {
		        if (cart.getProduct().getQuantity() < cart.getQuantity()) {
		            throw new OrderSaveFailedException("Failed to Order, few products are out of stock");
		        }

		        Orders order = new Orders();
		        order.setOrderId(orderId);
		        order.setUser(user);
		        order.setOrderTime(orderTime);
		        order.setQuantity(cart.getQuantity());
		        order.setProduct(cart.getProduct());
		        order.setStatus(DeliveryStatus.PENDING.value());
		        order.setDeliveryStatus(DeliveryStatus.PENDING.value());

		        orders.add(order);
		        
		        Product productToUpdate = cart.getProduct();

				int quantity = productToUpdate.getQuantity() - cart.getQuantity();
				productToUpdate.setQuantity(quantity);

				products.add(productToUpdate);
		    }
		}

		List<Orders> addedOrders = this.orderService.addOrder(orders);

		if (addedOrders == null) {
			throw new OrderSaveFailedException("Failed to Order Products");
		}

		try {
			this.cartService.deleteCarts(carts);
		} catch (Exception e) {
			throw new OrderSaveFailedException("Failed to Order Products");
		}

		List<Product> updateProducts = this.productService.updateAllProduct(products);

		if (updateProducts == null) {
			throw new OrderSaveFailedException("Failed to Order Products");
		}

		response.setResponseMessage("Order Placed Successful, Check Status in Dashboard!!!");
		response.setSuccess(true);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}


}
