package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.Handlers.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws SQLException {

        Router router = new Router();
        router.addHandlers();

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] command;
        Method method;

        while (!line.equals("EXIT")) {
            command = line.split(" ");
            method = switch (command[0]) {
                case "GET" -> Method.GET;
                case "POST" -> Method.POST;
                case "EXIT" -> Method.EXIT;
                default -> throw new IllegalStateException("Unexpected value: " + command[0]);
            };

            Optional<RouteResult> optional = router.findRoute(method, new Path(command[1]));
            if (optional.isPresent()) {

                RouteResult routeResult = optional.get();
                CommandRequest commandRequest;
                if (command.length == 2) {
                    commandRequest = new CommandRequest(routeResult.getPathParameters(), dataSource);
                } else if (command.length == 3) {
                    ArrayList<String> parameters = new ArrayList<>(Arrays.asList(command[2].split("&")));
                    commandRequest = new CommandRequest(routeResult.getPathParameters(), parameters, dataSource);
                } else {
                    System.out.println("Wrong command: " + line);
                    break;
                }
                try {
                    Optional<CommandResult> optionalCommandResult = routeResult.getHandler().execute(commandRequest);
                    if (optionalCommandResult.isPresent()) {
                        CommandResult commandResult = optionalCommandResult.get();
                        printResult(routeResult, commandResult);
                    } else System.out.println("Wrong command: " + line);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else System.out.println("Wrong command: " + line);
            //TODO: should we throw an exception or try again

            line = scanner.nextLine();
        }
    }

    //TODO: refactor, another idea
    public static void printResult(RouteResult routeResult, CommandResult commandResult) throws SQLException {
        if (routeResult.getHandler() instanceof GetUsersHandler || routeResult.getHandler() instanceof GetUserByIdHandler) {
            while (commandResult.getResultSet().next()) {
                System.out.println("name: " + commandResult.getResultSet().getString("name") + " " +
                        " - email: " + commandResult.getResultSet().getString("email"));
            }
        } else if (routeResult.getHandler() instanceof GetRoutesHandler || routeResult.getHandler() instanceof GetRouteByIDHandler) {
            while (commandResult.getResultSet().next()) {
                System.out.println("start location: " + commandResult.getResultSet().getString("start_location") + " " +
                        " - end location: " + commandResult.getResultSet().getString("end_location") + " " +
                        " - distance: " + commandResult.getResultSet().getString("distance"));
            }
        } else if (routeResult.getHandler() instanceof GetSportHandler || routeResult.getHandler() instanceof GetSportByIdHandler) {
            while (commandResult.getResultSet().next()) {
                System.out.println("name: " + commandResult.getResultSet().getString("name") + " " +
                        " - description: " + commandResult.getResultSet().getString("description"));
            }
        } else if (routeResult.getHandler() instanceof GetSportActivitiesHandler || routeResult.getHandler() instanceof GetSportActivitiesByIdHandler
                || routeResult.getHandler() instanceof GetUserActivitiesHandler || routeResult.getHandler() instanceof GetUserActivitiesByIdHandler
                || routeResult.getHandler() instanceof GetTopsActivitiesHandler) {
            while (commandResult.getResultSet().next()) {
                System.out.println("date: " + commandResult.getResultSet().getString("date") + " " +
                        " - duration time: " + commandResult.getResultSet().getString("duration_time") + " " +
                        " - sport ID: " + commandResult.getResultSet().getString("sid") + " " +
                        " - user ID: " + commandResult.getResultSet().getString("uid") + " " +
                        " - route ID: " + commandResult.getResultSet().getString("rid"));
            }
        } else if (routeResult.getHandler() instanceof PostActivityHandler || routeResult.getHandler() instanceof PostSportHandler
                || routeResult.getHandler() instanceof PostUserHandler || routeResult.getHandler() instanceof PostRouteHandler) {
            if (commandResult.getResultSet().next()) {
                System.out.println("ID: " + commandResult.getResultSet().getInt(1));
            }
        }
    }
}
