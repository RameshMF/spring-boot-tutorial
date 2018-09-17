/**
 * 
 */
package net.guides.springboot.springbootmultipledatasources.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;


import net.guides.springboot.springbootmultipledatasources.security.entities.User;

/**
 * @author Ramesh Fadatare
 *
 */
public interface UserRepository extends JpaRepository<User, Integer>
{
	
}

