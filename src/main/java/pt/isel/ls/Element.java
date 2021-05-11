package pt.isel.ls;

import java.util.ArrayList;

public class Element {

    ArrayList<Element> children;
    Text text;

    public Element(String name) {
        children = new ArrayList<>();
    }

    public Element with(Element ... elements)
    {
        for (Element element:elements) {
            children.add(element);
        }
        return this;
    }

    public Element with(Text text)
    {
        this.text = text;
        return this;
    }


}
