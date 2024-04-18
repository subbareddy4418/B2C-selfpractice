package com.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.customer.model.Orders;
import com.customer.model.User;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {

	List<Orders> findByOrderId(String orderId);

	List<Orders> findByOrderIdAndStatusIn(String orderId, List<String> status);

	List<Orders> findByUser(User user);

	List<Orders> findByUserAndStatusIn(User user, List<String> status);


}
