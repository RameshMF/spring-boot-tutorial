package net.guides.springboot2.springboot2webappjsp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import net.guides.springboot2.springboot2webappjsp.repositories.UserRepository;

@Controller
public class UserController {
	@Autowired
	UserRepository userRepo;

	@RequestMapping("/users")
	public String home(Model model) {
		model.addAttribute("users", userRepo.findAll());
		return "users";
	}
}
