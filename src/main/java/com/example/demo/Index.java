package com.example.demo;

import dev.mccue.html.Html;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import static dev.mccue.html.Html.HTML;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;


@RestController
public class Index {
    private final DataSource db;

    public Index(DataSource db) {
        this.db = db;
    }

    @GetMapping(value = "/", produces = TEXT_HTML_VALUE)
    public ResponseEntity<Html> index() throws SQLException {
        var rows = new ArrayList<Html>();
        try (var conn = db.getConnection();
             var stmt = conn.prepareStatement("""
                SELECT id, name, phone_number
                FROM employee
                """)) {
            var rs = stmt.executeQuery();
            while (rs.next()) {
                rows.add(HTML."""
                    <li>
                        $$[\{rs.getString("id")}] \{rs.getString("name")} - \{rs.getString("phone_number")}
                    </li>
                    """);
            }
        }

        return ResponseEntity.ok(
                HTML."""
                <ul>
                    \{rows}
                </ul>
                """
        );
    }

    @GetMapping("/create")
    public ResponseEntity<Employee> create() throws SQLException {
        try (var conn = db.getConnection();
             var stmt = conn.prepareStatement("""
                INSERT INTO employee(id, name, phone_number)
                VALUES (?, ?, ?)
                RETURNING id, name, phone_number
                """)) {
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, "bob");
            stmt.setInt(3, 25);

            var rs = stmt.executeQuery();
            rs.next();

            return ResponseEntity.ok(new Employee(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("name"),
                    rs.getString("phone_number")
            ));
        }
    }
}
