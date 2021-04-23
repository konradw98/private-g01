package pt.isel.ls;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) throws SQLException {

        Router router = new Router();
        Path path = new Path("/users/1");
        router.addHandlers();

        RouteResult routeResult = router.findRoute(Method.GET, path).get();
        CommandResult commandResult = routeResult.getHandler().execute(new CommandRequest(routeResult.getParameters()));
        while (commandResult.getResultSet().next()) {
            System.out.println(commandResult.getResultSet().getInt("uid") + " " +
                    commandResult.getResultSet().getString("email") + " " +
                    commandResult.getResultSet().getString("name"));
        }

        path = new Path("/sports");
        String pobrany = "name=basketball&description=shooting+a+basketball+through+the+defenders+hoop";
        List<String> parametersList = Arrays.asList(pobrany.split("&"));
        ArrayList<String> parameters = new ArrayList<>(parametersList);
        routeResult = router.findRoute(Method.POST, path).get();
        commandResult = routeResult.getHandler().execute(new CommandRequest(routeResult.getParameters(), parameters));

        while (commandResult.getResultSet().next()) {
            System.out.println(commandResult.getResultSet().getInt("sid") + " " +
                    commandResult.getResultSet().getString("name") + " " +
                    commandResult.getResultSet().getString("description"));
        }

        path = new Path("/users");
        router.addHandlers();

        routeResult = router.findRoute(Method.GET, path).get();
        commandResult = routeResult.getHandler().execute(new CommandRequest(routeResult.getParameters()));
        while (commandResult.getResultSet().next()) {
            System.out.println(commandResult.getResultSet().getInt("uid") + " " +
                    commandResult.getResultSet().getString("email") + " " +
                    commandResult.getResultSet().getString("name"));
        }





        /*while (commandResult.getResultSet().next()) {
            System.out.println(commandResult.getResultSet().getInt("rid") + " " +
                    commandResult.getResultSet().getString("start_location") + " " +
                    commandResult.getResultSet().getString("end_location") + " " +
                    commandResult.getResultSet().getString("distance"));
        }*/


    }
}
