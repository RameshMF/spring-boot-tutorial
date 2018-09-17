
package net.guides.springboot.springbootmultipledatasources.orders.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Ramesh Fadatare
 *
 */
@Entity
@Table(name="ORDER_ITEMS")
public class OrderItem
{
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String productCode;
	private int quantity;
	@ManyToOne
	@JoinColumn(name="order_id")
	private Order order;
	
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public String getProductCode()
	{
		return productCode;
	}
	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}
	public int getQuantity()
	{
		return quantity;
	}
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	public Order getOrder()
	{
		return order;
	}
	public void setOrder(Order order)
	{
		this.order = order;
	}
	
}
