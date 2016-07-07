package com.tradeshift.models.task;

import com.tradeshift.models.User;

import java.util.ArrayList;
import java.util.List;

public class Task implements ITask {
    private int _taskId;
    private String _message;
    private List<User> _users = new ArrayList<>();
    private boolean _completed;

    public int getTaskId() {
        return _taskId;
    }

    public void setTaskId(int taskId) {
        _taskId = taskId;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public List<User> getUsers() {
        return _users;
    }

    public void setUsers(List<User> users) {
        _users = users;
    }

    public void addUser(User user) {
        _users.add(user);
    }

    public boolean isCompleted() {
        return _completed;
    }

    public void setCompleted(boolean completed) {
        _completed = completed;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.GENERAL;
    }
}
