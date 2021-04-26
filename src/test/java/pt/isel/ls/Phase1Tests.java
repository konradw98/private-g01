package pt.isel.ls;

import org.junit.Before;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    public void wrongPathNoSlashTest() {
        Path path = new Path("users/1");
        Method method = Method.GET;
        Optional<RouteResult> optional = router.findRoute(method, path);
        assertEquals(Optional.empty(), optional);
    }

    @Test
    public void wrongPathParamsNegativeDistanceTest() throws SQLException {
        Path path = new Path("/routes");
        Method method = Method.POST;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        ArrayList<String> parameters = new ArrayList<>(Arrays.asList(("startLocation=Wroclaw&endLocation=Warszawa&distance=-1.0").split("&")));
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(), parameters, dataSource);
        Optional<CommandResult> optionalCommandResult = routeResult.getHandler().execute(commandRequest);
        assertEquals(Optional.empty(), optionalCommandResult);
    }

    @Test
    public void wrongPathParamsTypoInNameTest() throws SQLException {
        Path path = new Path("/users");
        Method method = Method.POST;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        ArrayList<String> parameters = new ArrayList<>(Arrays.asList(("nam=John&email=mail").split("&")));
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(), parameters, dataSource);
        Optional<CommandResult> optionalCommandResult = routeResult.getHandler().execute(commandRequest);
        assertEquals(Optional.empty(), optionalCommandResult);
    }

    @Test
    public void wrongPathParamsActivitiesIdTest() throws SQLException {

        Path correctPath = new Path("/sports/1/activities");
        Path wrongSidPath = new Path("/sports/1000/activities");
        Method method = Method.POST;
        ArrayList<String> parameters = new ArrayList<>(Arrays.asList(("uid=1&duration=00:10:30&date=2021-04-21&rid=1").split("&")));
        ArrayList<String> wrongUidParameters = new ArrayList<>(Arrays.asList(("uid=1000&duration=00:10:30&date=2021-04-21&rid=1").split("&")));
        ArrayList<String> wrongRidParameters = new ArrayList<>(Arrays.asList(("uid=1&duration=00:10:30&date=2021-04-21&rid=1000").split("&")));

        Optional<RouteResult> optional = router.findRoute(method, correctPath);
        RouteResult routeResult = optional.get();
        CommandRequest wrongUidCommandRequest = new CommandRequest(routeResult.getPathParameters(), wrongUidParameters, dataSource);
        Optional<CommandResult> wrongUidOptionalCommandResult = routeResult.getHandler().execute(wrongUidCommandRequest);
        assertEquals(Optional.empty(), wrongUidOptionalCommandResult);

        CommandRequest wrongRidCommandRequest = new CommandRequest(routeResult.getPathParameters(), wrongRidParameters, dataSource);
        Optional<CommandResult> wrongRidOptionalCommandResult = routeResult.getHandler().execute(wrongRidCommandRequest);
        assertEquals(Optional.empty(), wrongRidOptionalCommandResult);

        Optional<RouteResult> wrongPathOptional = router.findRoute(method, wrongSidPath);
        RouteResult wrongPathRouteResult = wrongPathOptional.get();
        CommandRequest wrongPathCommandRequest = new CommandRequest(wrongPathRouteResult.getPathParameters(), parameters, dataSource);
        Optional<CommandResult> wrongPathOptionalCommandResult = wrongPathRouteResult.getHandler().execute(wrongPathCommandRequest);
        assertEquals(Optional.empty(), wrongPathOptionalCommandResult);
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
    public void postRouteTest() throws SQLException {
        Path path = new Path("/routes");
        Method method = Method.POST;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        ArrayList<String> parameters1 = new ArrayList<>(Arrays.asList(("distance=3051.0&startLocation=Poland&endLocation=Portugal").split("&")));
        CommandRequest commandRequest1 = new CommandRequest(routeResult.getPathParameters(), parameters1, dataSource);
        Optional<CommandResult> optionalCommandResult1 = routeResult.getHandler().execute(commandRequest1);
        CommandResult commandResult1 = optionalCommandResult1.get();
        int numberOfUsersAfterFirstPost = -1;
        if (commandResult1.getResultSet().next()) numberOfUsersAfterFirstPost = commandResult1.getResultSet().getInt(1);

        ArrayList<String> parameters2 = new ArrayList<>(Arrays.asList(("distance=3014.3&startLocation=Wroclaw&endLocation=Lisbon").split("&")));
        CommandRequest commandRequest2 = new CommandRequest(routeResult.getPathParameters(), parameters2, dataSource);
        Optional<CommandResult> optionalCommandResult2 = routeResult.getHandler().execute(commandRequest2);
        CommandResult commandResult2 = optionalCommandResult2.get();
        int numberOfUsersAfterSecondPost = -1;
        if (commandResult2.getResultSet().next())
            numberOfUsersAfterSecondPost = commandResult2.getResultSet().getInt(1);

        assertEquals(numberOfUsersAfterFirstPost + 1, numberOfUsersAfterSecondPost);
    }


    @Test
    public void postSportWithRandomOrderedParamsTest() throws SQLException {
        Path path = new Path("/sports");
        Method method = Method.POST;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        ArrayList<String> parameters1 = new ArrayList<>(Arrays.asList(("description=travelling+over+snow+on+ski&name=skiing").split("&")));
        CommandRequest commandRequest1 = new CommandRequest(routeResult.getPathParameters(), parameters1, dataSource);
        Optional<CommandResult> optionalCommandResult1 = routeResult.getHandler().execute(commandRequest1);
        CommandResult commandResult1 = optionalCommandResult1.get();
        int numberOfUsersAfterFirstPost = -1;
        if (commandResult1.getResultSet().next()) numberOfUsersAfterFirstPost = commandResult1.getResultSet().getInt(1);

        ArrayList<String> parameters2 = new ArrayList<>(Arrays.asList(("description=sliding+downhill+on+a+snowboard&name=snowboarding").split("&")));
        CommandRequest commandRequest2 = new CommandRequest(routeResult.getPathParameters(), parameters2, dataSource);
        Optional<CommandResult> optionalCommandResult2 = routeResult.getHandler().execute(commandRequest2);
        CommandResult commandResult2 = optionalCommandResult2.get();
        int numberOfUsersAfterSecondPost = -1;
        if (commandResult2.getResultSet().next())
            numberOfUsersAfterSecondPost = commandResult2.getResultSet().getInt(1);

        assertEquals(numberOfUsersAfterFirstPost + 1, numberOfUsersAfterSecondPost);
    }

    @Test
    public void postActivityTest() throws SQLException {

        Path path = new Path("/sports/3/activities");
        Method method = Method.POST;
        ArrayList<String> parameters1 = new ArrayList<>(Arrays.asList(("uid=1&duration=01:01:01&date=2001-01-01&rid=1").split("&")));
        ArrayList<String> parameters2 = new ArrayList<>(Arrays.asList(("uid=2&duration=02:02:02&date=2002-02-02&rid=2").split("&")));

        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();

        CommandRequest commandRequest1 = new CommandRequest(routeResult.getPathParameters(), parameters1, dataSource);
        Optional<CommandResult> optionalCommandResult1 = routeResult.getHandler().execute(commandRequest1);
        CommandResult commandResult1 = optionalCommandResult1.get();
        int numberOfUsersAfterFirstPost = -1;
        if (commandResult1.getResultSet().next()) numberOfUsersAfterFirstPost = commandResult1.getResultSet().getInt(1);

        CommandRequest commandRequest2 = new CommandRequest(routeResult.getPathParameters(), parameters2, dataSource);
        Optional<CommandResult> optionalCommandResult2 = routeResult.getHandler().execute(commandRequest2);
        CommandResult commandResult2 = optionalCommandResult2.get();
        int numberOfUsersAfterSecondPost = -1;
        if (commandResult2.getResultSet().next())
            numberOfUsersAfterSecondPost = commandResult2.getResultSet().getInt(1);

        assertEquals(numberOfUsersAfterFirstPost + 1, numberOfUsersAfterSecondPost);
    }

    @Test
    public void getUserTest() throws SQLException {

        //make sure that database has the desired user
        String maxSql = "SELECT MAX(uid) FROM users";
        Connection conn = dataSource.getConnection();
        PreparedStatement maxPreparedStatement = conn.prepareStatement(maxSql);
        int maxUid = 0;
        ResultSet maxResultSet = maxPreparedStatement.executeQuery();
        if (maxResultSet.next()) {
            maxUid = maxResultSet.getInt(1);
        }

        if(maxUid<1) {
            String insertSql = "INSERT INTO users(name, email) VALUES(name='First User', " +
                    "email='user@gmail.com');";
            PreparedStatement insertPreparedStatement = conn.prepareStatement(insertSql);
            insertPreparedStatement.execute();
        }
        else {
            String updateSql = "UPDATE users SET name ='First User', email='user@gmail.com' WHERE uid = 1;";
            PreparedStatement updatePreparedStatement = conn.prepareStatement(updateSql);
            updatePreparedStatement.execute();
        }

        String expectedResult = "First User user@gmail.com";
        Path path = new Path("/users/1");
        Method method = Method.GET;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(), dataSource);
        Optional<CommandResult> optionalCommandResult = routeResult.getHandler().execute(commandRequest);
        CommandResult commandResult = optionalCommandResult.get();
        String firstUser = "";
        while (commandResult.getResultSet().next()) {
            firstUser = (commandResult.getResultSet().getString("name") + " " +
                    commandResult.getResultSet().getString("email"));
        }

        assertEquals(expectedResult, firstUser);

    }

    @Test
    public void getUsersTest() throws SQLException {
        Path path = new Path("/users/");
        Method method = Method.GET;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(), dataSource);
        Optional<CommandResult> optionalCommandResult = routeResult.getHandler().execute(commandRequest);
        CommandResult commandResult = optionalCommandResult.get();
        int numberOfUsers = 0;
        while (commandResult.getResultSet().next()) {
            numberOfUsers++;
        }
        String sql = "SELECT MAX(uid) FROM users";
        Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        int result = 0;
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            result = resultSet.getInt(1);
        }

        assertEquals(numberOfUsers, result);

    }

    @Test
    public void getRoutesTest() throws SQLException {
        Path path = new Path("/routes");
        Method method = Method.GET;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(), dataSource);
        Optional<CommandResult> optionalCommandResult = routeResult.getHandler().execute(commandRequest);
        CommandResult commandResult = optionalCommandResult.get();
        int numberOfRoutes = 0;
        while (commandResult.getResultSet().next()) {
            numberOfRoutes++;
        }
        String sql = "SELECT MAX(rid) FROM routes";
        Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        int result = 0;
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            result = resultSet.getInt(1);
        }

        assertEquals(numberOfRoutes, result);

    }

    @Test
    public void getSportsTest() throws SQLException {
        Path path = new Path("/sports");
        Method method = Method.GET;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(), dataSource);
        Optional<CommandResult> optionalCommandResult = routeResult.getHandler().execute(commandRequest);
        CommandResult commandResult = optionalCommandResult.get();
        int numberOfSports = 0;
        while (commandResult.getResultSet().next()) {
            numberOfSports++;
        }
        String sql = "SELECT MAX(sid) FROM sports";
        Connection conn = dataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        int result = 0;
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            result = resultSet.getInt(1);
        }

        assertEquals(numberOfSports, result);

    }

    @Test
    public void getTopsActivitiesTest() throws SQLException {

        //make sure that database has the desired activity
        String sql = "SELECT * FROM activities WHERE sid=2 AND date='2021-05-02' AND rid=1";
        Connection conn = dataSource.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        int activitiesNum = 0;
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            activitiesNum = resultSet.getInt(1);
        }

        if(activitiesNum<1) {
            String insertSql = "INSERT INTO activities(date, duration_time, sid, uid, rid) VALUES(date='2021-05-02', duration_time='24:00:00',sid=2, uid=1, rid=1)";
            PreparedStatement insertPreparedStatement = conn.prepareStatement(insertSql);
            insertPreparedStatement.execute();
            conn.close();
        }
        else {
            String updateSql = "UPDATE activities SET date='2021-05-02', duration_time='24:00:00', uid=1, rid=1 WHERE sid = 2;";
            PreparedStatement updatePreparedStatement = conn.prepareStatement(updateSql);
            updatePreparedStatement.execute();
            conn.close();
        }

        String expectedResult = "2021-05-02, 24:00:00, 2, 1, 1";
        Path path = new Path("/tops/activities");
        Method method = Method.GET;
        ArrayList<String> parameters = new ArrayList<>(Arrays.asList(("sid=2&orderBy=desc&date=2021-05-02&rid=1").split("&")));
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(), parameters, dataSource);
        Optional<CommandResult> optionalCommandResult = routeResult.getHandler().execute(commandRequest);
        CommandResult commandResult = optionalCommandResult.get();
        String result = "";
        while (commandResult.getResultSet().next()) {
            result = (commandResult.getResultSet().getString("date") + ", " +
                    commandResult.getResultSet().getString("duration_time") + ", " +
                    commandResult.getResultSet().getInt("sid") + ", " +
                    commandResult.getResultSet().getInt("uid") + ", " +
                    commandResult.getResultSet().getInt("rid"));
        }

        assertEquals(expectedResult, result);

    }


}
