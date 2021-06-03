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

    public GetByIdServlet(PGSimpleDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //TODO: headers
        String accept = req.getHeader("accept");
        String path = req.getRequestURI();
        Optional<RouteResult> routeResult = Router.findRoute(Method.GET, new Path(path));

        if (routeResult.isPresent()) {
            CommandRequest commandRequest = new CommandRequest(routeResult.get().getPathParameters(), dataSource);
            String respBody = "";
            try {
                CommandResult commandResult = routeResult.get().getHandler().execute(commandRequest);
                respBody = commandResult.generateResults();
                //TODO: not exception
            } catch (Exception e) {
                respBody = new WrongParametersResult().generateResults();
            } finally {
                Charset utf8 = StandardCharsets.UTF_8;
                byte[] respBodyBytes = respBody.getBytes(utf8);
                resp.setStatus(200);
                resp.setContentType("text/html");
                resp.setContentLength(respBodyBytes.length);
                OutputStream os = resp.getOutputStream();
                os.write(respBodyBytes);
                os.flush();
            }
        }
    }
}
