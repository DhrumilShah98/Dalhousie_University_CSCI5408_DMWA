import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * {@code Transaction} class imitates a Transaction.
 */
public class Transaction implements Runnable {
    // transactionLogFileWriter to write in the log file.
    private final FileWriter transactionLogFileWriter;

    // transaction id.
    private final String transactionId;

    // customer zip code.
    private final int customerZipCode;

    // customer city name to update.
    private final String customerCityNameToUpdate;

    /**
     * Constructs this {@code Transaction}.
     *
     * @param transactionLogFileWriter to write in the log file.
     * @param transactionId            transaction id.
     * @param customerZipCode          customer zip code.
     * @param customerCityNameToUpdate customer city name to update.
     */
    public Transaction(FileWriter transactionLogFileWriter, String transactionId, int customerZipCode, String customerCityNameToUpdate) {
        this.transactionLogFileWriter = transactionLogFileWriter;
        this.transactionId = transactionId;
        this.customerZipCode = customerZipCode;
        this.customerCityNameToUpdate = customerCityNameToUpdate;
    }

    /**
     * Gets the customer city.
     *
     * @param customersResultSet result set of customers.
     * @return customer city if available otherwise null.
     * @throws SQLException if any error occurs.
     */
    public String getCustomersCity(ResultSet customersResultSet) throws SQLException {
        String customerCity = null;
        if (customersResultSet.next()) {
            customerCity = customersResultSet.getString("customer_city");
        }
        if (customerCity == null) {
            customerCity = "NA";
        }
        return customerCity;
    }

    /**
     * Runs this transaction.
     */
    @Override
    public void run() {
        ResultSet customersResultSet = null;
        try (final Connection connection = DriverManager.getConnection(
                DatabaseConstants.PATH,
                DatabaseConstants.USERNAME,
                DatabaseConstants.PASSWORD);
             final Statement statement = connection.createStatement()) {

            // Small delay for transaction 3.
            if (transactionId.startsWith("T3")) {
                Thread.sleep(20);
            }

            // Set auto commit to false.
            connection.setAutoCommit(false);
            statement.execute("START TRANSACTION;");

            // Transaction started.
            System.out.println(transactionId + " started");

            // SELECT query.
            final String readCustomerQuery = DatabaseConstants.readCustomersDataQuery(customerZipCode);
            customersResultSet = statement.executeQuery(readCustomerQuery);
            final String customerCity = getCustomersCity(customersResultSet);
            transactionLogFileWriter.append(String.format("%16s%12s%28s%20s%12s%12s%25s\n", transactionId, "SELECT", "olist_customers_dataset", "customer_city", "-", customerCity, new SimpleDateFormat("dd MMM yyyy HH:mm:ss.SSS").format(new Date())));

            // UPDATE query.
            final String updateCustomerQuery = DatabaseConstants.updateCustomersCityQuery(customerCityNameToUpdate, customerZipCode);
            statement.executeUpdate(updateCustomerQuery);
            transactionLogFileWriter.append(String.format("%16s%12s%28s%20s%12s%12s%25s\n", transactionId, "UPDATE", "olist_customers_dataset", "customer_city", customerCity, customerCityNameToUpdate, new SimpleDateFormat("dd MMM yyyy HH:mm:ss.SSS").format(new Date())));

            // COMMIT this transaction.
            connection.commit();

            // Transaction completed.
            System.out.println(transactionId + " completed");

            customersResultSet.close();
        } catch (SQLException | IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println(transactionId + ": " + e.getMessage());
            try {
                if (customersResultSet != null) {
                    customersResultSet.close();
                }
            } catch (SQLException e1) {
                e.printStackTrace();
                System.out.println(transactionId + ": " + e1.getMessage());
            }
        }
    }

    /**
     * {@code DatabaseConstants} holds constants and queries.
     */
    private static final class DatabaseConstants {
        private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        private static final String PATH = "jdbc:mysql://34.93.146.180:3306/data5408_p1_t2";
        private static final String USERNAME = "dhrumilshahvm1";
        private static final String PASSWORD = "dhrumil@vm1";

        private static String readCustomersDataQuery(int customerZipCode) {
            return "SELECT customer_city " +
                    "FROM olist_customers_dataset " +
                    "WHERE customer_zip_code_prefix = " + customerZipCode + ";";
        }

        private static String updateCustomersCityQuery(String customerUpdateCity, int customerZipCode) {
            return "UPDATE olist_customers_dataset" +
                    " SET customer_city=\"" + customerUpdateCity + "\"" +
                    " WHERE customer_zip_code_prefix = " + customerZipCode + ";";
        }
    }


    public static void main(String[] args) {
        try {
            Class.forName(DatabaseConstants.JDBC_DRIVER).getDeclaredConstructor().newInstance();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        File transactionFile = new File("transaction_log.txt");
        final boolean transactionFileExists = transactionFile.exists();

        try (FileWriter fileWriter = new FileWriter("transaction_log.txt", true)) {

            if (!transactionFileExists) {
                fileWriter.append(String.format("%16s%12s%28s%20s%12s%12s%25s\n", "TRX NUM", "Operation", "Table", "Attribute", "Before Val", "After Val", "Timestamp"));
            } else {
                fileWriter.append("\n");
            }

            // Create 3 transactions.
            final int customerZipCode = 2951;
            Thread t1 = new Thread(new Transaction(fileWriter, "T1-" + System.currentTimeMillis(), customerZipCode, "T1 city"));
            Thread t2 = new Thread(new Transaction(fileWriter, "T2-" + System.currentTimeMillis(), customerZipCode, "T2 city"));
            Thread t3 = new Thread(new Transaction(fileWriter, "T3-" + System.currentTimeMillis(), customerZipCode, "T3 city"));

            // Least priority given to transaction 3 and highest priority given to transaction 1 and 2.
            t1.setPriority(Thread.MAX_PRIORITY);
            t2.setPriority(Thread.MAX_PRIORITY);
            t3.setPriority(Thread.MIN_PRIORITY);

            // Start 3 transactions concurrently
            t1.start();
            t2.start();
            t3.start();

            // Wait for transactions to complete and then close the transaction_log.txt file.
            t1.join();
            t2.join();
            t3.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}