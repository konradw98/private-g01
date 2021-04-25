package pt.isel.ls;

import org.junit.Before;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;

public class Phase1Tests {

    private Router router;
    private PGSimpleDataSource dataSource;

    @Before
    public void init() {
        router = new Router();
        router.addHandlers();

        dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");
    }


    @Test
    public void wrongPathTest() {
        Path path = new Path("users/1");
        Method method = Method.GET;
        Optional<RouteResult> optional = router.findRoute(method, path);
        assertEquals(Optional.empty(), optional);
    }

    @Test
    public void wrongPathParamsTest() throws SQLException {
        Path path = new Path("/routes");
        Method method = Method.POST;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        ArrayList<String> parameters = new ArrayList<>(Arrays.asList(("start_location=Wroclaw&end_location=Warszawa&distance=-1").split("&")));
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(), parameters, dataSource);
        Optional<CommandResult> optionalCommandResult = routeResult.getHandler().execute(commandRequest);
        assertEquals(Optional.empty(), optionalCommandResult);
    }

    @Test(expected = SQLException.class)
    public void exceptionThrowableTest() throws SQLException {
        PGSimpleDataSource testDataSource = new PGSimpleDataSource();
        testDataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        testDataSource.setPassword("wrong_password");
        testDataSource.setUser("postgres");

        Path path = new Path("/users");
        Method method = Method.GET;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(), testDataSource);
        routeResult.getHandler().execute(commandRequest);
    }

    @Test
    public void postUserTest() throws SQLException {
        Path path = new Path("/users");
        Method method = Method.POST;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        ArrayList<String> parameters1 = new ArrayList<>(Arrays.asList(("name=Tom+Rock&email=tom@gmail.com").split("&")));
        CommandRequest commandRequest1 = new CommandRequest(routeResult.getPathParameters(), parameters1, dataSource);
        Optional<CommandResult> optionalCommandResult1 = routeResult.getHandler().execute(commandRequest1);
        CommandResult commandResult1 = optionalCommandResult1.get();
        int numberOfUsersAfterFirstPost = -1;
        if (commandResult1.getResultSet().next()) numberOfUsersAfterFirstPost = commandResult1.getResultSet().getInt(1);
        //System.out.println(numberOfUsersAfterFirstPost);

        ArrayList<String> parameters2 = new ArrayList<>(Arrays.asList(("name=Kat+Rock&email=kat@gmail.com").split("&")));
        CommandRequest commandRequest2 = new CommandRequest(routeResult.getPathParameters(), parameters2, dataSource);
        Optional<CommandResult> optionalCommandResult2 = routeResult.getHandler().execute(commandRequest2);
        CommandResult commandResult2 = optionalCommandResult2.get();
        int numberOfUsersAfterSecondPost = -1;
        if (commandResult2.getResultSet().next())
            numberOfUsersAfterSecondPost = commandResult2.getResultSet().getInt(1);
        //System.out.println(numberOfUsersAfterSecondPost);

        assertEquals(numberOfUsersAfterFirstPost + 1, numberOfUsersAfterSecondPost);
    }

    @Test
    public void getUserTest() throws SQLException{
        String expectedResult = "First User user@gmail.com";
        Path path = new Path("/users/1");
        Method method = Method.GET;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(), dataSource);
        Optional<CommandResult> optionalCommandResult = routeResult.getHandler().execute(commandRequest);
        CommandResult commandResult = optionalCommandResult.get();
        String firstUser="";
        while (commandResult.getResultSet().next()) {
            firstUser = (commandResult.getResultSet().getString("name") + " " +
                    commandResult.getResultSet().getString("email"));
        }
        System.out.println(firstUser);

        assertEquals(expectedResult, firstUser);

    }


}
