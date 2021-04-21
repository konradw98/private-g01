package pt.isel.ls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Router {
    private final HashMap<Method, HashMap<PathTemplate, CommandHandler>> mapOfHandlers;

    public Router() {
        mapOfHandlers = new HashMap<>();
    }

    public void addRoute(Method method, PathTemplate pathTemplate, CommandHandler commandHandler) {
        HashMap<PathTemplate, CommandHandler> map = new HashMap<>();
        map.put(pathTemplate, commandHandler);
        mapOfHandlers.put(method, map);
    }

    public Optional<RouteResult> findRoute(Method method, Path path) {
        if (mapOfHandlers.containsKey(method)) {
            boolean ifSame = true;
            List<String> pathSegments = path.splitSegmentsFromPath();
            for (PathTemplate pathTemplate : mapOfHandlers.get(method).keySet()) {
                List<String> pathTemplateSegments = pathTemplate.splitSegmentsFromPathTemplate();
                ArrayList<String> parameters = new ArrayList<>();
                if (pathTemplateSegments.size() == pathSegments.size()) {
                    int i = 0;
                    for (; i < pathSegments.size(); i++) {
                        if (pathTemplateSegments.get(i).charAt(0) != '{') {
                            if (!pathSegments.get(i).equals(pathTemplateSegments.get(i))) {
                                ifSame = false;
                            }
                        } else parameters.add(pathSegments.get(i));
                    }
                    if (ifSame)
                        return Optional.of(new RouteResult(mapOfHandlers.get(method).get(pathTemplate), parameters));
                    else parameters.clear();
                } else ifSame = false;
            }
        }
        return Optional.empty();
    }
}
