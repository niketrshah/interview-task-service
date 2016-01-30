package com.tradeshift;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class HelloDAO {
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        createSchema();
    }

    public void createSchema(){
        String sql = "CREATE table IF NOT EXISTS tasks(message text);";
        this.jdbcTemplate.execute(sql);
    }

    public void saveOne(String message) throws SQLException {
        System.out.println("Inserting message " + message);
        this.jdbcTemplate.update("INSERT INTO tasks (message) VALUES (?)", message);
    }

    public String getOne() {
        String sql = "SELECT * FROM tasks LIMIT 1;";
        List<String> messages = this.jdbcTemplate.queryForList(sql, String.class);
        if (messages.isEmpty()) {
            return null;
        } else {
            return messages.get(0);
        }
    }
}
