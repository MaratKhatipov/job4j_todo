package ru.job4j.todo.store.impl;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TaskRepositoryImplTest {

    private static StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private static SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();
    private static TaskRepositoryImpl taskRepository = new TaskRepositoryImpl(sf);

    @BeforeAll
    static void beforeAll() {
        List<Task> tasks = taskRepository.findAll();
        for (Task task : tasks) {
            taskRepository.deleteById(task.getId());
        }

    }

    @Test
    void findAll() {
        Task task1 = taskRepository.create(new Task(
                        1,
                        "desk1",
                        LocalDateTime.of(2023, 1, 25, 16, 25),
                        false));
        Task task2 = taskRepository.create(new Task(
                        2,
                        "desk2",
                        LocalDateTime.of(2022, 2, 15, 15, 25),
                        false));
        Task task3 = taskRepository.create(new Task(
                        3,
                        "desk3",
                        LocalDateTime.of(2021, 3, 5, 11, 25),
                        false));
        Task task4 = taskRepository.create(new Task(
                        4,
                        "desk4",
                        LocalDateTime.of(2020, 4, 16, 13, 25),
                        false));
        List<Task> result = taskRepository.findAll();
        assertThat(result).isEqualTo(List.of(task1, task2, task3, task4));
    }

    @Test
    void whenCreateTaskAndFindById() {
        var task1 = taskRepository.create(new Task(
                1,
                "desk1",
                LocalDateTime.of(2023, 1, 25, 16, 25),
                false));
        var taskExpected = taskRepository.findById(task1.getId()).get();
        assertThat(task1).isEqualTo(taskExpected);
    }

    @Test
    void update() {
        var task1 = taskRepository.create(new Task(
                1,
                "desk1",
                LocalDateTime.of(2023, 1, 25, 16, 25),
                false));
        var taskUpdate = new Task(
                task1.getId(),
                "updateDescription",
                LocalDateTime.of(2023, 1, 25, 16, 25),
                false);
        taskRepository.update(taskUpdate);
        assertThat(taskUpdate).isEqualTo(taskRepository.findById(taskUpdate.getId()).get());

    }

    @Test
    void deleteById() {
        Task task1 = taskRepository.create(new Task(
                1,
                "desk1",
                LocalDateTime.of(2023, 1, 25, 16, 25),
                false));
        Task task2 = taskRepository.create(new Task(
                2,
                "desk2",
                LocalDateTime.of(2022, 2, 15, 15, 25),
                false));
        Task task3 = taskRepository.create(new Task(
                3,
                "desk3",
                LocalDateTime.of(2021, 3, 5, 11, 25),
                false));
        Task task4 = taskRepository.create(new Task(
                4,
                "desk4",
                LocalDateTime.of(2020, 4, 16, 13, 25),
                false));
        List<Task> result = taskRepository.findAll();
        taskRepository.deleteById(task2.getId());
        assertThat(result.size() - 1).isEqualTo(taskRepository.findAll().size());
    }
}