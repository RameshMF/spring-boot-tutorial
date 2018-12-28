package net.guides.springboot2.springboot2webappjsp.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User
{
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String name;
	
	public User()
	{
	}

	public User(Integer id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
}
