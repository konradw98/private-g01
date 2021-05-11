package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.commandresults.CommandResult;
import java.sql.SQLException;
import java.util.Optional;

public class CommandExecutor {
    private static final int COMMAND_WITHOUT_HEADERS = 2;
    private static final int COMMAND_WIT_HEADERS = 3;
    private static final int METHOD_INDEX = 0;
    private static final int PATH_INDEX = 1;
    private static final int PARAMETERS_INDEX = 2;

    public static void runCommand(String line, Router router, PGSimpleDataSource dataSource) {
        executeCommand(line.split(" "), line, router, dataSource);
    }

    public static void runCommand(String[] command, Router router, PGSimpleDataSource dataSource) {
        executeCommand(command, String.join(" ", command), router, dataSource);
    }

    private static void executeCommand(String[] command, String line, Router router, PGSimpleDataSource dataSource) {
        if (command.length == COMMAND_WITHOUT_HEADERS || command.length == COMMAND_WIT_HEADERS) {
            Method method = validateMethod(command[METHOD_INDEX]);
            if (!method.equals(Method.WRONG_METHOD)) {
                Optional<RouteResult> optional = router.findRoute(method, new Path(command[PATH_INDEX]));
                if (optional.isPresent()) {
                    executeProperCommand(optional.get(), command, dataSource);
                } else {
                    System.out.println("Wrong command: " + line);
                }
            } else {
                System.out.println("Wrong command: " + line);
            }
        } else {
            System.out.println("Wrong command: " + line);
        }
    }

    private static Method validateMethod(String command) {
        switch (command) {
            case "GET" -> {
                return Method.GET;
            }
            case "POST" -> {
                return Method.POST;
            }
            case "OPTION" -> {
                return Method.OPTION;
            }
            case "EXIT" -> {
                return Method.EXIT;
            }
            default -> {
                return Method.WRONG_METHOD;
            }
        }
    }

    private static void executeProperCommand(RouteResult routeResult, String[] command, PGSimpleDataSource dataSource) {
        CommandRequest commandRequest;
       // Parameters parameters = new Parameters(command[PARAMETERS_INDEX]);

        //TODO: refactor
        if (command.length == COMMAND_WITHOUT_HEADERS) {
            commandRequest = new CommandRequest(routeResult.getPathParameters(),
                    dataSource);
        } else {
            Headers headers = new Headers();
            Parameters parameters = new Parameters(command[PARAMETERS_INDEX]);
            commandRequest = new CommandRequest(routeResult.getPathParameters(),
                    parameters, headers, dataSource);
        }

        try {
            CommandResult commandResult = routeResult.getHandler().execute(commandRequest);
            commandResult.print();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
