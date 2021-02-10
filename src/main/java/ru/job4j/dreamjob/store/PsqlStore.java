package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.Post;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class PsqlStore implements Store {
    private static final Logger LOG = LogManager.getLogger(PsqlStore.class.getName());
    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select * from post")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select * from candidate")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return candidates;
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    @Override
    public Post findPostById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select * from post where id = ?")) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Post(id, it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            create(candidate);
        } else {
            update(candidate);
        }
    }

    @Override
    public Candidate findCandidateById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select * from candidate where id = ?")) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Candidate(id, it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    private void create(Post post) {
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement(
                    "insert into post(name) values (?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    private void update(Post post) {
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement(
                    "update post set name = ? where id = ?"
            )) {
            ps.setString(1, post.getName());
            ps.setInt(2, post.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    private void create(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "insert into candidate(name) values (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, candidate.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    private void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "update candidate set name = ? where id = ?"
             )) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error(e);
        }
    }
}
