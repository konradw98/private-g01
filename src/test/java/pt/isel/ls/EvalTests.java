package pt.isel.ls;

import org.junit.Before;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;
import static org.junit.Assert.assertEquals;

public class EvalTests {

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
    public void wrongPathParamsTypoInPathTest() throws SQLException {
        Path path = new Path("/users/1/activities/cout");
        Method method = Method.GET;
        Optional<RouteResult> optional = router.findRoute(method, path);
        assertEquals(Optional.empty(), optional);
    }

    @Test
    public void getUsersPlainTest() {

        CommandExecutor.runCommand("GET /users/3/activities/count accept:text/plain|"
                + "file-name:src/test/files/usersCount.txt ", router, dataSource);
        String data = "";
        try {
            File myObj = new File("src/test/files/usersCount.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                System.out.println(data);

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String excepted = "id: 3 email: ZnKal@gmail.com name: ZnKal Number of activities 0";
        assertEquals(excepted, data);

    }


}
