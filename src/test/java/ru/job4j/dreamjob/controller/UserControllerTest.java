package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

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
        when(userService.findByEmailAndPassword("mail@mail.ru", "password")).thenReturn(Optional.of(user));
        MockHttpServletRequest request = new MockHttpServletRequest();

        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, request);

        assertThat(view).isEqualTo("redirect:/vacancies");
    }
}