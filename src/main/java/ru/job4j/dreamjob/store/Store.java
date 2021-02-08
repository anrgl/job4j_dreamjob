package ru.job4j.dreamjob.store;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ru.job4j.dreamjob.model.Post;

public class Store {
    private static final Store INST = new Store();
    private Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Store() {
        posts.put(1, new Post(1, "Junior Java Job", "Java SE", "2020-01-25"));
        posts.put(2, new Post(2, "Middle Java Job", "Java SE, OOP", "2020-01-26"));
        posts.put(3, new Post(3, "Senior Java Job", "Java EE, Spring", "2020-01-27"));
        posts.put(4, new Post(4, "Junior Kotlin Job", "Kotlin, Android", "2020-01-02"));
        posts.put(5, new Post(5, "Middle Kotlin Job", "Java, Kotlin, Android", "2020-01-15"));
        posts.put(6, new Post(6, "Senior Scala Job", "Scala", "2020-01-16"));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }
}