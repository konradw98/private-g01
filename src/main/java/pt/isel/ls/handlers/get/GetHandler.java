package pt.isel.ls.handlers.get;

import pt.isel.ls.Headers;

public abstract class GetHandler {

    protected String validateHeaders(Headers headers) {
        String wrongParameters = "";

        if (headers == null) {
            return wrongParameters;
        }

        String acceptArgument = headers.get("accept");
        String fileNameArgument = headers.get("file-name");

        if (acceptArgument != null && !acceptArgument.equals("text/html") && !acceptArgument.equals("text/plain")
                && !acceptArgument.equals("application/json")) {
            wrongParameters += "accept ";
        }
        if (fileNameArgument != null && !fileNameArgument.endsWith("txt") && !fileNameArgument.endsWith("html")
                && !fileNameArgument.endsWith("htm") && !fileNameArgument.endsWith("json")) {
            wrongParameters += "file-name ";
        }
        return wrongParameters;
    }

}
