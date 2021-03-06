package pt.isel.ls.handlers;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.CommandRequest;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.ListenResult;
import pt.isel.ls.http.servlets.AppServlet;

public class ListenHandler implements CommandHandler {
    private static final int LISTEN_PORT = 8080;

    private static Logger log = LoggerFactory.getLogger(ListenHandler.class);

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws Exception {
        Thread listenThread = new Thread(new ListenThread(commandRequest));
        listenThread.start();

        return new ListenResult();
    }


    public static class ListenThread implements Runnable {
        private final CommandRequest commandRequest;
        private static Server server = null;

        public ListenThread(CommandRequest commandRequest) {
            this.commandRequest = commandRequest;
        }

        @Override
        public void run() {
            if (server == null) {
                String portDef = System.getenv("PORT");
                int port = portDef != null ? Integer.parseInt(portDef) : LISTEN_PORT;

                log.info("Server started listening on port {}", port);

                server = new Server(port);

                ServletHandler handler = new ServletHandler();
                handler.addServletWithMapping(new ServletHolder(new AppServlet(commandRequest.getDataSource())),
                        "/*");

                server.setHandler(handler);
                try {
                    server.start();
                    server.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                log.info("Server is already started");
            }

        }

        public static void stop() throws Exception {
            server.stop();
        }
    }
}
