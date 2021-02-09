package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.Post;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.save(new Post(0, "Java Job"));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        store.save(new Post(1, "Junior Java Job"));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        store.save(new Candidate(0, "Java Developer"));
        for (var can : store.findAllCandidates()) {
            System.out.println(can.getId() + " " + can.getName());
        }
        store.save(new Candidate(1, "Junior Java Developer"));
        for (var can : store.findAllCandidates()) {
            System.out.println(can.getId() + " " + can.getName());
        }
        Post post = store.findPostById(1);
        System.out.println(post.getId() + " " + post.getName());
        Candidate can = store.findCandidateById(1);
        System.out.println(can.getId() + " " + can.getName());
    }
}
