package ru.job4j.todo.service.ipml;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;
import ru.job4j.todo.store.UserRepository;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        return userRepository.create(user);
    }

    @Override
    public Optional<User> findByLoginAndPwd(String login, String password) {
        return userRepository.findByLoginAndPwd(login, password);
    }
}
