package main;

import exception.DuplicateSkuException;
import exception.InsufficientStockException;
import exception.ProductNotFoundException;
import model.Product;
import model.Transaction;
import service.InventoryService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final InventoryService service =
            new InventoryService();

    public static void main(String[] args) {

        while (true) {

            showMenu();

            int choice =
                    readInt("Enter choice: ");

            try {

                switch (choice) {

                    case 1:
                        addProduct();
                        break;

                    case 2:
                        viewProducts();
                        break;

                    case 3:
                        searchProducts();
                        break;

                    case 4:
                        updateProduct();
                        break;

                    case 5:
                        deleteProduct();
                        break;

                    case 6:
                        stockIn();
                        break;

                    case 7:
                        stockOut();
                        break;

                    case 8:
                        lowStock();
                        break;

                    case 9:
                        categoryReport();
                        break;

                    case 10:
                        transactionHistory();
                        break;

                    case 0:
                        System.out.println(
                                "Goodbye!"
                        );
                        return;

                    default:
                        System.out.println(
                                "Invalid choice."
                        );
                }

            } catch (SQLException |
                     ProductNotFoundException |
                     DuplicateSkuException |
                     InsufficientStockException e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "Input Error: " +
                                e.getMessage()
                );
            }
        }
    }

    private static void showMenu() {

        System.out.println();
        System.out.println(
                "=========================================="
        );
        System.out.println(
                "   INVENTORY / WAREHOUSE MANAGEMENT"
        );
        System.out.println(
                "=========================================="
        );
        System.out.println("1. Add Product");
        System.out.println("2. View All Products");
        System.out.println("3. Search Product");
        System.out.println("4. Update Product");
        System.out.println("5. Delete Product");
        System.out.println("6. Stock In");
        System.out.println("7. Stock Out");
        System.out.println("8. Low Stock Alert");
        System.out.println("9. Stock Value by Category");
        System.out.println("10. Transaction History");
        System.out.println("0. Exit");
    }

    private static void addProduct()
            throws SQLException,
            DuplicateSkuException {

        String sku =
                readString("SKU: ");

        String name =
                readString("Name: ");

        String category =
                readString("Category: ");

        int quantity =
                readInt("Quantity: ");

        double price =
                readDouble("Unit Price: ");

        int reorder =
                readInt("Reorder Threshold: ");

        Product product =
                new Product(
                        sku,
                        name,
                        category,
                        quantity,
                        price,
                        reorder
                );

        service.addProduct(product);

        System.out.println(
                "Product added successfully."
        );
    }

    private static void viewProducts()
            throws SQLException {

        List<Product> products =
                service.getAllProducts();

        if (products.isEmpty()) {
            System.out.println(
                    "No products found."
            );
            return;
        }

        for (Product product : products) {
            printProduct(product);
        }
    }

    private static void searchProducts()
            throws SQLException {

        String keyword =
                readString("Search: ");

        List<Product> products =
                service.searchProducts(keyword);

        if (products.isEmpty()) {
            System.out.println(
                    "No products found."
            );
            return;
        }

        for (Product product : products) {
            printProduct(product);
        }
    }

    private static void updateProduct()
            throws SQLException,
            ProductNotFoundException,
            DuplicateSkuException {

        int id =
                readInt("Product ID: ");

        Product product =
                service.findProduct(id);

        product.setSku(
                readString("New SKU: ")
        );

        product.setName(
                readString("New Name: ")
        );

        product.setCategory(
                readString("New Category: ")
        );

        product.setQuantity(
                readInt("New Quantity: ")
        );

        product.setUnitPrice(
                readDouble("New Unit Price: ")
        );

        product.setReorderThreshold(
                readInt(
                        "New Reorder Threshold: "
                )
        );

        service.updateProduct(product);

        System.out.println(
                "Product updated successfully."
        );
    }

    private static void deleteProduct()
            throws SQLException,
            ProductNotFoundException {

        int id =
                readInt("Product ID: ");

        service.deleteProduct(id);

        System.out.println(
                "Product deleted successfully."
        );
    }

    private static void stockIn()
            throws SQLException,
            ProductNotFoundException {

        int productId =
                readInt("Product ID: ");

        int quantity =
                readInt("Quantity: ");

        service.stockIn(
                productId,
                quantity
        );

        System.out.println(
                "Stock-in successful."
        );
    }

    private static void stockOut()
            throws SQLException,
            ProductNotFoundException,
            InsufficientStockException {

        int productId =
                readInt("Product ID: ");

        int quantity =
                readInt("Quantity: ");

        service.stockOut(
                productId,
                quantity
        );

        System.out.println(
                "Stock-out successful."
        );
    }

    private static void lowStock()
            throws SQLException {

        List<Product> products =
                service.getLowStockProducts();

        if (products.isEmpty()) {

            System.out.println(
                    "No low-stock products."
            );

            return;
        }

        System.out.println(
                "LOW STOCK PRODUCTS"
        );

        for (Product product : products) {
            printProduct(product);
        }
    }

    private static void categoryReport()
            throws SQLException {

        Map<String, Double> report =
                service.getStockValueByCategory();

        System.out.println(
                "STOCK VALUE BY CATEGORY"
        );

        for (Map.Entry<String, Double> entry :
                report.entrySet()) {

            System.out.printf(
                    "%-20s %.2f%n",
                    entry.getKey(),
                    entry.getValue()
            );
        }
    }

    private static void transactionHistory()
            throws SQLException {

        List<Transaction> transactions =
                service.getTransactions();

        if (transactions.isEmpty()) {

            System.out.println(
                    "No transactions found."
            );

            return;
        }

        for (Transaction transaction :
                transactions) {

            System.out.println(
                    "ID: " + transaction.getId() +
                            " | Product: " +
                            transaction.getProductId() +
                            " | Type: " +
                            transaction.getTransactionType() +
                            " | Quantity: " +
                            transaction.getQuantity() +
                            " | Date: " +
                            transaction.getTransactionDate()
            );
        }
    }

    private static void printProduct(
            Product product) {

        System.out.printf(
                "ID=%d | SKU=%s | Name=%s | Category=%s | Qty=%d | Price=%.2f | Reorder=%d%n",
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getCategory(),
                product.getQuantity(),
                product.getUnitPrice(),
                product.getReorderThreshold()
        );
    }

    private static String readString(
            String message) {

        while (true) {

            System.out.print(message);

            String value =
                    scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println(
                    "Input cannot be empty."
            );
        }
    }

    private static int readInt(
            String message) {

        while (true) {

            System.out.print(message);

            try {

                return Integer.parseInt(
                        scanner.nextLine().trim()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Enter a valid integer."
                );
            }
        }
    }

    private static double readDouble(
            String message) {

        while (true) {

            System.out.print(message);

            try {

                return Double.parseDouble(
                        scanner.nextLine().trim()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Enter a valid number."
                );
            }
        }
    }
}