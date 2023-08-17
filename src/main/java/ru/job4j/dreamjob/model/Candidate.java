package ru.job4j.dreamjob.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Candidate {
    private int id;

    private String name;

    private String description;

    private LocalDateTime creationDate;

    public Candidate(int id, String name, String description, LocalDateTime creationDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkills() {
        return description;
    }

    public void setSkills(String skills) {
        this.description = skills;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Candidate that = (Candidate) o;
        return id == that.id && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, creationDate);
    }

    @Override
    public String toString() {
        return "Candidates{" + "id="
                + id + ", name='"
                + name + '\'' + ", skills='"
                + description + '\'' + ", creationDate="
                + creationDate + '}';
    }
}
