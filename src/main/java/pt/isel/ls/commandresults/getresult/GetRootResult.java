package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Headers;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.models.Root;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class GetRootResult extends GetCommandResult implements CommandResult {
    private final Root root;
    private Headers headers;

    public GetRootResult(Root root, Headers headers) {
        this.root = root;
        this.headers = headers;
    }

    @Override
    public boolean results(boolean http) {
        System.out.println(generateResults(http));
        return false;
    }

    @Override
    public String generateResults(boolean http) {
        String accept;
        String fileName;
        if (headers == null) {
            accept = "text/html";
            fileName = null;
        } else {
            accept = headers.get("accept") == null ? "text/html" : headers.get("accept");
            fileName = headers.get("file-name");
        }

        String str;
        switch (accept) {
            case "text/plain" -> str = root.toString();
            case "application/json" -> str = root.generateJson();
            default -> str = root.generateHtml();
        }
        if (fileName != null) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                writer.write(str);
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return "";
            }
        } else {
            return str;
        }
    }
}
