package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;

public class CommandRequest {
    private PathParameters pathParameters;
    private Parameters parameters;
    private Headers headers;
    private PGSimpleDataSource dataSource;

    public CommandRequest(PathParameters pathParameters, PGSimpleDataSource dataSource) {
        this.pathParameters = pathParameters;
        this.dataSource = dataSource;
        this.headers = null;
        this.parameters = null;
    }

    public CommandRequest(PathParameters pathParameters, Parameters parameters, Headers headers,
                          PGSimpleDataSource dataSource) {
        this.pathParameters = pathParameters;
        this.parameters = parameters;
        this.headers = headers;
        this.dataSource = dataSource;

    }

    public CommandRequest(PathParameters pathParameters, Parameters parameters, PGSimpleDataSource dataSource) {
        this.pathParameters = pathParameters;
        this.parameters = parameters;
        this.dataSource = dataSource;
        this.headers = null;
    }

    public CommandRequest(PathParameters pathParameters, Headers headers, PGSimpleDataSource dataSource) {
        this.pathParameters = pathParameters;
        this.headers = headers;
        this.dataSource = dataSource;
        this.parameters = null;
    }

    public PathParameters getPathParameters() {
        return pathParameters;
    }

    public void setPathParameters(PathParameters pathParameters) {
        this.pathParameters = pathParameters;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public boolean hasParameters() {
        return parameters != null;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public PGSimpleDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(PGSimpleDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
