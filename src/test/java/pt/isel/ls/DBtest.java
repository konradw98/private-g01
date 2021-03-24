package pt.isel.ls;

import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class DBtest {

    @Test
    public void insertTest() throws SQLException {

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");
        Connection conn = dataSource.getConnection();

        String sql = "SELECT * FROM students";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        int numOfStudentsBefore=0;
        while (rs.next()) {
            numOfStudentsBefore++;
        }

        String sql1="INSERT INTO students VALUES(1111,'Jan',1)";
        pstmt=conn.prepareStatement(sql1);
        pstmt.executeUpdate();

        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();

        int numOfStudentsAfter=0;
        while (rs.next()) {
            numOfStudentsAfter++;
        }

        String sql2="DELETE FROM students WHERE number=1111";
        pstmt=conn.prepareStatement(sql2);
        pstmt.executeUpdate();

        rs.close();

        assertTrue(numOfStudentsBefore+1==numOfStudentsAfter);
    }

    @Test
    public void deleteTest() throws SQLException{

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");
        Connection conn = dataSource.getConnection();

        String sql = "SELECT * FROM students";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        int numOfStudentsBefore=0;
        while (rs.next()) {
            numOfStudentsBefore++;
        }

        String sql1="DELETE FROM students WHERE number=12345";
        pstmt=conn.prepareStatement(sql1);
        pstmt.executeUpdate();

        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();

        int numOfStudentsAfter=0;
        while (rs.next()) {
            numOfStudentsAfter++;
        }

        String sql2="INSERT INTO students VALUES(12345,'Alice',1)";
        pstmt=conn.prepareStatement(sql2);
        pstmt.executeUpdate();

        rs.close();

        assertTrue(numOfStudentsBefore==numOfStudentsAfter+1);
    }

}
