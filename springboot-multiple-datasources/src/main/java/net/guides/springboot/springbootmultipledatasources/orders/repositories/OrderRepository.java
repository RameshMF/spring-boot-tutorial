/**
 * 
 */
package net.guides.springboot.springbootmultipledatasources.orders.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import net.guides.springboot.springbootmultipledatasources.orders.entities.Order;

/**
 * @author Ramesh Fadatare
 *
 */
public interface OrderRepository extends JpaRepository<Order, Integer>{

}
