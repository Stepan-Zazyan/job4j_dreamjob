package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.repository.UserRepository;
import ru.job4j.dreamjob.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private UserController userController;

    private UserService userService;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenRequestUserCreationPageThenGetRegisterPage() {
        var view = userController.getRegistrationPage();
        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenRegisterUserThenRegisterAndRedirectToVacanciesPage() {
        User user = new User(1, "mail@mail.ru", "name", "password");
        when(userService.save(user)).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.register(user, model);

        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    public void whenNotRegisterUserThenRedirectToErrorPage() {
        User user = new User(1, "mail@mail.ru", "name", "password");
        String expectedMessage = "Пользователь с такой почтой уже существует";
        when(userService.save(user)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = userController.register(user, model);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenLoginUserCreationPageThenGetLoginPage() {
        var view = userController.getLoginPage();
        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenLogoutUserThenRedirectLoginPage() {
        var session = mock(HttpSession.class);
        var view = userController.logout(session);
        assertThat(view).isEqualTo("redirect:/users/login");
    }

    @Test
    public void whenLoginUserThenRegisterAndRedirectToVacanciesPage() {
        User user = new User(1, "mail@mail.ru", "name", "password");
        var session = mock(HttpSession.class);
        var httpServletRequest = mock(HttpServletRequest.class);
        when(userService.findByEmailAndPassword("mail@mail.ru", "password")).thenReturn(Optional.of(user));
        when(userService.findByEmailAndPassword("mail@mail.ru", "password")).thenReturn(Optional.of(user));
        session.setAttribute("user", user);
        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, httpServletRequest);

        assertThat(view).isEqualTo("redirect:/vacancies");
    }


}