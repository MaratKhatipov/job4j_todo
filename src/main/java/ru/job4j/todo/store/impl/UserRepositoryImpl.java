package ru.job4j.todo.store.impl;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sf;

    @Override
    public User create(User user) {
        User result;
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        result = user;
        return result;
    }

    @Override
    public Optional<User> findById(int id) {
        Optional<User> result;
        Session session = sf.openSession();
        session.beginTransaction();
        Query<User> query = session
                .createQuery("from User as i where i.id = :fId", User.class)
                .setParameter("fId", id);
        result = query.uniqueResultOptional();
        session.getTransaction().commit();
        return result;
    }

    @Override
    public Optional<User> findByLoginAndPwd(String login, String password) {
        Optional<User> result;
        Session session = sf.openSession();
        session.beginTransaction();
        Query<User> query = session
                .createQuery(
                        "from User as i where i.login = :fLogin and i.password = :fPassword",
                        User.class)
                .setParameter("fLogin", login)
                .setParameter("fPassword", password);
        result = query.uniqueResultOptional();
        session.getTransaction().commit();
        return result;
    }

    @Override
    public List<User> findAll() {
        List<User> result;
        Session session = sf.openSession();
        session.beginTransaction();
        Query<User> query = session.createQuery("from User", User.class);
        session.getTransaction().commit();
        result = query.list();
        return result;
    }

    @Override
    public void deleteById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete User as i where i.id = :fId")
                .setParameter("fId", id)
                .executeUpdate();
        session.getTransaction().commit();
    }
}
