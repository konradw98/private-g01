package pt.isel.ls.handlers.get;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.Headers;
import pt.isel.ls.Parameters;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.getresult.GetActivitiesResult;
import pt.isel.ls.commandresults.WrongParametersResult;
import pt.isel.ls.handlers.CommandHandler;
import pt.isel.ls.models.Activity;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

public class GetTopsActivitiesHandler extends GetHandler implements CommandHandler {
    private static final int PARAMETERS_WITH_3_OPTIONALS = 5;
    private static final int PARAMETERS_WITH_2_OPTIONALS = 4;
    private static final int PARAMETERS_WITH_1_OPTIONAL = 3;
    private static final int MIN_AMOUNT_OF_PARAMETERS = 2;


    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Parameters parameters = commandRequest.getParameters();
        String sid = parameters.get("sid");
        String orderBy = parameters.get("orderBy");
        String date = parameters.get("date");
        String rid = parameters.get("rid");
        String minDistance = parameters.get("distance");

        String wrongParameters = checkParametersWithoutRidAndDate(sid, orderBy);

        Headers headers = commandRequest.getHeaders();
        String acceptArgument = headers.get("accept");
        String fileNameArgument = headers.get("file-name");

        wrongParameters += validateHeaders(acceptArgument, fileNameArgument);
        if (!wrongParameters.equals("")) {
            return new WrongParametersResult(wrongParameters);
        }

        Connection conn = commandRequest.getDataSource().getConnection();
        try {
            PreparedStatement pstmt;

            if (parameters.size() == PARAMETERS_WITH_3_OPTIONALS) {
                wrongParameters += checkRid(rid);
                wrongParameters += checkDate(date);
                wrongParameters += checkDistance(minDistance);

                if (!wrongParameters.equals("")) {
                    conn.close();
                    return new WrongParametersResult(wrongParameters);
                }


                String sql = "SELECT * FROM activities WHERE sid=? AND date=? AND rid=? AND rid IN"
                        + " (SELECT rid FROM routes WHERE distance > ?) ORDER BY duration_time " + orderBy;
                pstmt = conn.prepareStatement(sql);

                pstmt.setInt(1, Integer.parseInt(sid));
                pstmt.setDate(2, Date.valueOf(date));
                pstmt.setInt(3, Integer.parseInt(rid));
                pstmt.setDouble(4, Double.parseDouble(minDistance));

                ResultSet resultSet = pstmt.executeQuery();
                conn.close();
                return executeActivitiesResult(resultSet, commandRequest.getHeaders());
            } else if (parameters.size() == PARAMETERS_WITH_2_OPTIONALS) {
                if (date == null) {
                    wrongParameters += checkRid(rid);
                    wrongParameters += checkDistance(minDistance);
                    if (!wrongParameters.equals("")) {
                        conn.close();
                        return new WrongParametersResult(wrongParameters);
                    }

                    String sql = "SELECT * FROM activities WHERE sid=? AND rid=? AND rid IN (SELECT rid FROM routes "
                            + "WHERE distance > ?) ORDER BY duration_time " + orderBy;
                    pstmt = conn.prepareStatement(sql);

                    pstmt.setInt(1, Integer.parseInt(sid));
                    pstmt.setInt(2, Integer.parseInt(rid));
                    pstmt.setDouble(3, Double.parseDouble(minDistance));

                    ResultSet resultSet = pstmt.executeQuery();
                    conn.close();
                    return executeActivitiesResult(resultSet, commandRequest.getHeaders());
                } else if (rid == null) {
                    wrongParameters += checkDistance(minDistance);
                    wrongParameters += checkDate(date);
                    if (!wrongParameters.equals("")) {
                        conn.close();
                        return new WrongParametersResult(wrongParameters);
                    }

                    String sql = "SELECT * FROM activities WHERE sid=? AND date=? AND rid IN (SELECT rid FROM routes "
                            + "WHERE distance > ?) ORDER BY duration_time " + orderBy;
                    pstmt = conn.prepareStatement(sql);

                    pstmt.setInt(1, Integer.parseInt(sid));
                    pstmt.setDate(2, Date.valueOf(date));
                    pstmt.setDouble(3, Double.parseDouble(minDistance));

                    ResultSet resultSet = pstmt.executeQuery();
                    conn.close();
                    return executeActivitiesResult(resultSet, commandRequest.getHeaders());
                } else if (minDistance == null) {
                    wrongParameters += checkRid(rid);
                    wrongParameters += checkDate(date);
                    if (!wrongParameters.equals("")) {
                        conn.close();
                        return new WrongParametersResult(wrongParameters);
                    }

                    String sql = "SELECT * FROM activities WHERE sid=? AND date=? AND rid=? "
                            + "ORDER BY duration_time " + orderBy;
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, Integer.parseInt(sid));
                    pstmt.setDate(2, Date.valueOf(date));
                    pstmt.setInt(3, Integer.parseInt(rid));

                    ResultSet resultSet = pstmt.executeQuery();
                    conn.close();
                    return executeActivitiesResult(resultSet, commandRequest.getHeaders());
                }
            } else if (parameters.size() == PARAMETERS_WITH_1_OPTIONAL) {
                if (date != null) {
                    if (!wrongParameters.equals("")) {
                        conn.close();
                        return new WrongParametersResult(wrongParameters);
                    }

                    String sql = "SELECT * FROM activities WHERE sid=? AND date=? ORDER BY duration_time " + orderBy;
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, Integer.parseInt(sid));
                    pstmt.setDate(2, Date.valueOf(date));

                    ResultSet resultSet = pstmt.executeQuery();
                    conn.close();
                    return executeActivitiesResult(resultSet, commandRequest.getHeaders());
                } else if (rid != null) {
                    wrongParameters += checkRid(rid);
                    if (!wrongParameters.equals("")) {
                        conn.close();
                        return new WrongParametersResult(wrongParameters);
                    }

                    String sql = "SELECT * FROM activities WHERE sid=? AND rid=? ORDER BY duration_time " + orderBy;
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, Integer.parseInt(sid));
                    pstmt.setInt(2, Integer.parseInt(rid));

