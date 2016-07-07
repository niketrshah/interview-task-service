package com.tradeshift.resources;

import com.tradeshift.error.TaskNotUpdatedException;
import com.tradeshift.models.task.Task;
import com.tradeshift.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/tasks")
public class TaskResource {
    private final TaskService _taskService;

    @Autowired
    public TaskResource(TaskService taskService) {
        _taskService = taskService;
    }

    @RequestMapping(value = "",
            method = POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return new ResponseEntity<>(_taskService.createTask(task), HttpStatus.CREATED);
    }


    @RequestMapping(value = "",
            method = GET,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Task>> getAllTasks(@RequestParam(required = false) Integer userId) {
        List<Task> allTasks;
        if (userId == null) {
            allTasks = _taskService.getTasks();
        } else {
            allTasks = _taskService.getTasks(userId);
        }

        if (allTasks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(allTasks, HttpStatus.OK);
    }

    @RequestMapping(value = "{taskId}",
            method = GET,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> getTask(@PathVariable int taskId) {
        final Task task = _taskService.getTask(taskId);

        if (task == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(task, HttpStatus.OK);
    }


    @RequestMapping(value = "{taskId}/users",
            method = PUT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateTaskUsers(@PathVariable int taskId, @RequestBody Task task) {
        final Task existingTask = _taskService.getTask(taskId);

        if (existingTask == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingTask.setUsers(task.getUsers());

        try {
            _taskService.updateTaskUsers(existingTask);
        } catch (TaskNotUpdatedException e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "{taskId}/status",
            method = PUT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateTaskStatus(@PathVariable int taskId, @RequestBody Task task) {
        final Task existingTask = _taskService.getTask(taskId);

        if (existingTask == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingTask.setCompleted(task.isCompleted());

        _taskService.updateTaskStatus(existingTask);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
