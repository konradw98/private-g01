package pt.isel.ls.commandresults;

import pt.isel.ls.Element;
import pt.isel.ls.Text;

public class PostResult implements CommandResult {
    int id;
    String label;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PostResult(int id, String label) {
        this.id = id;
        this.label = label;
    }

    @Override
    public boolean results(boolean http) {
        System.out.println(generateResults(http));
        return false;
    }

    @Override
    public String generateResults(boolean http) {
        Element rootHerf = new Element("a", "href=\"/\"").with(new Text("Root"));
        Element herf = switch (label) {
            case "sid" -> new Element("a", "href=\"/sports\"").with(new Text("Sports"));
            case "uid" -> new Element("a", "href=\"/users\"").with(new Text("Users"));
            case "rid" -> new Element("a", "href=\"/routes\"").with(new Text("Routes"));
            default -> null;
        };
        Element html = new Element("html");
        if (herf == null) {
            html.with(new Element("body").with(rootHerf, new Element("br")));
        } else {
            html.with(new Element("body").with(rootHerf, herf, new Element("br")));
        }
        return html.generateStringHtml("") + label + ": " + id;
    }
}
