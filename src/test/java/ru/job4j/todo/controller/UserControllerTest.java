package ru.job4j.todo.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    private UserService userService = mock(UserService.class);

    private Model model = mock(Model.class);

    private HttpServletRequest request = mock(HttpServletRequest.class);

    private HttpSession httpSession = mock(HttpSession.class);

    @DisplayName("Страница входа /loginPage")
    @Test
    void getLoginPage() {
        UserController userController = new UserController(userService);

        String page = userController.getLoginPage();

        assertEquals(page, "users/login");
    }

    @DisplayName("Попытка залогиниться успешно")
    @Test
    void loginUser() {
        UserController userController = new UserController(userService);
        User user = new User(1, "Name", "Login", "Pass");
        Optional<User> optionalUser = Optional.of(user);

        when(userService.findByLoginAndPwd(user.getLogin(), user.getPassword()))
                .thenReturn(optionalUser);
        when(request.getSession()).thenReturn(httpSession);

        String page = userController.loginUser(model, user, request);

        verify(httpSession).setAttribute("user", optionalUser.get());
        assertEquals(page, "redirect:/");

    }

    @DisplayName("Ошибка входа в систему")
    @Test
    void whenFailLoginUser() {
        UserController userController = new UserController(userService);
        User user = new User(1, "Name", "Login", "Pass");
        Optional<User> optionalUser = Optional.empty();

        when(userService.findByLoginAndPwd(user.getLogin(), user.getPassword()))
                .thenReturn(optionalUser);

        String page = userController.loginUser(model, user, request);

        verify(model).addAttribute("error", "Логин или пароль введены не правильно");
        assertEquals(page, "users/login");

    }

    @DisplayName("Страница регистрации пользователя")
    @Test
    void getRegisterPage() {
        UserController userController = new UserController(userService);

        String page = userController.getRegisterPage();

        assertEquals(page, "users/register");
    }

    @DisplayName("Регистрация пользователя - успешно")
    @Test
    void createUser() {
        User user = new User(1, "Name", "Login", "Pass");
        when(userService.create(user)).thenReturn(user);

        UserController userController = new UserController(userService);

        String page = userController.createUser(model, user);

        assertEquals(page, "redirect:/");
    }

    @DisplayName("Попытка регистрации с одинаковыми логинами")
    @Test
    void whenFailCreateUser() {
        User user = new User(1, "Name", "Login", "Pass");
        when(userService.create(user)).thenReturn(null);

        UserController userController = new UserController(userService);

        String page = userController.createUser(model, user);

        assertEquals(page, "errors/404");
        verify(model).addAttribute("message", "Такой пользователь уже существует");
    }

    @DisplayName("Выход из учётной записи")
    @Test
    void logout() {
        UserController userController = new UserController(userService);
        String page = userController.logout(httpSession);
        assertEquals(page, "redirect:/loginPage");
    }
}