package pt.isel.ls.http.servlets;

import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.*;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.handlers.get.gettables.GetTablesHandler;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet("/AppServlet")
public class AppServlet extends HttpServlet {
    private final PGSimpleDataSource dataSource;

    public AppServlet(PGSimpleDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String accept = req.getHeader("accept");
        String fileName = req.getHeader("file-name");

        if (accept == null) {
            accept = "text/html";
        } else if (!accept.equals("application/json") && !accept.equals("text/plain")) {
            accept = "text/html";
        }

        if (fileName == null) {
            fileName = "";
        } else {
            fileName = "|file-name:" + fileName;
        }

        String header = "accept:" + accept + fileName;
        Headers headers = new Headers(header);
        String path = req.getRequestURI();

        Optional<RouteResult> routeResult = Router.findRoute(Method.GET, new Path(path));

        if (routeResult.isPresent()) {
            String queryString = req.getQueryString();
            Parameters parameters = null;
            if (queryString == null && routeResult.get().getHandler() instanceof GetTablesHandler) {
                if (accept.equals("text/html")) {
                    queryString = "skip=0&top=5";
                } else {
                    queryString = "skip=0&top=" + Integer.MAX_VALUE;
                }
                parameters = new Parameters(queryString);
            } else if (queryString != null) {
                parameters = new Parameters(queryString);
            }
            CommandRequest commandRequest;
            if (parameters == null) {
                commandRequest = new CommandRequest(routeResult.get().getPathParameters(), headers, dataSource);
            } else {
                commandRequest = new CommandRequest(routeResult.get().getPathParameters(), parameters, headers,
                        dataSource);
            }
            String respBody = "";
            try {
                CommandResult commandResult = routeResult.get().getHandler().execute(commandRequest);
                respBody = commandResult.generateResults(true);
                //TODO: not exception
            } catch (Exception e) {
                respBody = new WrongParametersResult().generateResults(true);
            } finally {
                Charset utf8 = StandardCharsets.UTF_8;
                byte[] respBodyBytes = respBody.getBytes(utf8);

                switch (accept) {
                    case "text/plain" -> resp.setContentType("text/plain");
                    case "application/json" -> resp.setContentType("application/json");
                    default -> resp.setContentType("text/html");
                }

                if (fileName.equals("")) {
                    switch (respBody.substring(0, 4)) {
                        case "Reso" -> //resp.setStatus(404);
                                resp.sendError(404, "resource not found");
                        case "Wron" -> //resp.setStatus(400);
                                resp.sendError(400, respBody);
                        default -> resp.setStatus(200);
                    }
                }


                resp.setContentLength(respBodyBytes.length);
                System.out.println(resp.toString());
                OutputStream os = resp.getOutputStream();
                os.write(respBodyBytes);
                os.flush();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String accept = req.getHeader("accept");
        String fileName = req.getHeader("file-name");

        if (accept == null) {
            accept = "text/html";
        } else if (!accept.equals("application/json") && !accept.equals("text/plain")) {
            accept = "text/html";
        }

        if (fileName == null) {
            fileName = "";
        } else {
            fileName = "|file-name:" + fileName;
        }

        String header = "accept:" + accept + fileName;
        Headers headers = new Headers(header);
        String path = req.getRequestURI();

        Optional<RouteResult> routeResult = Router.findRoute(Method.POST, new Path(path));

        if (routeResult.isPresent()) {
            byte[] bytes = new byte[req.getContentLength()];
            req.getInputStream().read(bytes);
            String content = new String(bytes);
            Parameters parameters = new Parameters(content);
            CommandRequest commandRequest = new CommandRequest(routeResult.get().getPathParameters(), parameters,
                    headers, dataSource);

            String respBody = "";
            try {
                CommandResult commandResult = routeResult.get().getHandler().execute(commandRequest);
                respBody = commandResult.generateResults(true);
            } catch (Exception e) {
                respBody = new WrongParametersResult().generateResults(true);
            } finally {
                Charset utf8 = StandardCharsets.UTF_8;
                byte[] respBodyBytes = respBody.getBytes(utf8);

                switch (accept) {
                    case "text/plain" -> resp.setContentType("text/plain");
                    case "application/json" -> resp.setContentType("application/json");
                    default -> resp.setContentType("text/html");
                }

                if (fileName.equals("")) {
                    switch (respBody.substring(0, 4)) {
                        case "Reso" -> //resp.setStatus(404);
                                resp.sendError(404, "resource not found");
                        case "Wron" -> //resp.setStatus(400);
                                resp.sendError(400, respBody);
                        default -> resp.setStatus(200);
                    }
                }

                resp.setContentLength(respBodyBytes.length);
                System.out.println(resp.toString());
                OutputStream os = resp.getOutputStream();
                os.write(respBodyBytes);
                os.flush();
            }
        }
    }
}
