package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;

import java.util.Scanner;

public class App {
    //TODO: GET /users/a i w activities time i date validation
    //TODO: think about how to differ empty db and wrong parameters
    //TODO: validateParameters with top and skip is the same
    public static void main(String[] args) {
        boolean exit = false;

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");

        Router router = new Router();
        router.addHandlers();

        if (args.length == 0) {
            Scanner scanner = new Scanner(System.in);
            while (!exit) {
                String line = scanner.nextLine();
                exit = CommandExecutor.runCommand(line, router, dataSource);
            }
        } else {
            while (!exit) {
                exit = CommandExecutor.runCommand(args, router, dataSource);
            }
        }


    }
}
