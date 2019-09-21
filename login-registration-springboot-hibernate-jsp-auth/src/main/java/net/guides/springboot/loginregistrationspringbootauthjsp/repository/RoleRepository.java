package net.guides.springboot.loginregistrationspringbootauthjsp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.guides.springboot.loginregistrationspringbootauthjsp.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
}
