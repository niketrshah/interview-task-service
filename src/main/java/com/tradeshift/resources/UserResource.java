package com.tradeshift.resources;

import com.tradeshift.models.User;
import com.tradeshift.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/users")
public class UserResource {
    private final UserService _userService;

    @Autowired
    public UserResource(UserService taskService) {
        _userService = taskService;
    }

    @RequestMapping(value = "",
            method = GET,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(_userService.getAllUsers(), HttpStatus.OK);
    }


    @ExceptionHandler
    public ResponseEntity<Error> handleGenericException(Exception e) {
        return new ResponseEntity<>(new Error(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
