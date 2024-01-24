package net.guides.springboot2.springboot2swagger3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.springboot2swagger3.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
