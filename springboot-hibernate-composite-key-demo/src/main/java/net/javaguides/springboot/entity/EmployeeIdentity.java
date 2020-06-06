package net.javaguides.springboot.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class EmployeeIdentity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String employeeId;
	private String companyId;
	
	public EmployeeIdentity() {
		
	}
	
	public EmployeeIdentity(String employeeId, String companyId) {
		super();
		this.employeeId = employeeId;
		this.companyId = companyId;
	}
	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((employeeId == null) ? 0 : employeeId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeIdentity other = (EmployeeIdentity) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (employeeId == null) {
			if (other.employeeId != null)
				return false;
		} else if (!employeeId.equals(other.employeeId))
			return false;
		return true;
	}
}
