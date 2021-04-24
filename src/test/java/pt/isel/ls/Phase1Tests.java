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
    public void init(){
        router = new Router();
        router.addHandlers();

        dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");
    }


    @Test
    public void wrongPathTest()
    {
        Path path = new Path("users/1");
        Method method = Method.GET;
        Optional<RouteResult> optional = router.findRoute(method, path);
        assertEquals(Optional.empty(), optional);
    }

    @Test
    public void wrongPathParamsTest() throws SQLException
    {
        Path path = new Path("/routes");
        Method method = Method.POST;
        Optional<RouteResult> optional = router.findRoute(method, path);
        RouteResult routeResult = optional.get();
        ArrayList<String> parameters = new ArrayList<>(Arrays.asList(("start_location=Wroclaw&end_location=Warszawa&distance=-1").split("&")));
        CommandRequest commandRequest = new CommandRequest(routeResult.getPathParameters(), parameters, dataSource);
        Optional<CommandResult> optionalCommandResult = routeResult.getHandler().execute(commandRequest);
        assertEquals(Optional.empty(), optionalCommandResult);
    }

}
