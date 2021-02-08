package ru.job4j.dreamjob.model;

import java.util.Objects;

public class Candidate {
    private int id;
    private String name;

    public Candidate(int id, String name) {
        this.id = id;
        this.name = name;
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

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Candidate candidate = (Candidate) object;
        return id == candidate.id && java.util.Objects.equals(name, candidate.name);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name);
    }
}
