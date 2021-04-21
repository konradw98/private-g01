package pt.isel.ls;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException {

        Router router = new Router();
        Path path = new Path("/users");
        PathTemplate pathTemplate = new PathTemplate("/users");
        router.addRoute(Method.GET, pathTemplate, new GetUsersHandler());

        RouteResult routeResult = router.findRoute(Method.GET, path).get();
        CommandResult commandResult = routeResult.getHandler().execute(new CommandRequest());
        while (commandResult.getResultSet().next()) {
            System.out.println(commandResult.getResultSet().getInt("uid") + " " +
                    commandResult.getResultSet().getString("email") + " " +
                    commandResult.getResultSet().getString("name"));
        }
    }
}
