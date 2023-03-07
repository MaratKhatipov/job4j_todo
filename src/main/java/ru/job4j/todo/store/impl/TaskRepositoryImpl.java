package ru.job4j.todo.store.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.TaskRepository;

import java.util.List;
import java.util.Optional;

@Log4j
@AllArgsConstructor
@Repository
public class TaskRepositoryImpl implements TaskRepository {
    private final SessionFactory sf;

    @Override
    public List<Task> findAll() {
        List<Task> result;
        Session session = sf.openSession();
        session.beginTransaction();
        Query<Task> query = session.createQuery("from Task", Task.class);
        session.getTransaction().commit();
        result = query.list();
        log.info("find all tasks");
        return result;
    }

    @Override
    public Optional<Task> findById(int id) {
        Optional<Task> result;
        Session session = sf.openSession();
        session.beginTransaction();
        Query<Task> query = session
                .createQuery("from Task as i where i.id = :fId", Task.class)
                .setParameter("fId", id);
        result = query.uniqueResultOptional();
        session.getTransaction().commit();
        return result;
    }

    @Override
    public Task create(Task task) {
        Task result;
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(task);
        session.getTransaction().commit();
        result = task;
        return result;
    }

    @Override
    public void update(Task task) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("""
update Task set description = :fDescription, created = :fCreated, done = :fDone where id = :fId""")
                .setParameter("fId", task.getId())
                .setParameter("fDescription", task.getDescription())
                .setParameter("fCreated", task.getCreated())
                .setParameter("fDone", task.isDone())
                .executeUpdate();
        session.getTransaction().commit();
    }

    @Override
    public void deleteById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete Task as i where i.id = :fId")
                .setParameter("fId", id)
                .executeUpdate();
        session.getTransaction().commit();
    }
}
