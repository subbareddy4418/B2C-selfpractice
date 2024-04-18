package com.customer.service;

import java.util.List;

import com.customer.model.Orders;

public interface OrderService {
	
	List<Orders> addOrder(List<Orders> orders);

}
