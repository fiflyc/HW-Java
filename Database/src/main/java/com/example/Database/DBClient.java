package com.example.Database;

import java.sql.*;
import java.util.Scanner;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class DBClient {

    public static void main(String[] args) {
        BufferedOutputStream out = null;

        if (args.length > 0) {
            if (args[0].equals("-h") || args[0].equals("--help")) {
                System.out.print("This is the phone database client.\n\n");
                printHelp();
                return;
            } else if (args[0].equals("-o") || args[0].equals("--output")) {
                if (args.length == 1) {
                    System.out.print("You have not wrote filename!\n\n");
                    printHelp();
                    return;
                } else {
                    try {
                        out = new BufferedOutputStream (new FileOutputStream(args[1]));
                    } catch (FileNotFoundException e) {
                        System.out.print("File \"" + args[1] + "\" not found.\n");
                    }
                }
            }
        }

        if (out == null) {
            out = new BufferedOutputStream(System.out);
        }

        try {
            run(System.in, out, "PhoneNumbers");
        } catch (IOException e) {
            System.out.print("Error: " + e.getMessage() + "\n");
        } catch (SQLException e) {
            System.out.print("Error: " + e.getMessage() + "\n");
        }
    }


    public static void run(@NotNull InputStream in, @NotNull OutputStream out, @NotNull String dbName) throws IOException, SQLException {
        var scanner = new Scanner(in);
        var writer = new OutputStreamWriter(out);
        var connection = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
        var statement = connection.createStatement();
        ResultSet result;

        int command = scanner.nextInt();
        String name;
        String newName;
        String number;
        String newNumber;

        while (command != 0) {
            switch (command) {
                case 1:
                    name = scanner.next();
                    number = scanner.next();

                    statement.executeUpdate(
                            "INSERT INTO Numbers(Number, Name) VALUES (\'" +
                                    number + "\', \'" +
                                    name + "\');");
                case 2:
                    name = scanner.next();
                    result = statement.executeQuery(
                            "SELECT Number FROM Numbers WHERE Name = \'" + name + "\';");

                    while (result.next()) {
                        writer.write(result.getString("Number") + " ");
                    }
                    writer.write('\n');
                case 3:
                    number = scanner.next();
                    result = statement.executeQuery(
                            "SELECT Name FROM Numbers WHERE Number = \'" + number + "\';");

                    while (result.next()) {
                        writer.write(result.getString("Name") + " ");
                    }
                    writer.write('\n');
                case 4:
                    name = scanner.next();
                    number = scanner.next();

                    statement.executeUpdate(
                            "DELETE FROM Numbers WHERE Name = \'" +
                                    name + "\' AND Number = \'" +
                                    number + "\';");
                case 5:
                    name = scanner.next();
                    number = scanner.next();
                    newName = scanner.next();

                    statement.executeUpdate(
                            "UPDATE Numbers SET Name = \'" +
                                    newName + "\' WHERE Name = \'" +
                                    name + "\' AND Number = \'" +
                                    number + "\';");
                case 6:
                    name = scanner.next();
                    number = scanner.next();
                    newNumber = scanner.next();

                    statement.executeUpdate(
                            "UPDATE Numbers SET Number = \'" +
                                    newNumber + "\' WHERE Name = \'" +
                                    name + "\' AND Number = \'" +
                                    number + "\';");
                case 7:
                    result = statement.executeQuery("SELECT * FROM Numbers;");

                    while (result.next()) {
                        writer.write(
                                result.getString("Name") + " " +
                                result.getString("Number") + "\n");
                    }
                default:
                    writer.write("No such command!");
            }

            command = scanner.nextInt();
        }
    }

    private static void printHelp() {
        System.out.print(
                "Options:\n\t-h (--help) get help\n\t-o (--output) [FILE] write results in FILE\n" +
                "Usage:\n" +
                "\t0 - exit\n" +
                "\t1 [NAME] [NUMBER] - add a record NAME-NUMBER\n" +
                "\t2 [NAME] - get phones by NAME\n" +
                "\t3 [NUMBER] - get names by NUMBER\n" +
                "\t4 [NAME] [NUMBER] - remove a record NAME-NUMBER\n" +
                "\t5 [NAME] [NUMBER] [NEW NAME] - change name in a record NAME-NUMBER\n" +
                "\t6 [NAME] [NUMBER] [NEW NUMBER] - change phone in a record NAME-NUMBER\n" +
                "\t7 - print all records\n" +
                "\tAll names and phones does not contains whitespaces!\n");
    }
}