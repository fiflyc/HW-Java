package com.example.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import org.jetbrains.annotations.NotNull;

import java.io.*;

class DBClient {

    public int main(String[] args) {
        BufferedOutputStream out = null;

        if (args.length > 0) {
            if (args[0].equals("-h") || args[0].equals("--help")) {
                System.out.print("This is the phone database client.\n\n");
                printHelp();
                return 0;
            } else if (args[0].equals("-o") || args[0].equals("--output")) {
                if (args.length == 1) {
                    System.out.print("You have not wrote filename!\n\n");
                    printHelp();
                    return 0;
                } else {
                    try {
                        out = new BufferedOutputStream (new FileOutputStream(args[1]);
                    } catch (FileNotFoundException e) {
                        System.out.print("File \"" + args[1] + "\" not found.\n");
                        return 0;
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
            System.out.print("Error with writing result.");
        } catch (SQLException e) {
            System.out.print("The database does not exists.");
        }

        return 0;
    }


    public void run(@NotNull InputStream in, @NotNull OutputStream out, @NotNull String dbName) throws IOException, SQLException {
        
    }

    private static void printHelp() {
        System.out.print(
                "Options:\n\t-h (--help) get help\n\t-o (--output) [FILE] write results in FILE\n" +
                "Usage:\n\t" +
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