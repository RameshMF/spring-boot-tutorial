package net.guides.springboot.loginregistrationspringbootauthjsp.service;

import net.guides.springboot.loginregistrationspringbootauthjsp.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
