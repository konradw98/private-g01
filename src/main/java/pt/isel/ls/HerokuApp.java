package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HerokuApp {
    private static Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        log.info("main started");

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(System.getenv("JDBC_DATABASE_URL"));

        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.runCommand("LISTEN /", dataSource);
    }
}
