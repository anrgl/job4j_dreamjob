package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemStore implements Store {
    private static AtomicInteger postId = new AtomicInteger(4);
    private static AtomicInteger userId = new AtomicInteger(4);
    private static final MemStore INST = new MemStore();
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final Map<Integer, User> users = new ConcurrentHashMap<>();

    private MemStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Java SE", "2020-01-25"));
        posts.put(2, new Post(2, "Middle Java Job", "Java SE, OOP", "2020-01-26"));
        posts.put(3, new Post(3, "Senior Java Job", "Java EE, Spring", "2020-01-27"));
        posts.put(4, new Post(4, "Junior Kotlin Job", "Kotlin, Android", "2020-01-02"));
        posts.put(5, new Post(5, "Middle Kotlin Job", "Java, Kotlin, Android", "2020-01-15"));
        posts.put(6, new Post(6, "Senior Scala Job", "Scala", "2020-01-16"));
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
        users.put(1, new User(1, "Ivan", "ivan@mail.com", "qwerty123"));
        users.put(2, new User(2, "Oleg", "oleg@mail.com", "root"));
        users.put(3, new User(3, "Ana", "ana@mail.com", "!asd4BGD6142@>sdfthY"));
    }

    public static MemStore instOf() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }

    public void save(Post post) {
        if (post.getId() == 0) {
            post.setId(postId.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    public Post findPostById(int id) {
        return posts.get(id);
    }

    public int save(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(postId.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
        return candidate.getId();
    }

    public Candidate findCandidateById(int id) {
        return candidates.get(id);
    }

    @Override
    public int savePhoto() {
        return 0;
    }

    @Override
    public void updateCandidatePhotoId(int photoId, int candidateId) {
        var candidate = candidates.get(candidateId);
        candidate.setPhotoId(photoId);
    }

    @Override
    public void save(User user) {
        if (user.getId() == 0) {
            user.setId(userId.incrementAndGet());
        }
        users.put(user.getId(), user);
    }

    @Override
    public User findUserByEmail(String email) {
        for (User user : users.values()) {
            if (email.equals(user.getEmail())) {
                return user;
            }
        }
        return null;
    }
}
