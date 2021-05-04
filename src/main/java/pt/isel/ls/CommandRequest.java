package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.util.ArrayList;

public class CommandRequest {
    private ArrayList<String> pathParameters;
    private ArrayList<String> parameters;
    private PGSimpleDataSource dataSource;

    public CommandRequest(ArrayList<String> pathParameters, ArrayList<String> parameters, PGSimpleDataSource dataSource) {
        this.pathParameters = pathParameters;
        this.parameters = parameters;
        this.dataSource = dataSource;
    }

    public CommandRequest(ArrayList<String> pathParameters, PGSimpleDataSource dataSource) {
        this.pathParameters = pathParameters;
        this.dataSource = dataSource;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }

    public ArrayList<String> getPathParameters() {
        return pathParameters;
    }

    public void setPathParameters(ArrayList<String> parameters) {
        this.pathParameters = parameters;
    }

    public PGSimpleDataSource getDataSource() {
        return dataSource;
    }
}
