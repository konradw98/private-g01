package pt.isel.ls;

import java.util.ArrayList;
import java.util.Collections;

public class Element {

    private final ArrayList<Element> children;
    private Text text;
    private final String name;
    private String param;
    private boolean singleTag = false;

    public Element(String name) {
        this.name = name;
        children = new ArrayList<>();
    }

    public Element(String name, String param) {
        this.param = param;
        this.name = name;
        children = new ArrayList<>();
    }

    public Element with(Element... elements) {
        Collections.addAll(children, elements);
        return this;
    }

    public Element with(Text text, Element... elements) {
        Collections.addAll(children, elements);
        this.text = text;
        return this;
    }

    public Element with(Text text) {
        this.text = text;
        return this;
    }

    public void setAsSingleTag() {
        singleTag = true;
    }

    public String generateStringHtml(String prefix) {
        StringBuilder html;
        if (param != null) {
            html = new StringBuilder(prefix + "<" + name + " " + param + " >");
        } else {
            html = new StringBuilder(prefix + "<" + name + ">");
        }
        if (text != null) {
            html.append(text);
        } else {
            html.append("\n");
        }
        for (Element element : children) {
            html.append(element.generateStringHtml(prefix + "\t"));
        }
        if (text != null) {
            html.append("</").append(name).append(">\n");
        } else {
            if (!singleTag) {
                html.append(prefix).append("</").append(name).append(">\n");
            }
        }
        return String.valueOf(html);
    }
}
