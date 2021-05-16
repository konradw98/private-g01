package pt.isel.ls;

import org.junit.*;
import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.util.PSQLException;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.handlers.OptionHandler;
import pt.isel.ls.handlers.PostRouteHandler;
import pt.isel.ls.handlers.PostUserHandler;
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

        PathParameters pathParameters = new PathParameters();
        RouteResult routeResult = new RouteResult(new GetUsersHandler(), pathParameters);
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
        Parameters parameters = new Parameters("nam=John email=mail");
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(), parameters,
                dataSource);
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

}
