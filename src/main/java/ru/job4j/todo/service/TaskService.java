package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> findAll();

    Optional<Task> findById(int id);

    Task create(Task task);

    void update(Task task);

    void deleteById(int id);
}
