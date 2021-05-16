package pt.isel.ls;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.*;
import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.PostResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.handlers.DeleteHandler;
import pt.isel.ls.handlers.PostRouteHandler;
import pt.isel.ls.handlers.PostUserHandler;
import pt.isel.ls.handlers.get.gettables.GetTopsActivitiesHandler;
import pt.isel.ls.handlers.get.gettables.GetUsersHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
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
    public void wrongActivitiesParametersTest() throws SQLException {
        PathParameters pathParameters = new PathParameters();
        RouteResult routeResult = new RouteResult(new DeleteHandler(), pathParameters);
        Parameters parameters = new Parameters("activity=2&activity=-1");
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(),
                parameters, dataSource);
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
        String excepted = "id: 1 email: user@gmail.com name: First User";
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
        String excepted = "id: 1 name: football description: kicking a ball to score a goalid: 2" +
                " name: volleyball description: score points by grounding a ball on the other teams courtid:" +
                " 3 name: basketball description: shooting a basketball through the defenders hoop";
        assertEquals(excepted, data);

    }

    @Test
    public void getRoutesPlainTest() {

        CommandExecutor.runCommand("GET /routes/ accept:text/plain|file-name:src/test/files/routesPlain1.txt skip=0&top=3",
                router, dataSource);
        String data = "";
        try {
            File myObj = new File("src/test/files/routesPlain1.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                data = myReader.nextLine();

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String excepted = " rid: 1 start location: Bairro Alto end location: Alameda rid: 2 start location: Poland" +
                " end location: Portugal rid: 3 start location: Wroclaw end location: Lisbon";
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
        String excepted = "{  \"id\": 1, \"name\": football, \"description\":kicking a ball to score a goal,}";

        assertEquals(excepted, data);

    }

    @Test
    public void getRoutesByIdPlainTest() {

        CommandExecutor.runCommand("GET /routes/2 accept:text/plain|file-name:src/test/files/routesPlain2.txt", router, dataSource);
        String data = "";
        try {
            File myObj = new File("src/test/files/routesPlain2.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                data = data + myReader.nextLine();


            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String excepted = " rid: 2 start location: Poland end location: Portugal";

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
        String excepted = "{  \"id\": 3, \"name\": ZnKal, \"email\":ZnKal@gmail.com,}";

        assertEquals(excepted, data);

    }

    @Test
    public void getActivitiesPlainTest() {

        CommandExecutor.runCommand("GET /tops/activities  " +
                "accept:text/plain|file-name:src/test/files/activitiesPlain.txt" +
                " sid=3&orderBy=desc&rid=2&distance=1&skip=0&top=3", router, dataSource);
        String data = "";
        try {
            File myObj = new File("src/test/files/activitiesPlain.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                data = data + myReader.nextLine();


            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String excepted = "id: 5 date: 2002-02-02 duration time: 02:02:02 sport id: 3 user id: 2 route id: 2id:" +
                " 8 date: 2002-02-02 duration time: 02:02:02 sport id: 3 user id: 2 route id: 2id: 11 date: 2002-02-02" +
                " duration time: 02:02:02 sport id: 3 user id: 2 route id: 2";

        assertEquals(excepted, data);

    }

    @Test()
    public void postUserTest() throws SQLException {
        String name1 = RandomStringUtils.random(5, true, false);
        String email1 = name1 + "@gmail.com";
        Path path = new Path("/users");
        Method method = Method.POST;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        Parameters parameters1 = new Parameters("name=" + name1 + "&email=" + email1);
        CommandRequest commandRequest1 = new CommandRequest(routeResult.getPathParameters(),
                parameters1, dataSource);
        PostResult commandResult1 = (PostResult) routeResult.getHandler().execute(commandRequest1);
        int numberOfUsersAfterFirstPost = commandResult1.getId();

        String name2 = RandomStringUtils.random(5, true, false);
        String email2 = name2 + "@gmail.com";
        Parameters parameters2 = new Parameters("name=" + name2 + "&email=" + email2);
        CommandRequest commandRequest2 = new CommandRequest(routeResult.getPathParameters(),
                parameters2, dataSource);
        PostResult commandResult2 = (PostResult) routeResult.getHandler().execute(commandRequest2);
        int numberOfUsersAfterSecondPost = commandResult2.getId();

        assertEquals(numberOfUsersAfterFirstPost + 1, numberOfUsersAfterSecondPost);
    }

    @Test
    public void postRouteTest() throws SQLException {
        Path path = new Path("/routes");
        Method method = Method.POST;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        Parameters parameters1 = new Parameters("distance=3051.0&startLocation=Poland&endLocation=Portugal");
        CommandRequest commandRequest1 = new CommandRequest(routeResult.getPathParameters(), parameters1, dataSource);
        PostResult commandResult1 = (PostResult) routeResult.getHandler().execute(commandRequest1);
        int numberOfRoutesAfterFirstPost = commandResult1.getId();

        Parameters parameters2 = new Parameters("distance=3014.3&startLocation=Wroclaw&endLocation=Lisbon");
        CommandRequest commandRequest2 = new CommandRequest(routeResult.getPathParameters(), parameters2, dataSource);
        PostResult commandResult2 = (PostResult) routeResult.getHandler().execute(commandRequest2);
        int numberOfRoutesAfterSecondPost = commandResult2.getId();

        assertEquals(numberOfRoutesAfterFirstPost + 1, numberOfRoutesAfterSecondPost);
    }


    @Test
    public void postSportWithRandomOrderedParamsTest() throws SQLException {
        Path path = new Path("/sports");
        Method method = Method.POST;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        Parameters parameters1 = new Parameters("description=travelling+over+snow+on+ski&name=skiing");
        CommandRequest commandRequest1 = new CommandRequest(routeResult.getPathParameters(), parameters1, dataSource);
        PostResult commandResult1 = (PostResult) routeResult.getHandler().execute(commandRequest1);
        int numberOfSportsAfterFirstPost = commandResult1.getId();

        Parameters parameters2 = new Parameters("description=sliding+downhill+on+a+snowboard&name=snowboarding");
        CommandRequest commandRequest2 = new CommandRequest(routeResult.getPathParameters(), parameters2, dataSource);
        PostResult commandResult2 = (PostResult) routeResult.getHandler().execute(commandRequest2);
        int numberOfSportsAfterSecondPost = commandResult2.getId();

        assertEquals(numberOfSportsAfterFirstPost + 1, numberOfSportsAfterSecondPost);
    }

    @Test
    public void postActivityTest() throws SQLException {

        Path path = new Path("/sports/3/activities");
        Method method = Method.POST;
        Parameters parameters1 = new Parameters("uid=1&duration=01:01:01&date=2001-01-01&rid=1");
        Parameters parameters2 = new Parameters("uid=2&duration=02:02:02&date=2002-02-02&rid=1");

        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();

        CommandRequest commandRequest1 = new CommandRequest(routeResult.getPathParameters(), parameters1, dataSource);
        PostResult commandResult1 = (PostResult) routeResult.getHandler().execute(commandRequest1);
        int numberOfActivitiesAfterFirstPost = commandResult1.getId();


        CommandRequest commandRequest2 = new CommandRequest(routeResult.getPathParameters(), parameters2, dataSource);
        PostResult commandResult2 = (PostResult) routeResult.getHandler().execute(commandRequest2);
        int numberOfActivitiesSecondFirstPost = commandResult2.getId();

        assertEquals(numberOfActivitiesAfterFirstPost + 1, numberOfActivitiesSecondFirstPost);
    }


}
