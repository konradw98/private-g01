package pt.isel.ls;

import com.google.common.collect.ArrayListMultimap;
import pt.isel.ls.handlers.*;
import pt.isel.ls.handlers.get.*;
import pt.isel.ls.handlers.get.gettables.*;
import java.util.*;

public class Router {
    private static final ArrayListMultimap<Method, Tuple<PathTemplate, CommandHandler>> mapOfHandlers = ArrayListMultimap.create();

    public void addHandlers() {
        addRoute(Method.GET, new PathTemplate("/users"), new GetUsersHandler());
        addRoute(Method.GET, new PathTemplate("/users/{uid}"), new GetUserByIdHandler());
        addRoute(Method.GET, new PathTemplate("/routes"), new GetRoutesHandler());
        addRoute(Method.GET, new PathTemplate("/routes/{rid}"), new GetRouteByIdHandler());
        addRoute(Method.GET, new PathTemplate("/sports"), new GetSportsHandler());
        addRoute(Method.GET, new PathTemplate("/sports/{sid}"), new GetSportByIdHandler());
        addRoute(Method.GET, new PathTemplate("/sports/{sid}/activities"), new GetSportActivitiesHandler());
        addRoute(Method.GET, new PathTemplate("/sports/{sid}/activities/{aid}"), new GetSportActivitiesByIdHandler());
        addRoute(Method.GET, new PathTemplate("/users/{uid}/activities"), new GetUserActivitiesHandler());
        addRoute(Method.GET, new PathTemplate("/users/{uid}/activities/{aid}"), new GetUserActivitiesByIdHandler());
        addRoute(Method.POST, new PathTemplate("/users"), new PostUserHandler());
        addRoute(Method.POST, new PathTemplate("/routes"), new PostRouteHandler());
        addRoute(Method.POST, new PathTemplate("/sports"), new PostSportHandler());
        addRoute(Method.POST, new PathTemplate("/sports/{sid}/activities"), new PostActivityHandler());
        addRoute(Method.GET, new PathTemplate("/tops/activities"), new GetTopsActivitiesHandler());
        addRoute(Method.EXIT, new PathTemplate("/"), new ExitHandler());
        addRoute(Method.OPTION, new PathTemplate("/"), new OptionHandler());
        addRoute(Method.DELETE, new PathTemplate("/users/{uid}/activities"), new DeleteHandler());
        addRoute(Method.LISTEN, new PathTemplate("/"), new ListenHandler());
        addRoute(Method.GET, new PathTemplate("/"), new GetRootHandler());
    }

    public void addRoute(Method method, PathTemplate pathTemplate, CommandHandler commandHandler) {
        Tuple<PathTemplate, CommandHandler> map = new Tuple<>(pathTemplate, commandHandler);
        mapOfHandlers.put(method, map);
    }

    public static Optional<RouteResult> findRoute(Method method, Path path) {
        if (mapOfHandlers.containsKey(method)) {
            List<String> pathSegments = path.splitSegmentsFromPath();
            PathParameters pathParameters = new PathParameters();

            for (Tuple<PathTemplate, CommandHandler> tuple : mapOfHandlers.get(method)) {
                List<String> pathTemplateSegments = tuple.getFirst().splitSegmentsFromPathTemplate();

                if (pathTemplateSegments.size() == pathSegments.size()) {
                    boolean ifSame = false;
                    for (int i = 0; i < pathTemplateSegments.size(); i++) {
                        ifSame = true;
                        if (pathTemplateSegments.get(i).startsWith("{")
                                && pathTemplateSegments.get(i).endsWith("}")) {
                            pathParameters.addPathParameter(pathTemplateSegments.get(i).substring(1, 4),
                                    pathSegments.get(i));
                        } else if (!pathTemplateSegments.get(i).equals(pathSegments.get(i))) {
                            pathParameters.clear();
                            ifSame = false;
                            break;
                        }
                    }
                    if (ifSame) {
                        return Optional.of(new RouteResult(tuple.getSecond(), pathParameters));
                    }
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
