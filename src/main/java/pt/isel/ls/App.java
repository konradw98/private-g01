package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        boolean exit = false;


        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(System.getenv("JDBC_DATABASE_URL"));

        CommandExecutor commandExecutor = new CommandExecutor();

        if (args.length == 0) {
            Scanner scanner = new Scanner(System.in);
            while (!exit) {
                String line = scanner.nextLine();
                exit = commandExecutor.runCommand(line, dataSource);
            }
        } else {
            commandExecutor.runCommand(args[0], dataSource);
        }
    }
}
