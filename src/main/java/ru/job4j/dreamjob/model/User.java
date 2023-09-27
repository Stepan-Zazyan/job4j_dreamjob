package ru.job4j.dreamjob.model;

import java.util.Objects;

public class User {

    private int id;

    private String email;

    private String name;

    private String password;

    private int fileId;

    public User(int id, String email, String name, String password, int fileId) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.fileId = fileId;
    }

    public User() {
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", email='" + email + '\''
                + ", name='" + name + '\''
                + ", password='" + password + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User that = (User) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
