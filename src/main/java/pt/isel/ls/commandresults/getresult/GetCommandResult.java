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

    public Element ul() {
        return new Element("ul");
    }

    public Element li() {
        return new Element("li");
    }

    public Element table() {
        return new Element("table");
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
}
