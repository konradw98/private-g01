package pt.isel.ls.handlers.get;

public abstract class GetHandler {

    protected String validateHeaders(String acceptArgument, String fileNameArgument) {
        String wrongParameters = "";
        if (acceptArgument != null && !acceptArgument.equals("text/html") && !acceptArgument.equals("text/plain")
                && !acceptArgument.equals("application/json")) {
            wrongParameters += acceptArgument;
        }
        if (fileNameArgument != null && !fileNameArgument.endsWith("txt") && !fileNameArgument.endsWith("html")
                && !fileNameArgument.endsWith("htm") && !fileNameArgument.endsWith("json")) {
            wrongParameters += fileNameArgument;
        }
        return wrongParameters;
    }

}
