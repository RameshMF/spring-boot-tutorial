
package net.guides.springboot.springbootmultipledatasources.security.entities;

import jakarta.persistence.*;

/**
 * @author Ramesh Fadatare
 *
 */
@Entity
@Table(name="ADDRESSES")
public class Address
{
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@Column(nullable=false)
	private String city;
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city = city;
	}
	public User getUser()
	{
		return user;
	}
	public void setUser(User user)
	{
		this.user = user;
	}
	
}
