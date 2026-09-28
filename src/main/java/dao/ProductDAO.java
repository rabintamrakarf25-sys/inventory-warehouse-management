package dao;

import model.Product;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public void addProduct(Product product)
            throws SQLException {

        String sql =
                "INSERT INTO products " +
                        "(sku, name, category, quantity, unit_price, reorder_threshold) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, product.getSku());
            statement.setString(2, product.getName());
            statement.setString(3, product.getCategory());
            statement.setInt(4, product.getQuantity());
            statement.setDouble(5, product.getUnitPrice());
            statement.setInt(6, product.getReorderThreshold());

            statement.executeUpdate();
        }
    }

    public List<Product> getAllProducts()
            throws SQLException {

        String sql =
                "SELECT * FROM products ORDER BY name";

        List<Product> products =
                new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {
                products.add(mapRow(resultSet));
            }
        }

        return products;
    }

    public Product findById(int id)
            throws SQLException {

        try (Connection connection =
                     DBConnection.getConnection()) {

            return findById(connection, id);
        }
    }

    public Product findById(
            Connection connection,
            int id)
            throws SQLException {

        String sql =
                "SELECT * FROM products WHERE id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }

        return null;
    }

    public List<Product> search(
            String keyword)
            throws SQLException {

        String sql =
                "SELECT * FROM products " +
                        "WHERE name LIKE ? " +
                        "OR sku LIKE ? " +
                        "OR category LIKE ? " +
                        "ORDER BY name";

        List<Product> products =
                new ArrayList<>();

        String searchValue =
                "%" + keyword + "%";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, searchValue);
            statement.setString(2, searchValue);
            statement.setString(3, searchValue);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    products.add(mapRow(resultSet));
                }
            }
        }

        return products;
    }

    public void updateProduct(Product product)
            throws SQLException {

        String sql =
                "UPDATE products SET " +
                        "sku = ?, name = ?, category = ?, " +
                        "quantity = ?, unit_price = ?, " +
                        "reorder_threshold = ? " +
                        "WHERE id = ?";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, product.getSku());
            statement.setString(2, product.getName());
            statement.setString(3, product.getCategory());
            statement.setInt(4, product.getQuantity());
            statement.setDouble(5, product.getUnitPrice());
            statement.setInt(6, product.getReorderThreshold());
            statement.setInt(7, product.getId());

            statement.executeUpdate();
        }
    }

    public void deleteProduct(int id)
            throws SQLException {

        String sql =
                "DELETE FROM products WHERE id = ?";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public boolean skuExists(String sku)
            throws SQLException {

        String sql =
                "SELECT id FROM products WHERE sku = ?";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, sku);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                return resultSet.next();
            }
        }
    }

    public int increaseQuantity(
            Connection connection,
            int productId,
            int amount)
            throws SQLException {

        String sql =
                "UPDATE products " +
                        "SET quantity = quantity + ? " +
                        "WHERE id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, amount);
            statement.setInt(2, productId);

            return statement.executeUpdate();
        }
    }

    public int decreaseQuantity(
            Connection connection,
            int productId,
            int amount)
            throws SQLException {

        String sql =
                "UPDATE products " +
                        "SET quantity = quantity - ? " +
                        "WHERE id = ? AND quantity >= ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, amount);
            statement.setInt(2, productId);
            statement.setInt(3, amount);

            return statement.executeUpdate();
        }
    }

    private Product mapRow(
            ResultSet resultSet)
            throws SQLException {

        return new Product(
                resultSet.getInt("id"),
                resultSet.getString("sku"),
                resultSet.getString("name"),
                resultSet.getString("category"),
                resultSet.getInt("quantity"),
                resultSet.getDouble("unit_price"),
                resultSet.getInt("reorder_threshold")
        );
    }
}