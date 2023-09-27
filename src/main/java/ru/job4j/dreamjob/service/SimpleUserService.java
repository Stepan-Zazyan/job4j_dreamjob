package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.model.Vacancy;
import ru.job4j.dreamjob.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;
@ThreadSafe
@Service
public class SimpleUserService implements UserService {

    private final UserRepository userRepository;

    private final FileService fileService;

    public SimpleUserService(UserRepository userRepository, FileService fileService) {
        this.userRepository = userRepository;
        this.fileService = fileService;
    }

    @Override
    public Optional<User> save(User user, FileDto image) {
        saveNewFile(user, image);
        return userRepository.save(user);
    }

    private void saveNewFile(User user, FileDto image) {
        var file = fileService.save(image);
        user.setFileId(file.getId());
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean deleteById(int id) {
        var fileOptional = findById(id);
        boolean res = false;
        if (fileOptional.isPresent()) {
            res = userRepository.deleteById(id);
        }
        return res;
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }
}
