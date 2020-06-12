package net.javaguides.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.javaguides.springboot.entity.Comment;
import net.javaguides.springboot.entity.Post;
import net.javaguides.springboot.repository.PostRepository;

@SpringBootApplication
public class SpringbootHibernateOneManyMappingApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(SpringbootHibernateOneManyMappingApplication.class, args);
	}

	@Autowired
	private PostRepository postRepository;
	
	
	@Override
	public void run(String... args) throws Exception {
		
		Post post = new Post("one to many mapping using JPA and hibernate", "one to many mapping using JPA and hibernate");
		
		Comment comment1 = new Comment("Very useful");
		Comment comment2 = new Comment("informative");
		Comment comment3 = new Comment("Great post");
		
		post.getComments().add(comment1);
		post.getComments().add(comment2);
		post.getComments().add(comment3);
		
		this.postRepository.save(post);
	}
}
