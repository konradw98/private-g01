package pt.isel.ls.models;

public class Root implements Model {
    @Override
    public void print() {
        System.out.println(this);
    }

    @Override
    public String generateJson() {
        return "{ root page }";
    }

    @Override
    public String toString() {
        return "Root page";
    }

    public String generateHtml() {
        return """
                <html>
                    <head>
                        <title>
                            Root
                        </title>
                    </head>
                    <body>
                        <h1>Root</h1>
                        <p><a href = "/users?top=5&skip=0">Users</a></p>
                        <p><a href = "/sports?top=5&skip=0">Sports</a></p>
                        <p><a href = "/routes?top=5&skip=0">Routes</a></p>
                    </body>
                </html>\s""";
    }
}
