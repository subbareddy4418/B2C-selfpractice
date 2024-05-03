package com.customer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customer.model.Orders;
import com.customer.repository.OrdersRepository;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrdersRepository orderrepo;

	@Override
	public List<Orders> addOrder(List<Orders> orders) {
		return this.orderrepo.saveAll(orders);
	}

}
