package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.model.User;

import java.util.Collection;
import java.util.Optional;
@Repository
public class Sql2oUserRepository implements UserRepository {

    private final Sql2o sql2o;

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<User> save(User user) {
        try (var connection = sql2o.open()) {
        String sql = "Insert into users(email, name, password) values (:email, :name, :password)";
        Query query = connection.createQuery(sql, true)
                    .addParameter("email", user.getEmail())
                    .addParameter("name", user.getName())
                    .addParameter("password", user.getPassword());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            user.setId(generatedId);
            return Optional.of(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (Connection connection = sql2o.open()) {
            String sql = "Select * from users where (email = :email and password = :password)";
            Query query = connection.createQuery(sql)
                    .addParameter("email", email)
                    .addParameter("password", password);
            User user = query.executeAndFetchFirst(User.class);
            return Optional.of(user);
        }
    }

    @Override
    public Collection<User> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM users");
            return query.executeAndFetch(User.class);
        }
    }

    @Override
    public boolean deleteById(int id) {
        boolean rsl;
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM users WHERE id = :id");
            query.addParameter("id", id);
            rsl = query.executeUpdate().getResult() > 0;
        }
        return rsl;
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.empty();
    }
}
