package com.tradeshift.DAOs;

import com.tradeshift.models.task.Task;
import com.tradeshift.models.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TaskDAOTest extends BaseDAOTest {
    private final List<Task> _tasks = new ArrayList<>();
    private final List<User> _users = new ArrayList<>();

    @Autowired
    private TaskDAO _taskDAO;

    @Autowired
    private JdbcTemplate _jdbcTemplate;

    @Autowired
    private UserDAO _userDAO;

    @Before
    public void setUp() throws Exception {
        String sql = "INSERT INTO users (name) VALUES (?)";
        _jdbcTemplate.update(sql, "User 1");
        _jdbcTemplate.update(sql, "User 2");
        _jdbcTemplate.update(sql, "User 3");

        _users.addAll(_userDAO.getAllUsers());

        final Task task1 = new Task();
        task1.setMessage("test message 1");
        task1.setCompleted(true);
        task1.setUsers(Collections.singletonList(_users.get(0)));

        final Task task2 = new Task();
        task2.setMessage("test message 2");
        task2.setCompleted(false);
        task2.setUsers(Arrays.asList(_users.get(1), _users.get(2)));

        _tasks.add(_taskDAO.createTask(task1));
        _tasks.add(_taskDAO.createTask(task2));
    }

    @Test
    public void testCreateTask() throws Exception {
        final Task task = new Task();
        task.setMessage("test message");
        task.setCompleted(true);
        task.setUsers(_users);

        final Task taskCreated = _taskDAO.createTask(task);

        assertTasksEquality(task, taskCreated);

        final List<User> users = taskCreated.getUsers();
        assertEquals("User 1", users.get(0).getName());
        assertEquals("User 2", users.get(1).getName());
        assertEquals("User 3", users.get(2).getName());
    }

    @Test
    public void testGetAllTasks() throws Exception {
        final List<Task> allTasks = _taskDAO.getAllTasks();

        assertEquals(2, allTasks.size());

        final Task task1 = _tasks.get(0);
        assertTasksEquality(task1, allTasks.get(0));

        final Task task2 = _tasks.get(1);
        assertTasksEquality(task2, allTasks.get(1));
    }

    @Test
    public void testGetTask() throws Exception {
        final Task task = _tasks.get(1);
        assertTasksEquality(task, _taskDAO.getTask(task.getTaskId()));
    }

    @Test
    public void testUpdateTaskUsers() throws Exception {
        final Task task = _tasks.get(0);
        task.setUsers(Arrays.asList(_users.get(0), _users.get(2)));
        _taskDAO.updateTaskUsers(task);

        assertTasksEquality(task, _taskDAO.getTask(task.getTaskId()));
    }

    @Test
    public void testUpdateTaskStatus() throws Exception {
        final Task task = _tasks.get(0);
        task.setCompleted(false);
        _taskDAO.updateTaskStatus(task);

        assertTasksEquality(task, _taskDAO.getTask(task.getTaskId()));
    }

    private void assertTasksEquality(Task exptectedTask, Task actualTask) {
        assertEquals(exptectedTask.getMessage(), actualTask.getMessage());
        assertEquals(exptectedTask.isCompleted(), actualTask.isCompleted());
        assertEquals(exptectedTask.getUsers().size(), actualTask.getUsers().size());
    }
}