package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.File;
import ru.job4j.dreamjob.model.User;

import java.util.Properties;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oUserRepositoryTest {
 private static Sql2oUserRepository sql2oUserRepository;

    private static Sql2oFileRepository sql2oFileRepository;

    private static File file;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);

        file = new File("test", "test");
        sql2oFileRepository.save(file);
    }

    @AfterAll
    public static void deleteFile() {
        sql2oFileRepository.deleteById(file.getId());
    }

    @AfterEach
    public void clearVacancies() {
       var users = sql2oUserRepository.findAll();
        for (var user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var user = sql2oUserRepository.save(new User(0, "email5", "Kolya", "123", 1));
        var savedUser = sql2oUserRepository.findByEmailAndPassword("email5", "123");
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void whenSaveTheSameEmail() {
        var user = sql2oUserRepository.save(new User(0, "email8", "Kolya", "123", 1));
        assertThatThrownBy(() -> sql2oUserRepository.save(new User(0, "email8", "Kolya", "123", 1)))
                .isInstanceOf(org.sql2o.Sql2oException.class);
    }
}