package lt.mindaugas.customer_app;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {
    private String serverMySqlUrl = "jdbc:mysql://localhost:3333";
    private String user = "root";
    private String password = "root";
    private Connection connection;

    public DatabaseConnector() {
//        getProperties();
    }

    private void getProperties() {
//        C:\tomcat_servers\apache_tomcat_9_0_87\webapps\project_customer_app\WEB-INF\classes
        try (FileInputStream file = new FileInputStream("./WEB-INF/classes/db.properties")) {
//        try (FileInputStream file = new FileInputStream("./src/main/resources/db.properties")) {
            Properties properties = new Properties();
            properties.load(file);
            this.serverMySqlUrl = properties.getProperty("db.server_mysql_url");
            this.user = properties.getProperty("db.user");
            this.password = properties.getProperty("db.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(String dbName) throws SQLException {
        connection = DriverManager.getConnection(serverMySqlUrl + "/" + dbName, user, password);
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
