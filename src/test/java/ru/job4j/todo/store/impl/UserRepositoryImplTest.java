package ru.job4j.todo.store.impl;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryImplTest {

    private static StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private static SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private static UserRepository userRepository = new UserRepositoryImpl(sf);

    @BeforeEach
    void beforeAll() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            userRepository.deleteById(user.getId());
        }

    }

    @Test
    void whenCreateNewUserAndFindById() {
        var user = userRepository.create(new User(1, "name", "login", "pass"));
        var userExpected = userRepository.findById(user.getId()).get();
        assertThat(user).isEqualTo(userExpected);
    }

    @Test
    void findByLoginAndPwd() {
        var user = userRepository
                .create(new User(
                        1,
                        "name",
                        "login",
                        "pass"));
        Optional<User> optionalUser = userRepository.findByLoginAndPwd(
                user.getLogin(), user.getPassword()
        );
        assertThat(user).isEqualTo(optionalUser.get());
    }
}