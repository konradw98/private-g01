package pt.isel.ls.handlers;

import pt.isel.ls.CommandRequest;
import pt.isel.ls.Parameters;
import pt.isel.ls.commandresults.CommandResult;
import pt.isel.ls.commandresults.PostResult;
import pt.isel.ls.commandresults.WrongParametersResult;

import java.sql.*;

public class PostActivityHandler implements CommandHandler {
    private static final int MAX_AMOUNT_OF_PARAMETERS = 4;

    @Override
    public CommandResult execute(CommandRequest commandRequest) throws SQLException {
        Parameters parameters = commandRequest.getParameters();
        String uid = parameters.get("uid");
        String duration = parameters.get("duration");
        String date = parameters.get("date");
        String paramSid = commandRequest.getPathParameters().get("sid");

        String wrongParameters = checkParametersWithoutRid(paramSid, uid, duration, date);

        String sql;
        PreparedStatement pstmt;
        try (Connection conn = commandRequest.getDataSource().getConnection()) {
            if (commandRequest.getParameters().size() == MAX_AMOUNT_OF_PARAMETERS) {
                String rid = parameters.get("rid");
                wrongParameters += checkRid(rid);

                sql = "INSERT INTO activities(date,duration_time,sid,uid,rid) values(?,?,?,?,?)";
                if (!wrongParameters.equals("")) {
                    return new WrongParametersResult(wrongParameters);
                }

                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(5, Integer.parseInt(rid));
            } else if (!wrongParameters.equals("")) {
                return new WrongParametersResult(wrongParameters);
            } else if (commandRequest.getParameters().size() < MAX_AMOUNT_OF_PARAMETERS) {
                sql = "INSERT INTO activities(date,duration_time,sid,uid) values(?,?,?,?)";
                pstmt = conn.prepareStatement(sql);
            } else {
                return new WrongParametersResult(wrongParameters);
            }

            Date parsedDate;
            Time parsedTime;

            try {
                parsedDate = Date.valueOf(date);
            } catch (IllegalStateException e) {
                return new WrongParametersResult("date");
            }
            try {
                parsedTime = Time.valueOf(duration);
            } catch (IllegalStateException e) {
                return new WrongParametersResult("duration");
            }

            pstmt.setDate(1, parsedDate);
            pstmt.setTime(2, parsedTime);
            pstmt.setInt(3, Integer.parseInt(paramSid));
            pstmt.setInt(4, Integer.parseInt(uid));
            pstmt.executeUpdate();

            String sql1 = "SELECT MAX(aid) FROM activities";
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            ResultSet resultSet = pstmt1.executeQuery();
            conn.close();

            if (resultSet.next()) {
                int aid = resultSet.getInt("max");
                return new PostResult(aid, "aid");
            } else {
                return new WrongParametersResult();
            }

        }
    }

    private String checkParametersWithoutRid(String sid, String uid, String duration, String date) {
        String wrongParameters = "";
        int sidInt;
        try {
            sidInt = Integer.parseInt(sid);
        } catch (NumberFormatException | NullPointerException e) {
            return wrongParameters + "sid ";
        }
        if (sidInt < 1) {
            wrongParameters += "sid ";
        }

        int uidInt;
        try {
            uidInt = Integer.parseInt(uid);
        } catch (NumberFormatException | NullPointerException e) {
            return wrongParameters + "rid ";
        }
        if (uidInt < 1) {
            wrongParameters += "uid ";
        }

        try {
            Time.valueOf(duration);
        } catch (IllegalArgumentException | NullPointerException e) {
            return wrongParameters + "duration ";
        }

        try {
            Date.valueOf(date);
        } catch (IllegalArgumentException | NullPointerException e) {
            return wrongParameters + "date ";
        }

        return wrongParameters;
    }

    private String checkRid(String rid) {
        String wrongParameters = "";
        if (rid == null || Integer.parseInt(rid) < 1) {
            wrongParameters += "rid ";
        }

        return wrongParameters;
    }
}
