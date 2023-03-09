package ru.job4j.todo.store;

import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User create(User user);

    Optional<User> findById(int id);

    Optional<User> findByLoginAndPwd(String login, String password);

    List<User> findAll();

    void deleteById(int id);
}
