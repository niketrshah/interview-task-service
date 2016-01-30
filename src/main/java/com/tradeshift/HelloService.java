package com.tradeshift;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class HelloService {
    private HelloDAO helloDAO;

    @Autowired
    public HelloService(HelloDAO helloDAO) {
        this.helloDAO = helloDAO;
    }

    public String sayIt() {
        return "Shift Happens!";
    }

    public String getOne() {
        return helloDAO.getOne();
    }

    public void saveOne(String message) {
        try {
            helloDAO.saveOne(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
