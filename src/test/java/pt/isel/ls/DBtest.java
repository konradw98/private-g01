package pt.isel.ls;

import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class DBtest {

    @Test
    public void connectionTest() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");
        Connection conn = dataSource.getConnection();

        assertNotNull(conn);
    }

    @Test(expected = SQLException.class)
    public void failConnectionTest() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("wrongPassword");
        dataSource.setUser("postgres");
        dataSource.getConnection();
    }

    @Test
    public void deleteTest() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");
        Connection conn = dataSource.getConnection();

        //deleting student that does not exist in table yet (value 0 means no rows were affected)
        String sql = "DELETE FROM students WHERE number = 1111";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        assertEquals(0, pstmt.executeUpdate());

        String sql1 = "INSERT INTO students VALUES(1111,'Jan',1)";
        pstmt = conn.prepareStatement(sql1);
        pstmt.executeUpdate();

        //deleting student that exists (value 1) and does not exist anymore (value 0)
        String sql2 = "DELETE FROM students WHERE number = 1111";
        pstmt = conn.prepareStatement(sql2);
        assertEquals(1, pstmt.executeUpdate());
        assertEquals(0, pstmt.executeUpdate());
    }

    @Test
    public void insertTest() throws SQLException {

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");
        Connection conn = dataSource.getConnection();

        //counting the students before any actions
        String sql = "SELECT COUNT (*) AS total FROM students";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int numOfStudentsBefore = rs.getInt("total");

        String sql1 = "DELETE FROM students WHERE number = 12345";
        pstmt = conn.prepareStatement(sql1);
        pstmt.executeUpdate();

        //counting students after deleting one
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        rs.next();
        int numOfStudentsAfter = rs.getInt("total");

        String sql2 = "INSERT INTO students VALUES(12345, 'Alice', 1)";
        pstmt = conn.prepareStatement(sql2);
        pstmt.executeUpdate();

        rs.close();

        assertEquals(numOfStudentsBefore, numOfStudentsAfter + 1);
    }

    @Test
    public void updateTest() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://127.0.0.1:5432/test");
        dataSource.setPassword("password");
        dataSource.setUser("postgres");
        Connection conn = dataSource.getConnection();

        //checking is students with this number exists
        String sql = "SELECT * FROM students WHERE number = 0 OR name = 'Milena'";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        assertFalse(rs.next());

        String sql1 = "UPDATE students SET number = 0, name = 'Milena' WHERE number = 12345";
        pstmt = conn.prepareStatement(sql1);
        pstmt.executeUpdate();

        String sql2 = "SELECT * FROM students WHERE number = 0";
        pstmt = conn.prepareStatement(sql2);
        rs = pstmt.executeQuery();
        assertTrue(rs.next());

        String sql3 = "SELECT * FROM students WHERE name = 'Milena'";
        pstmt = conn.prepareStatement(sql3);
        rs = pstmt.executeQuery();
        assertTrue(rs.next());

        String sql4 = "UPDATE students SET number = 12345, name = 'Alice' WHERE number = 0";
        pstmt = conn.prepareStatement(sql4);
        pstmt.executeUpdate();

        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        assertFalse(rs.next());

        String sql5 = "SELECT * FROM students WHERE number = 12345 AND name = 'Alice'";
        pstmt = conn.prepareStatement(sql5);
        rs = pstmt.executeQuery();
        assertTrue(rs.next());
    }
}