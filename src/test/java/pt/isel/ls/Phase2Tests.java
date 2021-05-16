package pt.isel.ls;

import org.junit.*;
import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.util.PSQLException;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.handlers.OptionHandler;
import pt.isel.ls.handlers.PostRouteHandler;
import pt.isel.ls.handlers.PostUserHandler;
import pt.isel.ls.handlers.get.gettables.GetTopsActivitiesHandler;
import pt.isel.ls.handlers.get.gettables.GetUsersHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.instanceOf;

public class Phase2Tests {

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
    public void wrongPathNoSlashTest() {
        Path path = new Path("users/1");
        Method method = Method.GET;
        Optional<RouteResult> optional = router.findRoute(method, path);
        assertEquals(Optional.empty(), optional);
    }

    @Test(expected = SQLException.class)
    public void exceptionThrowableTest() throws SQLException {
        PGSimpleDataSource testDataSource = new PGSimpleDataSource();
        testDataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        testDataSource.setPassword("wrong_password");
        testDataSource.setUser("postgres");

        Path correctPath = new Path("/sports/1");
        Method method = Method.GET;
        Optional<RouteResult> optional = router.findRoute(method, correctPath);
        RouteResult routeResult = optional.get();
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(), testDataSource);
        routeResult.getHandler().execute(commandRequest);
    }

    @Test
    public void wrongPathParamsNegativeDistanceTest() throws SQLException {
        PathParameters pathParameters = new PathParameters();
        RouteResult routeResult = new RouteResult(new PostRouteHandler(), pathParameters);
        Parameters parameters = new Parameters("startLocation=Wroclaw endLocation=Warszawa distance=-1.0");
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(), parameters,
                dataSource);
        CommandResult commandResult = routeResult.getHandler().execute(commandRequest);
        assertThat(commandResult, instanceOf(WrongParametersResult.class));
    }

    @Test
    public void wrongPathParamsTypoInNameTest() throws SQLException {
        PathParameters pathParameters = new PathParameters();
        RouteResult routeResult = new RouteResult(new PostUserHandler(), pathParameters);
        Parameters parameters = new Parameters("nam=John&email=mail");
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(), parameters,
                dataSource);
        CommandResult commandResult = routeResult.getHandler().execute(commandRequest);
        assertThat(commandResult, instanceOf(WrongParametersResult.class));
    }


    @Test
    public void wrongParamFormatTest() throws SQLException {
        PathParameters pathParameters = new PathParameters();
        RouteResult routeResult = new RouteResult(new GetTopsActivitiesHandler(), pathParameters);
        Parameters parametersWithWrongDate = new Parameters("uid=1&duration=00:10:30&date=2021:04:21&rid=1");
        Parameters parametersWithWrongRidFormat = new Parameters("uid=1000&duration=00:10:30&rid=a");
        Parameters parametersWithWrongDurationFormat = new Parameters("uid=1&duration=00-10-30");
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(), parametersWithWrongDate,
                dataSource);
        CommandResult commandResult = routeResult.getHandler().execute(commandRequest);
        assertThat(commandResult, instanceOf(WrongParametersResult.class));

        commandRequest = new CommandRequest(routeResult.getPathParameters(), parametersWithWrongRidFormat,
                dataSource);
        commandResult = routeResult.getHandler().execute(commandRequest);
        assertThat(commandResult, instanceOf(WrongParametersResult.class));

        commandRequest = new CommandRequest(routeResult.getPathParameters(), parametersWithWrongDurationFormat,
                dataSource);
        commandResult = routeResult.getHandler().execute(commandRequest);
        assertThat(commandResult, instanceOf(WrongParametersResult.class));

    }

    @Test
    public void wrongRegexTest() throws SQLException {
        Path correctPath = new Path("/sports/1");
        Method method = Method.GET;
        Optional<RouteResult> optional = router.findRoute(method, correctPath);
        RouteResult routeResult = optional.get();
        Parameters parameters = new Parameters("skip=1,top=3");
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(), parameters,
                dataSource);
        CommandResult commandResult = routeResult.getHandler().execute(commandRequest);
        assertThat(commandResult, instanceOf(WrongParametersResult.class));
    }

    @Test
    public void notEnoughParametersTest() throws SQLException {
        Path path = new Path("/users");
        Method method = Method.GET;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(), dataSource);
        CommandResult commandResult = routeResult.getHandler().execute(commandRequest);
        assertThat(commandResult, instanceOf(WrongParametersResult.class));
    }

    @Test
    public void wrongFileExtensionFormatTest() throws SQLException {
        PathParameters pathParameters = new PathParameters();
        RouteResult routeResult = new RouteResult(new GetUsersHandler(), pathParameters);
        Parameters parameters = new Parameters("skip=1&top=3");
        Headers headers = new Headers("file-name:users.tx");
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(),
                parameters, headers, dataSource);
        CommandResult commandResult = routeResult.getHandler().execute(commandRequest);
        assertThat(commandResult, instanceOf(WrongParametersResult.class));
    }


    @Test
    public void getUsersPlainTest() {

        CommandExecutor.runCommand("GET /users/ accept:text/plain|file-name:src/test/files/usersPlain.txt skip=0&top=1", router, dataSource);
        String data = "";
        try {
            File myObj = new File("src/test/files/usersPlain.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                System.out.println(data);

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String excepted = "id: 3 email: email1 name: imie";
        assertEquals(excepted, data);

    }

    @Test
    public void getSportsPlainTest() {

        CommandExecutor.runCommand("GET /sports/ accept:text/plain|file-name:src/test/files/sportsPlain.txt skip=0&top=3", router, dataSource);
        String data = "";
        try {
            File myObj = new File("src/test/files/sportsPlain.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                System.out.println(data);

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String excepted = "id: 1 name: sport1 description: opisportu1id: 2 name: " +
                "sport2 description: opisportu2id: 3 name: sport3 description: opisportu3";
        assertEquals(excepted, data);

    }

    @Test
    public void getRoutesPlainTest() {

        CommandExecutor.runCommand("GET /routes/ accept:text/plain|file-name:src/test/files/routesPlain.txt skip=0&top=3",
                router, dataSource);
        String data = "";
        try {
            File myObj = new File("src/test/files/routesPlain.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                data = myReader.nextLine();

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String excepted = " rid: 1 start location: starlocaetiont1 end location: endlocation1 rid: 2 " +
                "start location: starlocaetiont2 end location: endlocation2 rid: 3" +
                " start location: starlocaetiont3 end location: endlocation3";
        assertEquals(excepted, data);

    }

    @Test
    public void getSportsByIdJsonTest() {

        CommandExecutor.runCommand("GET /sports/1 accept:application/json|file-name:src/test/files/sportsJson.json", router, dataSource);
        String data = "";
        try {
            File myObj = new File("src/test/files/sportsJson.json");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = data + myReader.nextLine();


            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String excepted = "{  \"id\": 1, \"name\": sport1, \"description\":opisportu1,}";

        assertEquals(excepted, data);

    }

    @Test
    public void getRoutesByIdPlainTest() {

        CommandExecutor.runCommand("GET /routes/2 accept:text/plain|file-name:src/test/files/routesPlain.txt", router, dataSource);
        String data = "";
        try {
            File myObj = new File("src/test/files/routesPlain.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                data = data + myReader.nextLine();


            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String excepted = " rid: 2 start location: starlocaetiont2 end location: endlocation2";

        assertEquals(excepted, data);

    }

    @Test
    public void getUsersByIdJsonTest() {

        CommandExecutor.runCommand("GET /users/3 accept:application/json|file-name:src/test/files/usersJson.json", router, dataSource);
        String data = "";
        try {
            File myObj = new File("src/test/files/usersJson.json");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                data = data + myReader.nextLine();


            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String excepted = "{  \"id\": 3, \"name\": imie, \"email\":email1,}";

        assertEquals(excepted, data);

    }

}
