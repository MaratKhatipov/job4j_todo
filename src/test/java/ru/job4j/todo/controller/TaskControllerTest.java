package ru.job4j.todo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.Arrays;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TaskControllerTest {
    private TaskService taskService = mock(TaskService.class);

    private Model model = mock(Model.class);

    @Test
    void whenGetIndex() {
        List<Task> tasks = Arrays.asList(
                new Task(1, "Desk", LocalDateTime.now(), false),
                new Task(2, "DeskTwo", LocalDateTime.now(), false),
                new Task(3, "DeskThree", LocalDateTime.now(), false)
        );
        when(taskService.findAll()).thenReturn(tasks);
        TaskController taskController = new TaskController(taskService);

        String page = taskController.getIndex(model);
        System.out.println(page);

        verify(model).addAttribute("tasks", taskService.findAll());
        assertEquals(page, "index");
    }

    @Test
    void whenShowCreate() {
        Task task = new Task();
        TaskController taskController = new TaskController(taskService);

        String page = taskController.showCreate(model);
        System.out.println(page);

        assertEquals(page, "/task/create");
    }

    @Test
    void whenCreateTask() {
        Task task = new Task(1, "desk", LocalDateTime.now(), false);
        when(taskService.create(task)).thenReturn(task);
        TaskController taskController = new TaskController(taskService);

        String page = taskController.createTask(task, model);
        System.out.println(page);

        assertEquals(page, "redirect:/");
    }

    @Test
    void whenFalseCreateTask() {
        Task task = null;
        when(taskService.create(task)).thenReturn(task);
        TaskController taskController = new TaskController(taskService);

        var page = taskController.createTask(task, model);
        System.out.println(page);

        verify(model).addAttribute("message", "task not created");
        assertEquals(page, "errors/addTaskError");
    }

    @Test
    void whenShowTaskById() {
        Optional<Task> optionalTask = Optional.of(new Task(1, "desk"));
        int id = 1;
        when(taskService.findById(id)).thenReturn(optionalTask);
        TaskController taskController = new TaskController(taskService);

        var page = taskController.showTaskById(id, model);
        System.out.println(page);

        verify(model).addAttribute("task", optionalTask.get());
        assertEquals(page, "task/simpleShow");
    }

    @Test
    void whenShowTaskByIdFalse() {
        Optional<Task> optionalTask = Optional.empty();
        int id = 1;
        when(taskService.findById(id)).thenReturn(optionalTask);
        TaskController taskController = new TaskController(taskService);

        var page = taskController.showTaskById(id, model);
        System.out.println(page);

        verify(model).addAttribute("message", "task not found");
        assertEquals(page, "errors/404");
    }

    @Test
    void whenEditTaskById() {
        Optional<Task> optionalTask = Optional.of(new Task(1, "desk"));
        int id = 1;
        when(taskService.findById(id)).thenReturn(optionalTask);
        TaskController taskController = new TaskController(taskService);

        var page = taskController.editTaskById(id, model);
        System.out.println(page);

        verify(model).addAttribute("task", optionalTask.get());
        assertEquals(page, "task/simpleEdit");
    }

    @Test
    void whenEditTaskByIdFalse() {
        Optional<Task> optionalTask = Optional.empty();
        int id = 1;
        when(taskService.findById(id)).thenReturn(optionalTask);
        TaskController taskController = new TaskController(taskService);

        var page = taskController.editTaskById(id, model);
        System.out.println(page);

        verify(model).addAttribute("message", "task not found");
        assertEquals(page, "errors/404");
    }

    @Test
    void whenUpdate() {
        Task task = new Task(1, "desk");
        TaskController taskController = new TaskController(taskService);

        var page = taskController.update(task);
        System.out.println(page);

        verify(taskService, times(1)).update(task);
        assertEquals(page, "redirect:/");
    }

    @Test
    void whenCompleteTask() {
        Task task = new Task(1, "desk", LocalDateTime.now(), false);
        TaskController taskController = new TaskController(taskService);

        var page = taskController.complete(task);
        System.out.println(page);

        task.setDone(true);
        verify(taskService, times(1)).update(task);
        assertEquals(page, "redirect:/");
    }

    @Test
    void whenDelete() {
        int id = 1;
        TaskController taskController = new TaskController(taskService);

        var page = taskController.delete(id);
        System.out.println(page);

        verify(taskService, times(1)).deleteById(id);
        assertEquals(page, "redirect:/");
    }

    @Test
    void whenShowOnlyDoneTask() {
        List<Task> tasks = Arrays.asList(
            new Task(1, "Desk", LocalDateTime.now(), false),
            new Task(2, "DeskTwo", LocalDateTime.now(), true),
            new Task(3, "DeskThree", LocalDateTime.now(), false),
            new Task(3, "DeskFour", LocalDateTime.now(), true)
    );
        when(taskService.findAll()).thenReturn(tasks);
        TaskController taskController = new TaskController(taskService);

        String page = taskController.showOnlyDoneTask(model);
        System.out.println(page);

        verify(model).addAttribute("tasks", taskService.findAll()
                .stream()
                .filter(Task::isDone)
                .collect(Collectors.toList())
        );
        assertEquals(page, "index");
    }

    @Test
    void whenShowOnlyNotDoneTask() {
        List<Task> tasks = Arrays.asList(
                new Task(1, "Desk", LocalDateTime.now(), false),
                new Task(2, "DeskTwo", LocalDateTime.now(), true),
                new Task(3, "DeskThree", LocalDateTime.now(), false),
                new Task(3, "DeskFour", LocalDateTime.now(), true)
        );
        when(taskService.findAll()).thenReturn(tasks);
        TaskController taskController = new TaskController(taskService);

        String page = taskController.showOnlyNotDoneTask(model);
        System.out.println(page);

        verify(model).addAttribute("tasks", taskService.findAll()
                .stream()
                .filter(p -> !p.isDone())
                .collect(Collectors.toList())
        );
        assertEquals(page, "index");
    }
}
