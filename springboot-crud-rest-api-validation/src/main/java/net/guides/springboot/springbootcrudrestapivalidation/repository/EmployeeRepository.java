	package net.guides.springboot.springbootcrudrestapivalidation.repository;
	
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.stereotype.Repository;
	
	import net.guides.springboot.springbootcrudrestapivalidation.model.Employee;
	
	@Repository
	public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	
	}
