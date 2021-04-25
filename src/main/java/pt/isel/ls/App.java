package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.Handlers.*;

import javax.sound.midi.Soundbank;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");

        Router router = new Router();
        router.addHandlers();

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        while (!line.equals("EXIT")) {
            InteractiveMode.run(line, router, dataSource);
            line = scanner.nextLine();
        }
    }
}
