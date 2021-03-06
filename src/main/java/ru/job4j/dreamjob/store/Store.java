package ru.job4j.dreamjob.store;

import java.util.Collection;

import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.model.User;

public interface Store {
    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    void save(Post post);

    Post findPostById(int id);

    int save(Candidate candidate);

    Candidate findCandidateById(int id);

    int savePhoto();

    void updateCandidatePhotoId(int photoId, int candidateId);

    void save(User user);

    User findUserByEmail(String email);
}
