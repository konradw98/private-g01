package pt.isel.ls.commandresults.getresult;

import pt.isel.ls.Element;
import pt.isel.ls.commandresults.CommandResult;

public abstract class GetCommandResult implements CommandResult {

    public Element html() {
        return new Element("html");
    }

    public Element head() {
        return new Element("head");
    }

    public Element body() {
        return new Element("body");
    }

    public Element title() {
        return new Element("title");
    }

    public Element h1() {
        return new Element("h1");
    }

    public Element h2() {
        return new Element("h2");
    }

    public Element ul() {
        return new Element("ul");
    }

    public Element li() {
        return new Element("li");
    }

    public Element table() {
        return new Element("table");
    }

    public Element table(String param) {
        return new Element("table", param);
    }

    public Element tr() {
        return new Element("tr");
    }

    public Element th() {
        return new Element("th");
    }

    public Element td() {
        return new Element("td");
    }

    public Element a(String param) { return new Element("a", param); }

    public void printResults(String result) {
        if (!result.equals("")) {
            System.out.println(result);
        }
    }

    public int getPageNumber(int skip, int top) {
        int pageNumber = -1;
        if (top == 5 && skip % 5 == 0) pageNumber = (skip / 5) + 1;
        return pageNumber;
    }
}
