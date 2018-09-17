	package net.guides.springboot.springbootcrudrestapivalidation.model;
	
	import javax.persistence.Column;
	import javax.persistence.Entity;
	import javax.persistence.GeneratedValue;
	import javax.persistence.GenerationType;
	import javax.persistence.Id;
	import javax.persistence.Table;
	import javax.validation.constraints.Email;
	import javax.validation.constraints.NotBlank;
	import javax.validation.constraints.NotNull;
	import javax.validation.constraints.Size;
	
	@Entity
	@Table(name = "employees")
	public class Employee {
	
		private long id;
	
		@NotNull
		@Size(min = 2, message = "First Name should have atleast 2 characters")
		private String firstName;
		
		@NotNull
		@Size(min = 2, message = "Last Name should have atleast 2 characters")
		private String lastName;
		
		@Email
		@NotBlank
		private String emailId;
		
		@NotNull
		@Size(min = 2, message = "Passport should have atleast 2 characters")
		private String passportNumber;
	
		public Employee() {
	
		}
	
		public Employee(String firstName, String lastName, String emailId) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.emailId = emailId;
		}
	
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		public long getId() {
			return id;
		}
	
		public void setId(long id) {
			this.id = id;
		}
	
		@Column(name = "first_name", nullable = false)
		public String getFirstName() {
			return firstName;
		}
	
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
	
		@Column(name = "last_name", nullable = false)
		public String getLastName() {
			return lastName;
		}
	
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
	
		@Column(name = "email_address", nullable = false)
		public String getEmailId() {
			return emailId;
		}
	
		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}
	
		@Column(name = "passport_number", nullable = false)
		public String getPassportNumber() {
			return passportNumber;
		}
	
		public void setPassportNumber(String passportNumber) {
			this.passportNumber = passportNumber;
		}
	
	}
