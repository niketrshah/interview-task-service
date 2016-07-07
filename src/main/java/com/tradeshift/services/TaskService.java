package com.tradeshift.services;

import com.tradeshift.DAOs.TaskDAO;
import com.tradeshift.error.TaskNotUpdatedException;
import com.tradeshift.models.task.ITask;
import com.tradeshift.models.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {
    private final TaskDAO _taskDAO;

    @Autowired
    public TaskService(TaskDAO taskDAO) {
        _taskDAO = taskDAO;
    }

    @Transactional
    public Task createTask(Task task) {
        return _taskDAO.createTask(task);
    }

    public List<Task> getTasks() {
        return _taskDAO.getAllTasks();
    }

    public List<Task> getTasks(int userId) {
        return _taskDAO.getAllTasks(userId);
    }

    public Task getTask(int taskId) {
        return _taskDAO.getTask(taskId);
    }

    @Transactional
    public void updateTaskUsers(Task task) throws TaskNotUpdatedException {
        try {
            _taskDAO.updateTaskUsers(task);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TaskNotUpdatedException(e.getMessage());
        }
    }

    public void updateTaskStatus(Task task) {
        _taskDAO.updateTaskStatus(task);
    }

    // To demonstrate some of the different task execution
    // Ideally would apply to any of the above operations
    public void execute(ITask task) {
        switch (task.getTaskType()) {
            case GENERAL:
                break;
            case W9:
                break;
            case INVOICE:
                break;
            case COMPANY_PROFILE:
                break;
        }
    }
}