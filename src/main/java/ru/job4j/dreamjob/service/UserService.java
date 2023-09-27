package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    Optional<User> save(User user, FileDto image);

    Optional<User> findByEmailAndPassword(String email, String password);

    Collection<User> findAll();

    boolean deleteById(int id);

    Optional<User> findById(int id);
}
