package com.example.Database;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    private static final String dbName = "./src/test/resources/PhoneNumbers";
    private static final Mode mode = Mode.SILENT;

    @Test
    void getAllRecords_AfterAddingRecords_AllRecordsExists() throws IOException, SQLException {
        var in = new FileInputStream("./src/test/resources/test1.in");
        var out = new FileOutputStream("./src/test/resources/test1.out");

        DBClient.run(in, out, dbName, mode);

        assertTrue(FileUtils.contentEquals(
                new File("./src/test/resources/test1.out"),
                new File("./src/test/resources/test1.expect")));
    }

    @Test
    void putRecords_CorrectSetsForEachName() throws IOException, SQLException {
        var in = new FileInputStream("./src/test/resources/test2.in");
        var out = new FileOutputStream("./src/test/resources/test2.out");

        DBClient.run(in, out, dbName, mode);

        assertTrue(FileUtils.contentEquals(
                new File("./src/test/resources/test2.out"),
                new File("./src/test/resources/test2.expect")));
    }

    @Test
    void putRecords_CorrectSetsForEachNumber() throws IOException, SQLException {
        var in = new FileInputStream("./src/test/resources/test3.in");
        var out = new FileOutputStream("./src/test/resources/test3.out");

        DBClient.run(in, out, dbName, mode);

        assertTrue(FileUtils.contentEquals(
                new File("./src/test/resources/test3.out"),
                new File("./src/test/resources/test3.expect")));
    }

    @Test
    void removeRecords_CorrectSetsForEachName() throws IOException, SQLException {
        var in = new FileInputStream("./src/test/resources/test4.in");
        var out = new FileOutputStream("./src/test/resources/test4.out");

        DBClient.run(in, out, dbName, mode);

        assertTrue(FileUtils.contentEquals(
                new File("./src/test/resources/test4.out"),
                new File("./src/test/resources/test4.expect")));
    }

    @Test
    void removeRecords_CorrectSetsForEachNumber() throws IOException, SQLException {
        var in = new FileInputStream("./src/test/resources/test5.in");
        var out = new FileOutputStream("./src/test/resources/test5.out");

        DBClient.run(in, out, dbName, mode);

        assertTrue(FileUtils.contentEquals(
                new File("./src/test/resources/test5.out"),
                new File("./src/test/resources/test5.expect")));
    }

    @Test
    void changeRecordName_CorrectSetsForEachName() throws IOException, SQLException {
        var in = new FileInputStream("./src/test/resources/test6.in");
        var out = new FileOutputStream("./src/test/resources/test6.out");

        DBClient.run(in, out, dbName, mode);

        assertTrue(FileUtils.contentEquals(
                new File("./src/test/resources/test6.out"),
                new File("./src/test/resources/test6.expect")));
    }

    @Test
    void changeRecordNumber_CorrectSetForEachNumber() throws IOException, SQLException {
        var in = new FileInputStream("./src/test/resources/test7.in");
        var out = new FileOutputStream("./src/test/resources/test7.out");

        DBClient.run(in, out, dbName, mode);

        assertTrue(FileUtils.contentEquals(
                new File("./src/test/resources/test7.out"),
                new File("./src/test/resources/test7.expect")));
    }

    @AfterEach
    void clearTable() throws SQLException {
        var connection = DriverManager.getConnection("jdbc:sqlite:./src/test/resources/PhoneNumbers.db");
        var statement = connection.createStatement();

        statement.executeUpdate("DELETE FROM Numbers");
    }
}