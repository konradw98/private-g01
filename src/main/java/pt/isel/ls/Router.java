package pt.isel.ls;

import com.google.common.collect.ArrayListMultimap;
import pt.isel.ls.Handlers.*;

import java.util.*;

public class Router {
    private final ArrayListMultimap<Method, Tuple<PathTemplate, CommandHandler>> mapOfHandlers;

    public Router() {
        mapOfHandlers = ArrayListMultimap.create();
    }

    public void addHandlers() {
        addRoute(Method.GET, new PathTemplate("/users"), new GetUsersHandler());
        addRoute(Method.GET, new PathTemplate("/users/{uid}"), new GetUserByIdHandler());
        addRoute(Method.GET, new PathTemplate("/routes"), new GetRoutesHandler());
        addRoute(Method.GET, new PathTemplate("/routes/{rid}"), new GetRouteByIDHandler());
        addRoute(Method.GET, new PathTemplate("/sports"), new GetSportHandler());
        addRoute(Method.GET, new PathTemplate("/sports/{sid}"), new GetSportByIdHandler());
        addRoute(Method.GET, new PathTemplate("/sports/{sid}/activities"), new GetSportActivitiesHandler());
        addRoute(Method.GET, new PathTemplate("/sports/{sid}/activities/{aid}"), new GetSportActivitiesByIdHandler());
        addRoute(Method.GET, new PathTemplate("/users/{uid}/activities"), new GetUserActivitiesHandler());
        addRoute(Method.GET, new PathTemplate("/users/{uid}/activities/{aid}"), new GetUserActivitiesByIdHandler());
        addRoute(Method.POST, new PathTemplate("/users"), new PostUserHandler());
        addRoute(Method.POST, new PathTemplate("/routes"), new PostRouteHandler());
        addRoute(Method.POST, new PathTemplate("/sports"), new PostSportHandler());
        addRoute(Method.POST, new PathTemplate("/sports/{sid}/activities"),new PostActivityHandler());
    }

    public void addRoute(Method method, PathTemplate pathTemplate, CommandHandler commandHandler) {
        Tuple<PathTemplate, CommandHandler> map = new Tuple<>(pathTemplate, commandHandler);
        mapOfHandlers.put(method, map);
    }

    public Optional<RouteResult> findRoute(Method method, Path path) {
        if (mapOfHandlers.containsKey(method)) {
            List<String> pathSegments = path.splitSegmentsFromPath();
            for (Tuple<PathTemplate, CommandHandler> tuple : mapOfHandlers.get(method)) {
                List<String> pathTemplateSegments = tuple.getFirst().splitSegmentsFromPathTemplate();
                ArrayList<String> parameters = new ArrayList<>();

                if (pathTemplateSegments.size() == pathSegments.size()) {
                    boolean ifSame = true;
                    int i = 0;
                    for (; i < pathSegments.size(); i++) {
                        if (pathTemplateSegments.get(i).charAt(0) != '{') {
                            if (!pathSegments.get(i).equals(pathTemplateSegments.get(i))) {
                                ifSame = false;
                                break;
                            }
                        } else parameters.add(pathSegments.get(i));
                    }

                    if (ifSame)
                        return Optional.of(new RouteResult(tuple.getSecond(), parameters));
                    else parameters.clear();
                }
            }
        }
        return Optional.empty();
    }

    private static class Tuple<T, S> {
        private final T first;
        private final S second;

        public Tuple(T first, S second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }

        public S getSecond() {
            return second;
        }
    }
}
