package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.commandresults.CommandResult;
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
                case "EXIT" -> method = Method.EXIT;
                default -> System.out.println("Wrong value: " + command[METHOD_INDEX]);
            }

            if (method != null) {
                Optional<RouteResult> optional = router.findRoute(method, new Path(command[PATH_INDEX]));
                if (optional.isPresent()) {
                    RouteResult routeResult = optional.get();
                    CommandRequest commandRequest;

                    if (command.length == GET_COMMAND_PARAMETERS) {
                        commandRequest = new CommandRequest(new ArrayList<>(routeResult.getPathParameters().values()),
                                dataSource);
                    } else if (command.length == POST_COMMAND_PARAMETERS) {
                        ArrayList<String> parameters = new ArrayList<>(Arrays.asList(command[PARAMETERS_INDEX]
                                .split("&")));
                        commandRequest = new CommandRequest(new ArrayList<>(routeResult.getPathParameters().values()),
                                parameters, dataSource);
                    } else {
                        System.out.println("Wrong command: " + line);
                        return;
                    }

                    try {
                        CommandResult commandResult = routeResult.getHandler().execute(commandRequest);
                        commandResult.print();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Wrong command: " + line);
                }
            }
        } else {
            System.out.println("Wrong command: " + line);
        }
    }

}
