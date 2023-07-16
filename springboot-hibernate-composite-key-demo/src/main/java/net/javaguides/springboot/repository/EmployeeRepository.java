package net.javaguides.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.entity.Employee;
import net.javaguides.springboot.entity.EmployeeIdentity;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, EmployeeIdentity> {

	/*
	 * Spring Data JPA will automatically parse this method name and create a query
	 * from it
	 */
	List<Employee> findByIdCompanyId(String companyId);
}
