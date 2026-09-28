package service;

import dao.ProductDAO;
import dao.TransactionDAO;
import exception.DuplicateSkuException;
import exception.InsufficientStockException;
import exception.ProductNotFoundException;
import model.Product;
import model.StockInTransaction;
import model.StockOutTransaction;
import model.Transaction;
import util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryService {

    private final ProductDAO productDAO;
    private final TransactionDAO transactionDAO;

    public InventoryService() {
        productDAO = new ProductDAO();
        transactionDAO = new TransactionDAO();
    }

    public void addProduct(Product product)
            throws SQLException, DuplicateSkuException {

        validateProduct(product);

        if (productDAO.skuExists(product.getSku())) {
            throw new DuplicateSkuException(
                    "SKU already exists."
            );
        }

        productDAO.addProduct(product);
    }

    public List<Product> getAllProducts()
            throws SQLException {

        List<Product> products =
                new ArrayList<>(
                        productDAO.getAllProducts()
                );

        products.sort(
                Comparator.comparing(
                        Product::getName,
                        String.CASE_INSENSITIVE_ORDER
                )
        );

        return products;
    }

    public List<Product> searchProducts(
            String keyword)
            throws SQLException {

        return productDAO.search(keyword);
    }

    public Product findProduct(int id)
            throws SQLException,
            ProductNotFoundException {

        Product product =
                productDAO.findById(id);

        if (product == null) {

            throw new ProductNotFoundException(
                    "Product not found."
            );
        }

        return product;
    }

    public void updateProduct(Product product)
            throws SQLException,
            ProductNotFoundException,
            DuplicateSkuException {

        findProduct(product.getId());

        if (productDAO.skuExists(product.getSku())) {
            throw new DuplicateSkuException(
                    "SKU already exists."
            );
        }

        validateProduct(product);

        productDAO.updateProduct(product);
    }

    public void deleteProduct(int id)
            throws SQLException,
            ProductNotFoundException {

        findProduct(id);

        productDAO.deleteProduct(id);
    }

    public void stockIn(
            int productId,
            int quantity)
            throws SQLException,
            ProductNotFoundException {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }

        try (Connection connection =
                     DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                Product product =
                        productDAO.findById(
                                connection,
                                productId
                        );

                if (product == null) {
                    throw new ProductNotFoundException(
                            "Product not found."
                    );
                }

                productDAO.increaseQuantity(
                        connection,
                        productId,
                        quantity
                );

                transactionDAO.addTransaction(
                        connection,
                        new StockInTransaction(
                                productId,
                                quantity
                        )
                );

                connection.commit();

            } catch (Exception e) {

                connection.rollback();

                if (e instanceof SQLException) {
                    throw (SQLException) e;
                }

                if (e instanceof ProductNotFoundException) {
                    throw (ProductNotFoundException) e;
                }

                throw new RuntimeException(e);
            }
        }
    }

    public void stockOut(
            int productId,
            int quantity)
            throws SQLException,
            ProductNotFoundException,
            InsufficientStockException {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }

        try (Connection connection =
                     DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                Product product =
                        productDAO.findById(
                                connection,
                                productId
                        );

                if (product == null) {

                    throw new ProductNotFoundException(
                            "Product not found."
                    );
                }

                if (product.getQuantity() < quantity) {

                    throw new InsufficientStockException(
                            "Insufficient stock. Current stock: "
                                    + product.getQuantity()
                    );
                }

                productDAO.decreaseQuantity(
                        connection,
                        productId,
                        quantity
                );

                transactionDAO.addTransaction(
                        connection,
                        new StockOutTransaction(
                                productId,
                                quantity
                        )
                );

                connection.commit();

            } catch (Exception e) {

                connection.rollback();

                if (e instanceof SQLException) {
                    throw (SQLException) e;
                }

                if (e instanceof ProductNotFoundException) {
                    throw (ProductNotFoundException) e;
                }

                if (e instanceof InsufficientStockException) {
                    throw (InsufficientStockException) e;
                }

                throw new RuntimeException(e);
            }
        }
    }

    public List<Product> getLowStockProducts()
            throws SQLException {

        List<Product> result =
                new ArrayList<>();

        for (Product product : getAllProducts()) {

            if (product.getQuantity()
                    < product.getReorderThreshold()) {

                result.add(product);
            }
        }

        return result;
    }

    public Map<String, Double>
    getStockValueByCategory()
            throws SQLException {

        Map<String, Double> result =
                new HashMap<>();

        for (Product product : getAllProducts()) {

            result.merge(
                    product.getCategory(),
                    product.calculateStockValue(),
                    Double::sum
            );
        }

        return result;
    }

    public List<Transaction> getTransactions()
            throws SQLException {

        return transactionDAO.getAllTransactions();
    }

    private void validateProduct(Product product) {

        if (product.getSku() == null ||
                product.getSku().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "SKU cannot be empty."
            );
        }

        if (product.getName() == null ||
                product.getName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Name cannot be empty."
            );
        }

        if (product.getCategory() == null ||
                product.getCategory().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Category cannot be empty."
            );
        }

        if (product.getQuantity() < 0 ||
                product.getUnitPrice() < 0 ||
                product.getReorderThreshold() < 0) {

            throw new IllegalArgumentException(
                    "Numeric values cannot be negative."
            );
        }
    }
}