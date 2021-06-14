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

public class GetIndexServlet extends HttpServlet {


    public GetIndexServlet(PGSimpleDataSource dataSource) {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String respBody ="<html>\n" +
                "    <head>\n" +
                "        <title>\n" +
                "            Root\n" +
                "        </title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <h1>Root</h1>\n" +
                "        <p><a href = \"/users?top=5&skip=0\">Users</a></p>\n" +
                "        <p><a href = \"/sports?top=5&skip=0\">Sports</a></p>\n" +
                "        <p><a href = \"/routes?top=5&skip=0\">Routes</a></p>\n" +
                "    </body>\n" +
                "</html> ";

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

