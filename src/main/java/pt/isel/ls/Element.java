package pt.isel.ls;

import java.util.ArrayList;
import java.util.Collections;

public class Element {

    ArrayList<Element> children;
    Text text;
    String name;

    public Element(String name) {
        this.name = name;
        children = new ArrayList<>();
    }

    public Element with(Element ... elements)
    {
        Collections.addAll(children, elements);
        return this;
    }

    public Element with(Text text)
    {
        this.text = text;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder html = new StringBuilder("<" + name + ">" + "\t");
        for(Element element: children) html.append(element.toString());
        html.append("</").append(name).append(">");

        return html.toString();
    }
}
