package com.example.Database;

import java.sql.*;
import java.util.Scanner;
import org.jetbrains.annotations.NotNull;

import java.io.*;

enum Mode { SILENT, INTERACTIVE }

public class DBClient {

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        for (int i = 1; i < args.length;) {
            if (args[i].equals("-h") || args[i].equals("--help")) {
                System.out.print("This is the phone database client.\n\n");
                printHelp();
                return;
            } else if (args[i].equals("-o") || args[i].equals("--output")) {
                if (args.length == i + 1) {
                    System.out.print("You have not wrote filename!\n\n");
                    printHelp();
                    return;
                } else {
                    try {
                        out = new BufferedOutputStream(new FileOutputStream(args[i + 1]));
                    } catch (FileNotFoundException e) {
                        System.out.print("File \"" + args[i + 1] + "\" not found.\n");
                    }
                    i += 2;
                }
            } else if (args[i].equals("-i") || args[i].equals("--input")) {
                if (args.length == i + 1) {
                    System.out.print("You have not wrote filename!\n\n");
                    printHelp();
                    return;
                } else {
                    try {
                        in = new BufferedInputStream(new FileInputStream(args[i + 1]));
                    } catch (FileNotFoundException e) {
                        System.out.print("File \"" + args[i + 1] + "\" not found.\n");
                    }
                    i += 2;
                }
            } else {
                System.out.print("Unknown key!\n");
                printHelp();
                return;
            }
        }

        Mode mode = Mode.SILENT;
        if (out == null) {
            out = new BufferedOutputStream(System.out);
        }
        if (in == null) {
            in = new BufferedInputStream(System.in);
            mode = Mode.INTERACTIVE;
        }

        try {
            run(in, out, args[0], mode);
        } catch (IOException e) {
            System.out.print("IO error: " + e.getMessage() + "\n");
        } catch (SQLException e) {
            System.out.print("DB error: " + e.getMessage() + "\n");
        }
    }


    public static void run(@NotNull InputStream in,
                           @NotNull OutputStream out,
                           @NotNull String dbName,
                           @NotNull Mode mode) throws IOException, SQLException {
        var scanner = new Scanner(in);
        var writer = new OutputStreamWriter(out);
        var connection = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
        var statement = connection.createStatement();
        ResultSet result;

        int command;
        String name;
        String newName;
        String number;
        String newNumber;

        while (true) {
            if (mode == Mode.INTERACTIVE) {
                writer.write(dbName + "> ");
                writer.flush();
            }
            command = scanner.nextInt();

            switch (command) {
                case 0:
                    return;
                case 1:
                    name = scanner.next();
                    number = scanner.next();

                    statement.executeUpdate(
                            "INSERT INTO Numbers(Number, Name) VALUES (\'" +
                                    number + "\', \'" +
                                    name + "\');");
                    break;
                case 2:
                    name = scanner.next();
                    result = statement.executeQuery(
                            "SELECT Number FROM Numbers WHERE Name = \'" + name + "\';");

                    while (result.next()) {
                        writer.write(result.getString("Number") + " ");
                    }
                    writer.write('\n');
                    writer.flush();

                    break;
                case 3:
                    number = scanner.next();
                    result = statement.executeQuery(
                            "SELECT Name FROM Numbers WHERE Number = \'" + number + "\';");

                    while (result.next()) {
                        writer.write(result.getString("Name") + " ");
                    }
                    writer.write('\n');
                    writer.flush();;

                    break;
                case 4:
                    name = scanner.next();
                    number = scanner.next();

                    statement.executeUpdate(
                            "DELETE FROM Numbers WHERE Name = \'" +
                                    name + "\' AND Number = \'" +
                                    number + "\';");

                    break;
                case 5:
                    name = scanner.next();
                    number = scanner.next();
                    newName = scanner.next();

                    statement.executeUpdate(
                            "UPDATE Numbers SET Name = \'" +
                                    newName + "\' WHERE Name = \'" +
                                    name + "\' AND Number = \'" +
                                    number + "\';");

                    break;
                case 6:
                    name = scanner.next();
                    number = scanner.next();
                    newNumber = scanner.next();

                    statement.executeUpdate(
                            "UPDATE Numbers SET Number = \'" +
                                    newNumber + "\' WHERE Name = \'" +
                                    name + "\' AND Number = \'" +
                                    number + "\';");

                    break;
                case 7:
                    result = statement.executeQuery("SELECT * FROM Numbers;");

                    while (result.next()) {
                        writer.write(
                                result.getString("Name") + " " +
                                result.getString("Number") + "\n");
                    }
                    writer.flush();

                    break;
                case 8:
                    if (mode == Mode.INTERACTIVE) {
                        printCommands();
                    }
                    break;
                default:
                    writer.write("No such command!");
                    writer.flush();
                    break;
            }
        }
    }

    private static void printHelp() {
        System.out.print(
                "Usage:\tDBClient [BASE NAME] [KEY]..." +
                "Keys:\n" +
                "\t-h (--help) get help\n" +
                "\t-o (--output) [FILE] write results in FILE\n" +
                "\t-i (--input) [FILE] get commands from FILE\n");
        printCommands();
    }

    private static void printCommands() {
        System.out.print(
                "Commands:\n" +
                        "\t0 - exit\n" +
                        "\t1 [NAME] [NUMBER] - add a record NAME-NUMBER\n" +
                        "\t2 [NAME] - get phones by NAME\n" +
                        "\t3 [NUMBER] - get names by NUMBER\n" +
                        "\t4 [NAME] [NUMBER] - remove a record NAME-NUMBER\n" +
                        "\t5 [NAME] [NUMBER] [NEW NAME] - change name in a record NAME-NUMBER\n" +
                        "\t6 [NAME] [NUMBER] [NEW NUMBER] - change phone in a record NAME-NUMBER\n" +
                        "\t7 - print all records\n" +
                        "\t8 - print list of commands (if you use console)\n" +
                        "\tAll names and phones does not contains whitespaces!\n");
    }
}