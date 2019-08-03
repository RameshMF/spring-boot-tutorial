package net.guides.springboot.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot.jpa.model.Instructor;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long>{

}
