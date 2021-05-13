package pt.isel.ls.commandresults;

import pt.isel.ls.Element;
import pt.isel.ls.Text;
import pt.isel.ls.models.Route;

public class GetRouteResult implements CommandResult {
    private Route route;

    public GetRouteResult(Route route) {
        this.route = route;
    }

    @Override
    public boolean results() {
        route.print();
        return false;
    }

    public String generateJson() {
        return route.generateJson();
    }

    public Element generateHtml() {
        Element html = html();

        html.with(head().with(title().with(new Text("Routes"))));
        html.with(body().with(h1().with(new Text("Route Details")),
                            ul().with(
                                        li().with(new Text("Identifier : " + route.getRid())),
                                        li().with(new Text("Start Location : " + route.getStartLocation())),
                                        li().with(new Text("End Location : " + route.getEndLocation())),
                                        li().with(new Text("Distance : " + route.getDistance()))
                                    )
                            )
                );


        return html;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
