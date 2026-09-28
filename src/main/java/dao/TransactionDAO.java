package dao;

import model.StockInTransaction;
import model.StockOutTransaction;
import model.Transaction;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public void addTransaction(
            Connection connection,
            Transaction transaction)
            throws SQLException {

        String sql =
                "INSERT INTO transactions " +
                        "(product_id, transaction_type, quantity) " +
                        "VALUES (?, ?, ?)";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    transaction.getProductId()
            );

            statement.setString(
                    2,
                    transaction.getTransactionType()
            );

            statement.setInt(
                    3,
                    transaction.getQuantity()
            );

            statement.executeUpdate();
        }
    }

    public List<Transaction> getAllTransactions()
            throws SQLException {

        String sql =
                "SELECT * FROM transactions " +
                        "ORDER BY created_at DESC";

        List<Transaction> transactions =
                new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                int id =
                        resultSet.getInt("id");

                int productId =
                        resultSet.getInt("product_id");

                String type =
                        resultSet.getString(
                                "transaction_type"
                        );

                int quantity =
                        resultSet.getInt(
                                "quantity"
                        );

                Timestamp timestamp =
                        resultSet.getTimestamp(
                                "created_at"
                        );

                if (type.equals("IN")) {

                    transactions.add(
                            new StockInTransaction(
                                    id,
                                    productId,
                                    quantity,
                                    timestamp.toLocalDateTime()
                            )
                    );

                } else {

                    transactions.add(
                            new StockOutTransaction(
                                    id,
                                    productId,
                                    quantity,
                                    timestamp.toLocalDateTime()
                            )
                    );
                }
            }
        }

        return transactions;
    }
}