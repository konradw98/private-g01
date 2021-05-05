package pt.isel.ls;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.commandresults.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;

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
        ArrayList<String> parameters = new ArrayList<>(Arrays
                .asList(("startLocation=Wroclaw&endLocation=Warszawa&distance=-1.0").split("&")));
        CommandRequest commandRequest =
                new CommandRequest(new ArrayList<>(routeResult.getPathParameters().values()), parameters, dataSource);
        CommandResult commandResult = routeResult.getHandler().execute(commandRequest);
        assertThat(commandResult, instanceOf(WrongParametersResult.class));
    }

    @Test
    public void wrongPathParamsTypoInNameTest() throws SQLException {
        Path path = new Path("/users");
        Method method = Method.POST;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        ArrayList<String> parameters = new ArrayList<>(Arrays.asList(("nam=John&email=mail").split("&")));
        CommandRequest commandRequest =
                new CommandRequest(new ArrayList<>(routeResult.getPathParameters().values()), parameters, dataSource);
        CommandResult commandResult = routeResult.getHandler().execute(commandRequest);
        assertThat(commandResult, instanceOf(WrongParametersResult.class));
    }

    @Test
    public void wrongPathParamsActivitiesIdTest() throws SQLException {

        Path correctPath = new Path("/sports/1/activities");
        Path wrongSidPath = new Path("/sports/1000/activities");
        Method method = Method.POST;
        ArrayList<String> parameters =
                new ArrayList<>(Arrays.asList(("uid=1&duration=00:10:30&date=2021-04-21&rid=1").split("&")));
        ArrayList<String> wrongUidParameters =
                new ArrayList<>(Arrays.asList(("uid=1000&duration=00:10:30&date=2021-04-21&rid=1").split("&")));
        ArrayList<String> wrongRidParameters =
                new ArrayList<>(Arrays.asList(("uid=1&duration=00:10:30&date=2021-04-21&rid=1000").split("&")));

        Optional<RouteResult> optional = router.findRoute(method, correctPath);
        RouteResult routeResult = optional.get();
        CommandRequest wrongUidCommandRequest = new CommandRequest(new ArrayList<>(routeResult.getPathParameters()
                .values()), wrongUidParameters, dataSource);
        CommandResult wrongUidCommandResult = routeResult.getHandler().execute(wrongUidCommandRequest);
        assertThat(wrongUidCommandResult, instanceOf(WrongParametersResult.class));

        CommandRequest wrongRidCommandRequest = new CommandRequest(new ArrayList<>(routeResult
                .getPathParameters().values()), wrongRidParameters, dataSource);
        CommandResult wrongRidCommandResult = routeResult.getHandler().execute(wrongRidCommandRequest);
        assertThat(wrongRidCommandResult, instanceOf(WrongParametersResult.class));

        Optional<RouteResult> wrongPathOptional = router.findRoute(method, wrongSidPath);
        RouteResult wrongPathRouteResult = wrongPathOptional.get();
        CommandRequest wrongPathCommandRequest =
                new CommandRequest(new ArrayList<>(routeResult.getPathParameters().values()), parameters, dataSource);
        CommandResult wrongPathCommandResult = wrongPathRouteResult.getHandler().execute(wrongPathCommandRequest);
        //assertThat(wrongPathCommandResult, instanceOf(WrongParametersResult.class));
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
        CommandRequest commandRequest =
                new CommandRequest(new ArrayList<>(routeResult.getPathParameters().values()), testDataSource);
        routeResult.getHandler().execute(commandRequest);
    }

    //exception because of unique mail parameters
    @Test()
    public void postUserTest() throws SQLException {
        String name1 = RandomStringUtils.random(5, true, false);
        String email1 = name1 + "@gmail.com";
        Path path = new Path("/users");
        Method method = Method.POST;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        ArrayList<String> parameters1 = new ArrayList<>(Arrays.asList(("name=" + name1 + "&email=" + email1)
                .split("&")));
        CommandRequest commandRequest1 = new CommandRequest(new ArrayList<>(routeResult.getPathParameters().values()),
                parameters1, dataSource);
        PostResult commandResult1 = (PostResult) routeResult.getHandler().execute(commandRequest1);
        int numberOfUsersAfterFirstPost = commandResult1.getId();

        String name2 = RandomStringUtils.random(5, true, false);
        String email2 = name2 + "@gmail.com";
        ArrayList<String> parameters2 = new ArrayList<>(Arrays.asList(("name=" + name2 + "&email=" + email2)
                .split("&")));
        CommandRequest commandRequest2 = new CommandRequest(new ArrayList<>(routeResult.getPathParameters()
                .values()), parameters2, dataSource);
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
        ArrayList<String> parameters1 = new ArrayList<>(Arrays
                .asList(("distance=3051.0&startLocation=Poland&endLocation=Portugal").split("&")));
        CommandRequest commandRequest1 =
                new CommandRequest(new ArrayList<>(routeResult.getPathParameters().values()), parameters1, dataSource);
        PostResult commandResult1 = (PostResult) routeResult.getHandler().execute(commandRequest1);
        int numberOfRoutesAfterFirstPost = commandResult1.getId();

        ArrayList<String> parameters2 = new ArrayList<>(Arrays
                .asList(("distance=3014.3&startLocation=Wroclaw&endLocation=Lisbon").split("&")));
        CommandRequest commandRequest2 =
                new CommandRequest(new ArrayList<>(routeResult.getPathParameters().values()), parameters2, dataSource);
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
        ArrayList<String> parameters1 = new ArrayList<>(Arrays
                .asList(("description=travelling+over+snow+on+ski&name=skiing").split("&")));
        CommandRequest commandRequest1 =
                new CommandRequest(new ArrayList<>(routeResult.getPathParameters().values()), parameters1, dataSource);
        PostResult commandResult1 = (PostResult) routeResult.getHandler().execute(commandRequest1);
        int numberOfSportsAfterFirstPost = commandResult1.getId();

        ArrayList<String> parameters2 = new ArrayList<>(Arrays
                .asList(("description=sliding+downhill+on+a+snowboard&name=snowboarding").split("&")));
        CommandRequest commandRequest2 =
                new CommandRequest(new ArrayList<>(routeResult.getPathParameters().values()), parameters2, dataSource);
        PostResult commandResult2 = (PostResult) routeResult.getHandler().execute(commandRequest2);
        int numberOfSportsAfterSecondPost = commandResult2.getId();

        assertEquals(numberOfSportsAfterFirstPost + 1, numberOfSportsAfterSecondPost);
    }

    @Test
    public void postActivityTest() throws SQLException {

        Path path = new Path("/sports/3/activities");
        Method method = Method.POST;
        ArrayList<String> parameters1 =
                new ArrayList<>(Arrays.asList(("uid=1&duration=01:01:01&date=2001-01-01&rid=1").split("&")));
        ArrayList<String> parameters2 =
                new ArrayList<>(Arrays.asList(("uid=2&duration=02:02:02&date=2002-02-02&rid=2").split("&")));

        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();

        CommandRequest commandRequest1 = new CommandRequest(new ArrayList<>(routeResult.getPathParameters().values()),
                parameters1, dataSource);
        PostResult commandResult1 = (PostResult) routeResult.getHandler().execute(commandRequest1);
        int numberOfActivitiesAfterFirstPost = commandResult1.getId();


        CommandRequest commandRequest2 = new CommandRequest(new ArrayList<>(routeResult.getPathParameters().values()),
                parameters2, dataSource);
        PostResult commandResult2 = (PostResult) routeResult.getHandler().execute(commandRequest2);
        int numberOfActivitiesSecondFirstPost = commandResult2.getId();

        assertEquals(numberOfActivitiesAfterFirstPost + 1, numberOfActivitiesSecondFirstPost);
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

        if (maxUid < 1) {
            String insertSql = "INSERT INTO users(name, email) VALUES('First User', "
                    + "'user@gmail.com');";
            PreparedStatement insertPreparedStatement = conn.prepareStatement(insertSql);
            insertPreparedStatement.execute();
        } else {
            String updateSql = "UPDATE users SET name ='First User', email='user@gmail.com' WHERE uid = 1;";
            PreparedStatement updatePreparedStatement = conn.prepareStatement(updateSql);
            updatePreparedStatement.execute();
        }

        String expectedResult = "User{uid=1, email=user@gmail.com, name=First User}";
        Path path = new Path("/users/1");
        Method method = Method.GET;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        CommandRequest commandRequest = new CommandRequest(new ArrayList<>(routeResult.getPathParameters().values()),
                dataSource);
        GetUserByIdResult commandResult = (GetUserByIdResult) routeResult.getHandler().execute(commandRequest);
        String firstUser = commandResult.getUser().toString();

        assertEquals(expectedResult, firstUser);

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

        if (activitiesNum < 1) {
            String insertSql = "INSERT INTO activities(date, duration_time, sid, uid, rid) "
                    + "VALUES('2021-05-02', '04:00:00', 2, 1, 1)";
            PreparedStatement insertPreparedStatement = conn.prepareStatement(insertSql);
            insertPreparedStatement.execute();
            conn.close();
        } else {
            String updateSql = "UPDATE activities SET date='2021-05-02', duration_time='04:00:00', "
                    + "uid=1, rid=1 WHERE sid = 2;";
            PreparedStatement updatePreparedStatement = conn.prepareStatement(updateSql);
            updatePreparedStatement.execute();
            conn.close();
        }

        String expectedResult = "[Activity{date=2021-05-02, durationTime=04:00:00, sid=2, uid=1, rid=1}]";
        Path path = new Path("/tops/activities");
        Method method = Method.GET;
        ArrayList<String> parameters =
                new ArrayList<>(Arrays.asList(("sid=2&orderBy=desc&date=2021-05-02&rid=1").split("&")));
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        CommandRequest commandRequest =
                new CommandRequest(new ArrayList<>(routeResult.getPathParameters().values()), parameters, dataSource);
        GetActivitiesResult commandResult = (GetActivitiesResult) routeResult.getHandler().execute(commandRequest);
        String result = commandResult.getActivities().toString();

        assertEquals(expectedResult, result);

    }

    @Test
    public void getUsersTest() throws SQLException {
        Path path = new Path("/users/");
        Method method = Method.GET;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        CommandRequest commandRequest = new CommandRequest(new ArrayList<>(routeResult.getPathParameters().values()),
                dataSource);
        GetUsersResult commandResult = (GetUsersResult) routeResult.getHandler().execute(commandRequest);
        int numberOfUsers = commandResult.getUsers().size();

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
        CommandRequest commandRequest = new CommandRequest(new ArrayList<>(routeResult.getPathParameters().values()),
                dataSource);
        GetRouteResults commandResult = (GetRouteResults) routeResult.getHandler().execute(commandRequest);
        int numberOfRoutes = commandResult.getRoutes().size();
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
        CommandRequest commandRequest = new CommandRequest(new ArrayList<>(routeResult.getPathParameters().values()),
                dataSource);
        GetSportsResult commandResult = (GetSportsResult) routeResult.getHandler().execute(commandRequest);
        int numberOfSports = commandResult.getSports().size();
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


}
