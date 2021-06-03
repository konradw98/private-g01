package pt.isel.ls.handlers;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import pt.isel.ls.CommandRequest;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.ListenResult;
import pt.isel.ls.http.servlets.*;

public class ListenHandler implements CommandHandler {
    private static final int LISTEN_PORT = 8080;

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws Exception {
        String portDef = System.getenv("PORT");
        int port = portDef != null ? Integer.parseInt(portDef) : LISTEN_PORT;

        Server server = new Server(port);
        ServletHandler handler = new ServletHandler();

        handler.addServletWithMapping(new ServletHolder(new GetByIdServlet(commandRequest.getDataSource())),
                "/users/*");
        handler.addServletWithMapping(new ServletHolder(new GetTablesServlet(commandRequest.getDataSource())),
                "/users");
        handler.addServletWithMapping(new ServletHolder(new GetByIdServlet(commandRequest.getDataSource())),
                "/routes/*");
        handler.addServletWithMapping(new ServletHolder(new GetTablesServlet(commandRequest.getDataSource())),
                "/routes");
        handler.addServletWithMapping(new ServletHolder(new GetByIdServlet(commandRequest.getDataSource())),
                "/sports/*");
        handler.addServletWithMapping(new ServletHolder(new GetTablesServlet(commandRequest.getDataSource())),
                "/sports");

        server.setHandler(handler);
        server.start();
        server.join();

        return new ListenResult();
    }
}