                    ResultSet resultSet = pstmt.executeQuery();
                    conn.close();
                    return executeActivitiesResult(resultSet, commandRequest.getHeaders());
                } else if (minDistance != null) {
                    wrongParameters += checkDistance(minDistance);
                    if (!wrongParameters.equals("")) {
                        conn.close();
                        return new WrongParametersResult(wrongParameters);
                    }

                    String sql = "SELECT * FROM activities WHERE sid=? AND rid IN (SELECT rid FROM routes "
                            + "WHERE distance>?) AND  ORDER BY duration_time " + orderBy;
                    pstmt = conn.prepareStatement(sql);

                    pstmt.setInt(1, Integer.parseInt(sid));
                    pstmt.setInt(2, Integer.parseInt(minDistance));
                    ResultSet resultSet = pstmt.executeQuery();

                    conn.close();
                    return executeActivitiesResult(resultSet, commandRequest.getHeaders());
                }
            } else if (parameters.size() == MIN_AMOUNT_OF_PARAMETERS) {
                if (!wrongParameters.equals("")) {
                    conn.close();
                    return new WrongParametersResult(wrongParameters);
                }

                String sql = "SELECT * FROM activities WHERE sid=? ORDER BY duration_time " + orderBy;
                pstmt = conn.prepareStatement(sql);

                pstmt.setInt(1, Integer.parseInt(sid));
                ResultSet resultSet = pstmt.executeQuery();

                conn.close();
                return executeActivitiesResult(resultSet, commandRequest.getHeaders());
            }
            conn.close();
            return new WrongParametersResult(wrongParameters);
        } finally {
            conn.close();
        }
    }

    private CommandResult executeActivitiesResult(ResultSet resultSet, Headers headers) throws SQLException {
        int aid;
        int sid;
        int rid;
        int uid;
        Date date;
        Time durationTime;
        Activity activity;
        ArrayList<Activity> activities = new ArrayList<>();

        while (resultSet.next()) {
            aid = resultSet.getInt("aid");
            date = resultSet.getDate("date");
            durationTime = resultSet.getTime("duration_time");
            sid = resultSet.getInt("sid");
            uid = resultSet.getInt("uid");
            rid = resultSet.getInt("rid");
            activity = new Activity(aid, date, durationTime, sid, uid, rid);
            activities.add(activity);
        }
        if (activities.size() == 0) {
            return new WrongParametersResult();
        } else {
            return new GetActivitiesResult(activities, headers);
        }
    }

    private String checkParametersWithoutRidAndDate(String sid, String orderBy) {

        String wrongParameters = "";
        int sidInt;
        try {
            sidInt = Integer.parseInt(sid);
        } catch (NumberFormatException e) {
            return wrongParameters + "sid ";
        }
        if (sid == null || sidInt < 1) {
            wrongParameters += "sid ";
        }

        if (!orderBy.toLowerCase(Locale.ENGLISH).equals("asc")
                && !orderBy.toLowerCase(Locale.ENGLISH).equals("desc")) {
            wrongParameters += " order by";
        }
        return wrongParameters;
    }

    private String checkDate(String date) {

        String wrongParameters = "";
        try {
            Date.valueOf(date);
        } catch (IllegalArgumentException e) {
            return wrongParameters + " date";
        }
        if (date == null) {
            wrongParameters += " date";
        }
        return wrongParameters;
    }

    private String checkRid(String rid) {
        String wrongParameters = "";
        int ridInt;
        try {
            ridInt = Integer.parseInt(rid);
        } catch (NumberFormatException e) {
            return wrongParameters + "rid ";
        }
        if (rid == null || ridInt < 1) {
            wrongParameters += "rid ";
        }
        return wrongParameters;
    }

    private String checkDistance(String distance) {
        String wrongParameters = "";
        double distanceDouble;
        try {
            distanceDouble = Double.parseDouble(distance);
        } catch (NumberFormatException e) {
            return wrongParameters + "distance ";
        }
        if (distance == null || distanceDouble < 0) {
            wrongParameters += " distance";
        }

        return wrongParameters;
    }
}