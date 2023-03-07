package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Controller
public class TaskController {
    private TaskService taskService;

    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "index";
    }

    @GetMapping("task/create")
    public String showCreate(Model model) {
        Task task = new Task();
        model.addAttribute("tasks", task);
        return "/task/create";
    }

    @PostMapping("task/create")
    public String createTask(@ModelAttribute Task task, Model model) {
        Task task1 = taskService.create(task);
        if (task1 == null) {
            model.addAttribute("message", "task not created");
            return "errors/addTaskError";
        }
        return "redirect:/";
    }

    @GetMapping("/task/{id}")
    public String showTaskById(@PathVariable("id") int id, Model model) {
        Optional<Task> optionalTask = taskService.findById(id);
        if (optionalTask.isEmpty()) {
            model.addAttribute("message", "task not found");
            return "errors/404";
        }
        model.addAttribute("task", optionalTask.get());
        return "task/simpleShow";
    }

    @GetMapping("/task/edit/{id}")
    public String editTaskById(@PathVariable("id") int id, Model model) {
        Optional<Task> optionalTask = taskService.findById(id);
        if (optionalTask.isEmpty()) {
            model.addAttribute("message", "task not found");
            return "errors/404";
        }
        model.addAttribute("task", optionalTask.get());
        return "task/simpleEdit";
    }

    @PostMapping("task/update")
    public String update(@ModelAttribute Task task) {
        taskService.update(task);
        return "redirect:/";
    }

    @PostMapping("task/complete")
    public String complete(@ModelAttribute Task task) {
        task.setDone(true);
        taskService.update(task);
        return "redirect:/";
    }

    @GetMapping("task/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        taskService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("task/onlyDone")
    public String showOnlyDoneTask(Model model) {
        model.addAttribute("tasks", taskService.findAll()
                .stream()
                .filter(Task::isDone)
                .collect(Collectors.toList())
        );
        return "index";
    }

    @GetMapping("task/onlyNotDone")
    public String showOnlyNotDoneTask(Model model) {
        model.addAttribute("tasks", taskService.findAll()
                .stream()
                .filter(p -> !p.isDone())
                .collect(Collectors.toList())
        );
        return "index";
    }
}