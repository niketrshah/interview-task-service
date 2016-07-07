package com.tradeshift.DAOs;

import com.tradeshift.models.task.Task;
import com.tradeshift.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class TaskDAO {
    private static final String TABLE_TASKS = "tasks";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_USERS_TASKS = "users_tasks";

    private final JdbcTemplate _jdbcTemplate;

    @Autowired
    public TaskDAO(JdbcTemplate jdbcTemplate) {
        _jdbcTemplate = jdbcTemplate;
    }

    public Task createTask(Task task) {
        final String sql = "INSERT INTO " + TABLE_TASKS + " (message, is_completed) VALUES (?, ?)";
        _jdbcTemplate.update(sql, task.getMessage(), task.isCompleted());

        final Task newlyCreatedTask = getNewlyCreatedTask();

        if (newlyCreatedTask == null) {
            return null;
        }

        newlyCreatedTask.setUsers(task.getUsers());
        updateTaskUsers(newlyCreatedTask);

        return getTask(newlyCreatedTask.getTaskId());
    }

    public List<Task> getAllTasks() {
        String sql = "SELECT * FROM " + TABLE_TASKS +
                " LEFT OUTER JOIN " + TABLE_USERS_TASKS + " USING (task_id)" +
                " LEFT OUTER JOIN " + TABLE_USERS + " USING (user_id)" +
                " ORDER BY task_id, user_id";
        final TasksRowCallbackHandler tasksRowCallbackHandler = new TasksRowCallbackHandler();
        _jdbcTemplate.query(sql, tasksRowCallbackHandler);

        return tasksRowCallbackHandler.getTasks();
    }

    public List<Task> getAllTasks(int userId) {
        final List<Task> allTasks = getAllTasks();

        return allTasks.stream().filter(task -> hasUserId(task, userId)).collect(Collectors.toList());
    }

    public Task getTask(int taskId) {
        String sql = "SELECT * FROM " + TABLE_TASKS +
                " LEFT OUTER JOIN " + TABLE_USERS_TASKS + " USING (task_id)" +
                " LEFT OUTER JOIN " + TABLE_USERS + " USING (user_id)" +
                " WHERE tasks.task_id = ?" +
                " ORDER BY user_id";


        final TasksRowCallbackHandler tasksRowCallbackHandler = new TasksRowCallbackHandler();
        _jdbcTemplate.query(sql, tasksRowCallbackHandler, taskId);

        final List<Task> tasks = tasksRowCallbackHandler.getTasks();

        return tasks.size() == 1 ? tasks.get(0) : null;
    }

    public void updateTaskUsers(Task task) {
        int taskId = task.getTaskId();

        final List<Integer> taskUserIds = task.getUsers().stream().map(User::getUserId).collect(Collectors.toList());
        Set<Integer> userIdSet = new HashSet<>(taskUserIds);
        List<Integer> userIds = new ArrayList<>(userIdSet);

        final String tasksUsersDeleteSql = "DELETE FROM " + TABLE_USERS_TASKS + " WHERE task_id = ?";
        _jdbcTemplate.update(tasksUsersDeleteSql, taskId);

        if (userIds.size() > 0) {
            final String tasksUsersInsertSql = "INSERT INTO " + TABLE_USERS_TASKS + " (task_id, user_id) VALUES (?, ?)";
            _jdbcTemplate.batchUpdate(tasksUsersInsertSql, new TaskUsesrBatchPSSetter(taskId, userIds));
        }
    }

    public void updateTaskStatus(Task task) {
        int taskId = task.getTaskId();

        final String taskUpdateSql = "UPDATE " + TABLE_TASKS +
                " SET is_completed = ?" +
                " WHERE task_id = ?";
        _jdbcTemplate.update(taskUpdateSql, task.isCompleted(), taskId);
    }

    private boolean hasUserId(Task task, int userId) {
        for (User user : task.getUsers()) {
            if (user.getUserId() == userId) {
                return true;
            }
        }

        return false;
    }

    private Task getNewlyCreatedTask() {
        String sql = "SELECT * FROM " + TABLE_TASKS +
                " LEFT OUTER JOIN " + TABLE_USERS_TASKS + " USING (task_id)" +
                " LEFT OUTER JOIN " + TABLE_USERS + " USING (user_id)" +
                " ORDER BY task_id DESC" +
                " LIMIT 1";


        final TasksRowCallbackHandler tasksRowCallbackHandler = new TasksRowCallbackHandler();
        _jdbcTemplate.query(sql, tasksRowCallbackHandler);

        final List<Task> tasks = tasksRowCallbackHandler.getTasks();

        return tasks.size() == 1 ? tasks.get(0) : null;
    }


    public static class TasksRowCallbackHandler implements RowCallbackHandler {


        private final List<Task> _tasks = new ArrayList<>();
        private Task _task = null;
        private User _user = null;

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            final int taskId = rs.getInt("task_id");

            if (_task == null || taskId != _task.getTaskId()) {
                _task = new Task();
                _user = null;

                _task.setTaskId(taskId);
                _task.setMessage(rs.getString("message"));
                _task.setCompleted(rs.getBoolean("is_completed"));

                _tasks.add(_task);
            }

            final int userId = rs.getInt("user_id");

            if (userId != 0 && (_user == null || _user.getUserId() != userId)) {
                _user = new User();
                _user.setUserId(userId);
                _user.setName(rs.getString("name"));

                _task.addUser(_user);
            }
        }

        public List<Task> getTasks() {
            return _tasks;
        }

    }

    public static class TaskUsesrBatchPSSetter implements BatchPreparedStatementSetter {


        private final int _taskId;
        private final List<Integer> _userIds;

        public TaskUsesrBatchPSSetter(int taskId, List<Integer> userIds) {
            _taskId = taskId;
            _userIds = userIds;
        }

        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setInt(1, _taskId);
            ps.setInt(2, _userIds.get(i));
        }

        @Override
        public int getBatchSize() {
            return _userIds.size();
        }

    }
}
