package pt.isel.ls.commandresults;

import pt.isel.ls.Element;

public interface CommandResult {
    boolean results();

    default Element html() {
        return new Element("html");
    }

    default Element head() {
        return new Element("head");
    }

    default Element body() {
        return new Element("body");
    }

    default Element title() {
        return new Element("title");
    }

    default Element h1() {
        return new Element("h1");
    }

    default Element ul() {
        return new Element("ul");
    }

    default Element li() {
        return new Element("li");
    }

    default Element table() {
        return new Element("table");
    }

    default Element tr() {
        return new Element("tr");
    }

    default Element th() {
        return new Element("th");
    }

    default Element td() {
        return new Element("td");
    }


}
