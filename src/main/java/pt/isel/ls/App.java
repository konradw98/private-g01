package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        boolean exit = false;

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test1");
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
            CommandExecutor.runCommand(args[0], router, dataSource);
        }


    }
}
