package net.javaguides.mssql.repository;

import net.javaguides.mssql.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
