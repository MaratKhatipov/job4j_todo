package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;

import java.util.Optional;

public interface UserService {
    User create(User user);

    Optional<User> findByLoginAndPwd(String login, String password);
}
