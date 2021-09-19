package baubolp.ryzerbe.ryzerclans.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private String[] login;
    private Connection con;

    public DatabaseConnection(String url, String username, String password) {
        if (!url.startsWith("jdbc:mysql://")) url = "jdbc:mysql://" + url;
        this.login = new String[]{url, username, password};

        try {
            this.con = createNewConnection(login);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Statement getStatement(){
        try {
            if (!isValidConnection(this.con)) this.con = createNewConnection(this.login);
            return this.con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Connection createNewConnection(String[] login) throws SQLException {
        return DriverManager.getConnection(login[0], login[1], login[2]);
    }

    private boolean isValidConnection(Connection con) {
        try {
            return !con.isClosed() && con.isValid(2000);
        } catch (SQLException var2) {
            return false;
        }
    }


    public void close() {
        if (this.con != null) {
            try {
                this.con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
