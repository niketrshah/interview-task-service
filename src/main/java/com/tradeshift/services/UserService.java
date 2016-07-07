package com.tradeshift.services;

import com.tradeshift.DAOs.UserDAO;
import com.tradeshift.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserDAO _userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        _userDAO = userDAO;
    }

    public List<User> getAllUsers() {
        return _userDAO.getAllUsers();
    }
}