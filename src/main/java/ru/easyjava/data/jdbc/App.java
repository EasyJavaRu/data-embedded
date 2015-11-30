package ru.easyjava.data.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Simple example of JDBC usage.
 */
public final class App {
    /**
     * Query that create table.
     */
    private static final String CREATE_QUERY =
            "CREATE TABLE EXAMPLE (GREETING VARCHAR(6), TARGET VARCHAR(6))";
    /**
     * Quaery that populates table with data.
     */
    private static final String DATA_QUERY =
            "INSERT INTO EXAMPLE VALUES('Hello','World')";

    /**
     * Do not construct me.
     */
    private App() {
    }

    private static Connection getH2Connection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:mem:");
    }

    private static Connection getHsqldbConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:mem:test");
    }

    private static Connection getDerbyConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:derby:memory:test;create=true");
    }

    private static void greet(Connection db) throws SQLException {
        try (Statement dataQuery = db.createStatement()) {
            dataQuery.execute(CREATE_QUERY);
            dataQuery.execute(DATA_QUERY);
        }

        try (PreparedStatement query =
                     db.prepareStatement("SELECT * FROM EXAMPLE")) {
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                System.out.println(String.format("%s, %s!",
                        rs.getString(1),
                        rs.getString("TARGET")));
            }
            rs.close();
        }
    }

    /**
     * Entry point.
     *
     * @param args Command line args. Not used.
     */
    public static void main(final String[] args) {
        try (Connection db = getH2Connection()) {
            greet(db);
        } catch (SQLException ex) {
            System.out.println("Database connection failure: "
                    + ex.getMessage());
        }

        try (Connection db = getHsqldbConnection()) {
            greet(db);
        } catch (SQLException ex) {
            System.out.println("Database connection failure: "
                    + ex.getMessage());
        }

        try (Connection db = getDerbyConnection()) {
            greet(db);
        } catch (SQLException ex) {
            System.out.println("Database connection failure: "
                    + ex.getMessage());
        }
    }
}
