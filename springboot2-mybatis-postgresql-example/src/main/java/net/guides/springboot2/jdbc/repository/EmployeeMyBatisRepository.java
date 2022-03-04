package net.guides.springboot2.jdbc.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import net.guides.springboot2.jdbc.model.Employee;

@Mapper
public interface EmployeeMyBatisRepository {
	@Select("select * from employees")
	public List<Employee> findAll();

	@Select("SELECT * FROM employees WHERE id = #{id}")
	public Employee findById(long id);

	@Delete("DELETE FROM employees WHERE id = #{id}")
	public int deleteById(long id);

	@Insert("INSERT INTO employees(id, first_name, last_name,email_address) "
			+ " VALUES (#{id}, #{firstName}, #{lastName}, #{emailId})")
	public int insert(Employee employee);

	@Update("Update employees set first_name=#{firstName}, "
			+ " last_name=#{lastName}, email_address=#{emailId} where id=#{id}")
	public int update(Employee employee);
}