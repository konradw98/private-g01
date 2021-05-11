package pt.isel.ls.commandresults;

import pt.isel.ls.Element;

public interface CommandResult {
    void print();

    public default Element html(){
        return new Element("html");
    }

    public default Element head(){
        return new Element("head");
    }

    public default Element body(){
        return new Element("body");
    }

    public default Element title(){
        return new Element("title");
    }

    public default Element h1(){
        return new Element("h1");
    }

    public default Element ul(){
        return new Element("ul");
    }

    public default Element li(){
        return new Element("li");
    }


}
