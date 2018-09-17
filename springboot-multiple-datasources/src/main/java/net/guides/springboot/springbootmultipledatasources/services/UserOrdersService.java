
package net.guides.springboot.springbootmultipledatasources.services;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.guides.springboot.springbootmultipledatasources.orders.entities.Order;
import net.guides.springboot.springbootmultipledatasources.orders.repositories.OrderRepository;
import net.guides.springboot.springbootmultipledatasources.security.entities.User;
import net.guides.springboot.springbootmultipledatasources.security.repositories.UserRepository;

/**
 * @author Ramesh Fadatare
 *
 */
@Service
public class UserOrdersService
{
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional(transactionManager="securityTransactionManager")
	public List<User> getUsers()
	{
		return userRepository.findAll();
	}
	
	@Transactional(transactionManager="ordersTransactionManager")
	public List<Order> getOrders()
	{
		return orderRepository.findAll();
	}
}
