package pt.isel.ls;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.util.ArrayList;

public class CommandRequest {
    private ArrayList<Integer> pathParameters;
    private ArrayList<String> parameters;
    private PGSimpleDataSource dataSource;

    public CommandRequest(ArrayList<Integer> pathParameters, ArrayList<String> parameters, PGSimpleDataSource dataSource) {
        this.pathParameters = pathParameters;
        this.parameters = parameters;
        this.dataSource = dataSource;
    }

    public CommandRequest(ArrayList<Integer> pathParameters, PGSimpleDataSource dataSource) {
        this.pathParameters = pathParameters;
        this.dataSource = dataSource;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }

    public ArrayList<Integer> getPathParameters() {
        return pathParameters;
    }

    public void setPathParameters(ArrayList<Integer> parameters) {
        this.pathParameters = parameters;
    }

    public PGSimpleDataSource getDataSource() {
        return dataSource;
    }
}
