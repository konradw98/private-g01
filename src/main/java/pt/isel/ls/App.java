package pt.isel.ls;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException {

        Router router = new Router();
        Path path = new Path("/users/1");
        PathTemplate pathTemplate = new PathTemplate("/users/{1}");
        router.addRoute(Method.GET, pathTemplate, new GetUserByIdHandler());

        RouteResult routeResult = router.findRoute(Method.GET, path).get();
        CommandResult commandResult = routeResult.getHandler().execute(new CommandRequest(routeResult.getParameters()));
        while (commandResult.getResultSet().next()) {
            System.out.println(commandResult.getResultSet().getInt("uid") + " " +
                    commandResult.getResultSet().getString("email") + " " +
                    commandResult.getResultSet().getString("name"));
        }


    }
}
