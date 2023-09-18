package ru.job4j.dreamjob.repository;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.model.Candidate;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oCandidateRepository implements CandidatesRepository {
    private final Sql2o sql2o;

    public Sql2oCandidateRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Candidate save(Candidate vacancy) {
        try (var connection = sql2o.open()) {
            var sql = """
                      INSERT INTO schema_dreamjob.candidates(name, description, creation_date, city_id, file_id)
                      VALUES (:name, :description, :creationDate, :cityId, :fileId)
                      """;
            var query = connection.createQuery(sql, true)
                    .addParameter("name", vacancy.getName())
                    .addParameter("description", vacancy.getDescription())
                    .addParameter("creationDate", vacancy.getCreationDate())
                    .addParameter("cityId", vacancy.getCityId())
                    .addParameter("fileId", vacancy.getFileId());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            vacancy.setId(generatedId);
            return vacancy;
        }
    }

    @Override
    public boolean deleteById(int id) {
        boolean rsl;
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM schema_dreamjob.candidates WHERE id = :id");
            query.addParameter("id", id);
            rsl = query.executeUpdate() != null;
            query.executeUpdate();
        }
        return rsl;
    }

    @Override
    public boolean update(Candidate candidate) {
        try (var connection = sql2o.open()) {
            var sql = """
                    UPDATE schema_dreamjob.candidates
                    SET title = :name, description = :description, creation_date = :creationDate,
                        city_id = :cityId, file_id = :fileId
                    WHERE id = :id
                    """;
            var query = connection.createQuery(sql)
                    .addParameter("name", candidate.getName())
                    .addParameter("description", candidate.getDescription())
                    .addParameter("creationDate", candidate.getCreationDate())
                    .addParameter("cityId", candidate.getCityId())
                    .addParameter("fileId", candidate.getFileId())
                    .addParameter("id", candidate.getId());
            var affectedRows = query.executeUpdate().getResult();
            return affectedRows > 0;
        }
    }

    @Override
    public Optional<Candidate> findById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM schema_dreamjob.candidates WHERE id = :id");
            query.addParameter("id", id);
            var vacancy = query.setColumnMappings(Candidate.COLUMN_MAPPING).executeAndFetchFirst(Candidate.class);
            return Optional.ofNullable(vacancy);
        }
    }

    @Override
    public Collection<Candidate> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM schema_dreamjob.candidates");
            return query.setColumnMappings(Candidate.COLUMN_MAPPING).executeAndFetch(Candidate.class);
        }
    }

}
