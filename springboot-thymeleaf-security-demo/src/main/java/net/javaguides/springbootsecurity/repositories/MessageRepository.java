package net.javaguides.springbootsecurity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguides.springbootsecurity.entities.Message;

/**
 * @author Ramesh Fadatare
 *
 */
public interface MessageRepository extends JpaRepository<Message, Integer>{

}
