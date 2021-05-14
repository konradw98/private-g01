package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.commandresults.CommandResult;

import java.sql.SQLException;
import java.util.Optional;

public class CommandExecutor {
    private static final int SIMPLEST_COMMAND_SEGMENTS = 2;
    private static final int COMMAND_WITHOUT_HEADERS_OR_WITH_PARAMETERS_ONLY = 3;
    private static final int COMMAND_WITH_HEADERS = 4;

    private static final int METHOD_INDEX = 0;
    private static final int PATH_INDEX = 1;
    private static final int PARAMETERS_INDEX_WITHOUT_HEADERS_OR_HEADERS_INDEX = 2;
    private static final int PARAMETERS_INDEX_WITH_HEADERS = 3;
    private static final int HEADERS_INDEX = 2;

    public static boolean runCommand(String line, Router router, PGSimpleDataSource dataSource) {
        return executeCommand(line.split("\\s+"), line, router, dataSource);
    }

    public static boolean runCommand(String[] command, Router router, PGSimpleDataSource dataSource) {
        return executeCommand(command, String.join(" ", command), router, dataSource);
    }

    private static boolean executeCommand(String[] command, String line, Router router, PGSimpleDataSource dataSource) {
        boolean exit = false;
        if (command.length >= SIMPLEST_COMMAND_SEGMENTS && command.length <= COMMAND_WITH_HEADERS) {
            Method method = validateMethod(command[METHOD_INDEX]);
            if (!method.equals(Method.WRONG_METHOD)) {
                Optional<RouteResult> optional = router.findRoute(method, new Path(command[PATH_INDEX]));
                if (optional.isPresent()) {
                    exit = executeProperCommand(optional.get(), command, dataSource);
                } else {
                    System.out.println("Wrong command: " + line);
                }
            } else {
                System.out.println("Wrong command: " + line);
            }
        } else {
            System.out.println("Wrong command: " + line);
        }
        return exit;
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
            case "DELETE" -> {
                return Method.DELETE;
            }
            case "EXIT" -> {
                return Method.EXIT;
            }
            default -> {
                return Method.WRONG_METHOD;
            }
        }
    }

    private static boolean executeProperCommand(RouteResult routeResult, String[] command,
                                                PGSimpleDataSource dataSource) {
        boolean exit = false;
        CommandRequest commandRequest = chooseRequest(command, routeResult, dataSource);

        try {
            CommandResult commandResult = routeResult.getHandler().execute(commandRequest);
            exit = commandResult.results();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exit;
    }

    private static CommandRequest chooseRequest(String[] command, RouteResult routeResult,
                                                PGSimpleDataSource dataSource) {
        CommandRequest commandRequest;
        if (command.length == SIMPLEST_COMMAND_SEGMENTS) {
            commandRequest = new CommandRequest(routeResult.getPathParameters(),
                    dataSource);
        } else if (command.length == COMMAND_WITHOUT_HEADERS_OR_WITH_PARAMETERS_ONLY) {
            if (Headers.ifIsHeader(command[PARAMETERS_INDEX_WITHOUT_HEADERS_OR_HEADERS_INDEX])) {
                Headers headers = new Headers(command[PARAMETERS_INDEX_WITHOUT_HEADERS_OR_HEADERS_INDEX]);
                commandRequest = new CommandRequest(routeResult.getPathParameters(), headers,
                        dataSource);
            } else {
                Parameters parameters = new Parameters(command[PARAMETERS_INDEX_WITHOUT_HEADERS_OR_HEADERS_INDEX]);
                commandRequest = new CommandRequest(routeResult.getPathParameters(), parameters,
                        dataSource);
            }
        } else {
            Headers headers = new Headers(command[HEADERS_INDEX]);
            Parameters parameters = new Parameters(command[PARAMETERS_INDEX_WITH_HEADERS]);
            commandRequest = new CommandRequest(routeResult.getPathParameters(),
                    parameters, headers, dataSource);
        }
        return commandRequest;
    }
}
