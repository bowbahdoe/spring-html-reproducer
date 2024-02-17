package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.lang.foreign.MemoryLayout;
import java.sql.SQLException;

@Configuration
public class DB {
    @Bean
    @Scope("singleton")
    public DataSource db() throws SQLException {
        var db = new SQLiteDataSource();
        db.setUrl("jdbc:sqlite:test.db");

        try (var conn = db.getConnection()) {
            try (var stmt = conn.prepareStatement("""
                 CREATE TABLE IF NOT EXISTS employee (
                    id text NOT NULL PRIMARY KEY,
                    name TEXT,
                    phone_number TEXT
                 );
                 """)) {
                stmt.execute();
            }
        }

        return db;
    }
}
