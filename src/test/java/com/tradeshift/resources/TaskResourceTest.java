package com.tradeshift.resources;

import com.tradeshift.BaseTest;
import com.tradeshift.error.TaskNotUpdatedException;
import com.tradeshift.models.task.Task;
import com.tradeshift.models.User;
import com.tradeshift.services.TaskService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskResourceTest extends BaseTest {
    private MockMvc _mockMvc;
    private TaskService _mockTaskService;

    @Before
    public void setUp() throws Exception {
        _mockTaskService = EasyMock.createMock(TaskService.class);
        _mockMvc = MockMvcBuilders.standaloneSetup(new TaskResource(_mockTaskService)).build();
    }

    @Test
    public void testGetAllTasksWithNoContent() throws Exception {
        EasyMock.expect(_mockTaskService.getTasks()).andReturn(Collections.emptyList());

        EasyMock.replay(_mockTaskService);

        _mockMvc.perform(get("/tasks").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetAllTasks() throws Exception {
        final User user1 = createUser(1);
        final Task task1 = createTask(1, true);
        task1.setUsers(Collections.singletonList(user1));

        final User user2 = createUser(2);
        final User user3 = createUser(3);
        final Task task2 = createTask(2, false);
        task2.setUsers(Arrays.asList(user2, user3));

        EasyMock.expect(_mockTaskService.getTasks()).andReturn(Arrays.asList(task1, task2));

        EasyMock.replay(_mockTaskService);

        final ResultActions result = _mockMvc.perform(get("/tasks").accept(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$", hasSize(2)));

        result.andExpect(jsonPath("$[0].taskId", is(1)));
        result.andExpect(jsonPath("$[0].message", is("task 1")));
        result.andExpect(jsonPath("$[0].completed", is(true)));
        result.andExpect(jsonPath("$[0].users", hasSize(1)));
        result.andExpect(jsonPath("$[0].users[0].userId", is(1)));
        result.andExpect(jsonPath("$[0].users[0].name", is("user 1")));

        result.andExpect(jsonPath("$[1].taskId", is(2)));
        result.andExpect(jsonPath("$[1].message", is("task 2")));
        result.andExpect(jsonPath("$[1].completed", is(false)));

        result.andExpect(jsonPath("$[1].users", hasSize(2)));

        result.andExpect(jsonPath("$[1].users[0].userId", is(2)));
        result.andExpect(jsonPath("$[1].users[0].name", is("user 2")));

        result.andExpect(jsonPath("$[1].users[1].userId", is(3)));
        result.andExpect(jsonPath("$[1].users[1].name", is("user 3")));
    }

    @Test
    public void testGetAllTasksWithUserIdWithNoContent() throws Exception {
        EasyMock.expect(_mockTaskService.getTasks(1)).andReturn(Collections.emptyList());

        EasyMock.replay(_mockTaskService);

        _mockMvc.perform(get("/tasks?userId=1").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetTasksForUser() throws Exception {
        final User user1 = createUser(1);
        final User user2 = createUser(2);
        final User user3 = createUser(3);

        final Task task1 = createTask(1, true);
        task1.setUsers(Arrays.asList(user1, user2));

        final Task task2 = createTask(2, false);
        task2.setUsers(Arrays.asList(user2, user3));

        EasyMock.expect(_mockTaskService.getTasks(2)).andReturn(Arrays.asList(task1, task2));

        EasyMock.replay(_mockTaskService);

        final ResultActions result = _mockMvc.perform(get("/tasks?userId=2").accept(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$", hasSize(2)));

        result.andExpect(jsonPath("$[0].taskId", is(1)));
        result.andExpect(jsonPath("$[0].message", is("task 1")));
        result.andExpect(jsonPath("$[0].completed", is(true)));

        result.andExpect(jsonPath("$[0].users", hasSize(2)));

        result.andExpect(jsonPath("$[0].users[0].userId", is(1)));
        result.andExpect(jsonPath("$[0].users[0].name", is("user 1")));

        result.andExpect(jsonPath("$[0].users[1].userId", is(2)));
        result.andExpect(jsonPath("$[0].users[1].name", is("user 2")));

        result.andExpect(jsonPath("$[1].taskId", is(2)));
        result.andExpect(jsonPath("$[1].message", is("task 2")));
        result.andExpect(jsonPath("$[1].completed", is(false)));

        result.andExpect(jsonPath("$[1].users", hasSize(2)));

        result.andExpect(jsonPath("$[1].users[0].userId", is(2)));
        result.andExpect(jsonPath("$[1].users[0].name", is("user 2")));

        result.andExpect(jsonPath("$[1].users[1].userId", is(3)));
        result.andExpect(jsonPath("$[1].users[1].name", is("user 3")));
    }

    @Test
    public void testGetTaskNotFound() throws Exception {
        EasyMock.expect(_mockTaskService.getTask(2)).andReturn(null);

        EasyMock.replay(_mockTaskService);

        final ResultActions result = _mockMvc.perform(get("/tasks/2").accept(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void testGetTask() throws Exception {
        final User user1 = createUser(1);
        final Task task1 = createTask(1, true);
        task1.setUsers(Collections.singletonList(user1));

        EasyMock.expect(_mockTaskService.getTask(1)).andReturn(task1);

        EasyMock.replay(_mockTaskService);

        final ResultActions result = _mockMvc.perform(get("/tasks/1").accept(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.taskId", is(1)));
        result.andExpect(jsonPath("$.message", is("task 1")));
        result.andExpect(jsonPath("$.completed", is(true)));
        result.andExpect(jsonPath("$.users", hasSize(1)));
        result.andExpect(jsonPath("$.users[0].userId", is(1)));
        result.andExpect(jsonPath("$.users[0].name", is("user 1")));
    }

    @Test
    public void testCreateTask() throws Exception {
        final User user1 = createUser(1);
        final Task task1 = createTask(1, true);
        task1.setUsers(Collections.singletonList(user1));

        EasyMock.expect(_mockTaskService.createTask(EasyMock.anyObject(Task.class))).andReturn(task1);

        EasyMock.replay(_mockTaskService);

        final ResultActions result = _mockMvc.perform(post("/tasks") //
                .contentType(MediaType.APPLICATION_JSON_VALUE) //
                .content("{\"message\":\"task 1\",\"users\":[{\"name\":\"user 1\",\"userId\":1}],\"completed\":true}"));

        result.andExpect(status().isCreated());

        result.andExpect(jsonPath("$.taskId", is(1)));
        result.andExpect(jsonPath("$.message", is("task 1")));
        result.andExpect(jsonPath("$.completed", is(true)));
        result.andExpect(jsonPath("$.users", hasSize(1)));
        result.andExpect(jsonPath("$.users[0].userId", is(1)));
        result.andExpect(jsonPath("$.users[0].name", is("user 1")));
    }

    @Test
    public void testUpdateTaskUsersNotModified() throws Exception, TaskNotUpdatedException {
        final User user1 = createUser(1);
        final User user2 = createUser(2);
        final Task task1 = createTask(1, true);
        task1.setUsers(Arrays.asList(user1, user2));

        EasyMock.expect(_mockTaskService.getTask(1)).andReturn(task1);
        _mockTaskService.updateTaskUsers(EasyMock.anyObject(Task.class));
        EasyMock.expectLastCall().andThrow(new TaskNotUpdatedException("Task Not Updated"));

        EasyMock.replay(_mockTaskService);

        final ResultActions result = _mockMvc.perform(put("/tasks/1/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE) //
                .content("{\"users\":[{\"userId\":1},{\"userId\":2}]}"));

        result.andExpect(status().isNotModified());
    }

    @Test
    public void testUpdateTaskUsers() throws Exception, TaskNotUpdatedException {
        final User user1 = createUser(1);
        final User user2 = createUser(2);
        final Task task1 = createTask(1, true);
        task1.setUsers(Arrays.asList(user1, user2));

        EasyMock.expect(_mockTaskService.getTask(1)).andReturn(task1);
        _mockTaskService.updateTaskUsers(EasyMock.anyObject(Task.class));
        EasyMock.expectLastCall();

        EasyMock.replay(_mockTaskService);

        final ResultActions result = _mockMvc.perform(put("/tasks/1/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE) //
                .content("{\"users\":[{\"userId\":1},{\"userId\":2}]}"));

        result.andExpect(status().isOk());
    }

    @Test
    public void testUpdateTaskStatus() throws Exception, TaskNotUpdatedException {
        final User user1 = createUser(1);
        final User user2 = createUser(2);
        final Task task1 = createTask(1, false);
        task1.setUsers(Arrays.asList(user1, user2));

        EasyMock.expect(_mockTaskService.getTask(1)).andReturn(task1);
        _mockTaskService.updateTaskStatus(EasyMock.anyObject(Task.class));
        EasyMock.expectLastCall();

        EasyMock.replay(_mockTaskService);

        final ResultActions result = _mockMvc.perform(put("/tasks/1/status")
                .contentType(MediaType.APPLICATION_JSON_VALUE) //
                .content("{\"completed\":true}"));

        result.andExpect(status().isOk());
    }

    private User createUser(int i) {
        User user = new User();
        user.setName("user " + i);
        user.setUserId(i);

        return user;
    }

    private Task createTask(int i, boolean completed) {
        Task task = new Task();
        task.setTaskId(i);
        task.setCompleted(completed);
        task.setMessage("task " + i);

        return task;
    }
}