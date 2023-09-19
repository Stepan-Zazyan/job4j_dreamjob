package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryCandidateRepository implements CandidatesRepository {
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger(0);;
    private MemoryCandidateRepository() {
        save(new Candidate(0, "Vasya", "junior", LocalDateTime.now(), 1, 0));
        save(new Candidate(0, "Kolya", "middle+", LocalDateTime.now(), 1, 0));
        save(new Candidate(0, "Petya", "junior", LocalDateTime.now(), 1, 0));
        save(new Candidate(0, "Ulya", "intern", LocalDateTime.now(), 1, 0));
        save(new Candidate(0, "Taya", "senior", LocalDateTime.now(), 1, 0));
        save(new Candidate(0, "Maya", "junior+", LocalDateTime.now(), 1, 0));
    }


    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(id.incrementAndGet());
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(), (id, oldCandidate) ->
                new Candidate(oldCandidate.getId(), candidate.getName(), candidate.getDescription(),
                        LocalDateTime.now(), candidate.getCityId(), candidate.getFileId())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.of(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
