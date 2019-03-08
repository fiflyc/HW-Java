package com.example.Database;

import java.sql.*;
import java.util.Scanner;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * Launch type.
 * SILENT -- get commands from file.
 * INTERACTIVE -- get commands from console.
 */
enum Mode { SILENT, INTERACTIVE }

/**
 * Database console client.
 * Run with --help to read how-to-use information.
 */
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

    /**
     * Commands handler.
     * @param in input commands
     * @param out result output
     * @param dbName database name
     * @param mode launch type (get commands from console or file)
     * @throws IOException
     * @throws SQLException
     */
    public static void run(@NotNull InputStream in,
                           @NotNull OutputStream out,
                           @NotNull String dbName,
                           @NotNull Mode mode) throws IOException, SQLException {
        var scanner = new Scanner(in);
        var writer = new OutputStreamWriter(out);
        var connection = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
        var statement = connection.createStatement();

        int command;
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
                    insert(scanner, connection);
                    break;
                case 2:
                    numbersByName(scanner, writer, connection);
                    break;
                case 3:
                    namesByNumber(scanner, writer, connection);
                    break;
                case 4:
                    remove(scanner, connection);
                    break;
                case 5:
                    changeName(scanner, connection);
                    break;
                case 6:
                    changeNumber(scanner, connection);
                    break;
                case 7:
                    printTable(writer, statement);
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

    /**
     * Inserts a record.
     * @throws SQLException
     */
    private static void insert(@NotNull Scanner scanner, @NotNull Connection connection) throws SQLException {
        var name = scanner.next();
        var number = scanner.next();

        var statement = connection.prepareStatement("INSERT INTO Numbers(Number, Name) VALUES (?, ?);");
        statement.setString(1, number);
        statement.setString(2, name);

        statement.execute();
    }

    /**
     * Writes numbers by name.
     * @throws IOException
     * @throws SQLException
     */
    private static void numbersByName(@NotNull Scanner scanner,
                                      @NotNull Writer writer,
                                      @NotNull Connection connection) throws IOException, SQLException {
        var name = scanner.next();
        var statement = connection.prepareStatement("SELECT Number FROM Numbers WHERE Name = ?;");
        statement.setString(1, name);
        var result = statement.executeQuery();

        while (result.next()) {
            writer.write(result.getString("Number") + " ");
        }
        writer.write('\n');
        writer.flush();
    }

    /**
     * Writes names by number.
     * @throws SQLException
     * @throws IOException
     */
    private static void namesByNumber(@NotNull Scanner scanner,
                                      @NotNull Writer writer,
                                      @NotNull Connection connection) throws SQLException, IOException {
        var number = scanner.next();
        var statement = connection.prepareStatement("SELECT Name FROM Numbers WHERE Number = ?;");

        statement.setString(1, number);
        var result = statement.executeQuery();

        while (result.next()) {
            writer.write(result.getString("Name") + " ");
        }
        writer.write('\n');
        writer.flush();
    }

    /**
     * Removes a record.
     * @throws SQLException
     */
    private static void remove(@NotNull Scanner scanner, @NotNull Connection connection) throws SQLException {
        var name = scanner.next();
        var number = scanner.next();

        var statement = connection.prepareStatement("DELETE FROM Numbers WHERE Name = ? AND Number = ?;");
        statement.setString(1, name);
        statement.setString(2, number);

        statement.execute();
    }

    /**
     * Change name in a record.
     * @throws SQLException
     */
    private static void changeName(@NotNull Scanner scanner, @NotNull Connection connection) throws SQLException {
        var name = scanner.next();
        var number = scanner.next();
        var newName = scanner.next();

        var statement = connection.prepareStatement("UPDATE Numbers SET Name = ? WHERE Name = ? AND Number = ?;");
        statement.setString(1, newName);
        statement.setString(2, name);
        statement.setString(3, number);

        statement.execute();
    }

    /**
     * Change number in a record.
     * @throws SQLException
     */
    private static void changeNumber(@NotNull Scanner scanner, @NotNull Connection connection) throws SQLException {
        var name = scanner.next();
        var number = scanner.next();
        var newNumber = scanner.next();

        var statement = connection.prepareStatement("UPDATE Numbers SET Number = ? WHERE Name = ? AND Number = ?;");
        statement.setString(1, newNumber);
        statement.setString(2, name);
        statement.setString(3, number);

        statement.execute();
    }

    /**
     * Prints table.
     * @throws SQLException
     * @throws IOException
     */
    private static void printTable(@NotNull Writer writer, @NotNull Statement statement) throws SQLException, IOException {
        var result = statement.executeQuery("SELECT * FROM Numbers;");

        while (result.next()) {
            writer.write(
                    result.getString("Name") + " " +
                            result.getString("Number") + "\n");
        }
        writer.flush();
    }

    /**
     * Prints usage and keys.
     */
    private static void printHelp() {
        System.out.print(
                "Usage:\tDBClient [BASE NAME] [KEY]..." +
                "Keys:\n" +
                "\t-h (--help) get help\n" +
                "\t-o (--output) [FILE] write results in FILE\n" +
                "\t-i (--input) [FILE] get commands from FILE\n");
        printCommands();
    }

    /**
     * Prints list of commands.
     */
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