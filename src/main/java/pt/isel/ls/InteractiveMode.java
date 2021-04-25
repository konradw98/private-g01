package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.Handlers.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class InteractiveMode {
    private static final int GET_COMMAND_PARAMETERS = 2;
    private static final int POST_COMMAND_PARAMETERS = 3;
    private static final int METHOD_INDEX = 0;
    private static final int PATH_INDEX = 1;
    private static final int PARAMETERS_INDEX = 2;

    public static void run(String line, Router router, PGSimpleDataSource dataSource) {
        Method method = null;
        String[] command = line.split(" ");

        if (command.length > 1) {
            switch (command[METHOD_INDEX]) {
                case "GET" -> method = Method.GET;
                case "POST" -> method = Method.POST;
                default -> System.out.println("Wrong value: " + command[METHOD_INDEX]);
            }

            if (method != null) {
                Optional<RouteResult> optional = router.findRoute(method, new Path(command[PATH_INDEX]));
                if (optional.isPresent()) {
                    RouteResult routeResult = optional.get();
                    CommandRequest commandRequest;

                    if (command.length == GET_COMMAND_PARAMETERS) {
                        commandRequest = new CommandRequest(routeResult.getPathParameters(), dataSource);
                    } else if (command.length == POST_COMMAND_PARAMETERS) {
                        ArrayList<String> parameters = new ArrayList<>(Arrays.asList(command[PARAMETERS_INDEX].split("&")));
                        commandRequest = new CommandRequest(routeResult.getPathParameters(), parameters, dataSource);
                    } else {
                        System.out.println("Wrong command: " + line);
                        return;
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
            }
        } else System.out.println("Wrong command: " + line);
    }

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
