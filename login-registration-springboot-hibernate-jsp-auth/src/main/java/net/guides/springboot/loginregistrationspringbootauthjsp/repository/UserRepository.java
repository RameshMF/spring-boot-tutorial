package net.guides.springboot.loginregistrationspringbootauthjsp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.guides.springboot.loginregistrationspringbootauthjsp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
