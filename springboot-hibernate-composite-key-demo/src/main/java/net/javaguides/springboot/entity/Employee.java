package net.javaguides.springboot.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {
	
	@EmbeddedId
	private EmployeeIdentity id;
	
	private String name;
	private String email;
	
	@Column(name = "phone_number")
	private String phoneNumber;

	public Employee() {
		
	}

	public Employee(EmployeeIdentity id, String name, String email, String phoneNumber) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	public EmployeeIdentity getId() {
		return id;
	}

	public void setId(EmployeeIdentity id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
