package lt.mindaugas.customer_app;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerService {
    private DatabaseConnector databaseConnector;
    private String dbName = "first_database";

    public CustomerService() {
        this.databaseConnector = new DatabaseConnector();
    }

    public List<Customer> getAllCustomers() {
        String sqlQuery = "SELECT * FROM customers";
        List<Customer> listOfCustomers = new ArrayList<>();

        try (
                Connection connection = databaseConnector.getConnection("first_database");
                PreparedStatement statement = connection.prepareStatement(sqlQuery);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                listOfCustomers.add(
                        new Customer(
                                resultSet.getInt("customerNumber"),
                                resultSet.getString("customerName"),
                                resultSet.getString("contactLastName"),
                                resultSet.getString("contactFirstName"),
                                resultSet.getString("phone"),
                                resultSet.getString("addressLine1"),
                                resultSet.getString("city"),
                                resultSet.getString("country")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listOfCustomers;
    }

    public Customer getCustomerById(int customerId) {
        String sqlQuery = "SELECT * FROM customers WHERE customerNumber = ?";
        Customer customer = new Customer();
        try (
                Connection connection = databaseConnector.getConnection(dbName);
                PreparedStatement statement = connection.prepareStatement(sqlQuery);
        ) {
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                customer = new Customer(
                        resultSet.getInt("customerNumber"),
                        resultSet.getString("customerName"),
                        resultSet.getString("contactLastName"),
                        resultSet.getString("contactFirstName"),
                        resultSet.getString("phone"),
                        resultSet.getString("addressLine1"),
                        resultSet.getString("city"),
                        resultSet.getString("country")
                );
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    public int deleteCustomerById(int customerId) {
        String sqlQuery = "DELETE FROM customers WHERE customerNumber = ?";

        try (
                Connection connection = databaseConnector.getConnection(dbName);
                PreparedStatement statement = connection.prepareStatement(sqlQuery);
        ) {
            statement.setInt(1, customerId);
            return statement.executeUpdate();

        } catch (SQLException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return 0;
    }

    public void createTable(String tableName, Map<String, String> columnDefinitions){

//        try {
//            Connection connection = databaseConnector.getConnection(dbName);
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SHOW TABLES");
//
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

        try (
                Connection connection = databaseConnector.getConnection(dbName);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SHOW TABLES");
        ) {
            boolean tableExist = false;

            while (resultSet.next()) {
                String existingTableName = resultSet.getString(1);
                if (tableName.equals(existingTableName)) {
                    tableExist = true;
                    break;
                }
            }

            if (!tableExist) {
                String sqlQuery = buildCreateTableQuery(tableName, columnDefinitions);
                statement.executeUpdate(sqlQuery);
                System.out.println("Created new table: " + tableName);

            } else {
                System.out.printf("Table %s already exist%n", tableName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

//    CREATE TABLE %s (
//            id INT PRIMARY KEY AUTO_INCREMENT,
//            company_name VARCHAR(255),
//    contact_first_name VARCHAR(255),
//    contact_last_name VARCHAR(255)
//                        )

    private String buildCreateTableQuery(String tableName, Map<String, String> columns) {
        StringBuilder query = new StringBuilder("CREATE TABLE %s (".formatted(tableName));
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            query.append(entry.getKey()).append(' ').append(entry.getValue()).append(',');
        }
        query.deleteCharAt(query.length() -1).append(')');
        return query.toString();
    }


}
