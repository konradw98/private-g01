package pt.isel.ls;

public class Text {

    Object text;

    public Text(Object text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text.toString();
    }
}
