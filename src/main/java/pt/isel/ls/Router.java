package pt.isel.ls;

import java.util.HashMap;
import java.util.Optional;

public class Router {
    private final HashMap<Path, HashMap<Method, CommandHandler>> mapOfHandlers;

    public Router() {
        mapOfHandlers = new HashMap<>();
    }

    public void addRoute(Method method, Path pathTemplate, CommandHandler commandHandler) {
        HashMap<Method, CommandHandler> map = new HashMap<>();
        map.put(method, commandHandler);
        mapOfHandlers.put(pathTemplate, map);
    }

    public Optional<RouteResult> findRoute(Method method, Path path) {
        if (mapOfHandlers.containsKey(path) && mapOfHandlers.get(path).containsKey(method)) {
            return Optional.of(new RouteResult(mapOfHandlers.get(path).get(method)));
        } else return Optional.empty();
    }

}
