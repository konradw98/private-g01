package pt.isel.ls.http.servlets;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.*;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class GetByIdServlet extends HttpServlet {
    private final PGSimpleDataSource dataSource;
    boolean http = false;

    public GetByIdServlet(PGSimpleDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //TODO: headers
        String accept = req.getHeader("accept");
        String header="accept:"+accept;
        Headers headers= new Headers(header);
        String path = req.getRequestURI();
        Optional<RouteResult> routeResult = Router.findRoute(Method.GET, new Path(path));

        if (routeResult.isPresent()) {
            CommandRequest commandRequest = new CommandRequest(routeResult.get().getPathParameters(), headers ,dataSource);
            String respBody = "";
            try {
                CommandResult commandResult = routeResult.get().getHandler().execute(commandRequest);
                respBody = commandResult.generateResults(http);
                //TODO: not exception
            } catch (Exception e) {
                respBody = new WrongParametersResult().generateResults(http);
            } finally {
                Charset utf8 = StandardCharsets.UTF_8;
                byte[] respBodyBytes = respBody.getBytes(utf8);
                resp.setStatus(200);
                switch (accept){
                    case "text/plain" -> {
                        resp.setContentType("text/plain");
                    }
                    case "application/json" -> {
                        resp.setContentType("application/json");
                    }

                    default -> {
                        resp.setContentType("text/html");
                    }
                }

                resp.setContentLength(respBodyBytes.length);
                OutputStream os = resp.getOutputStream();
                os.write(respBodyBytes);
                os.flush();
            }
        }
    }
}
