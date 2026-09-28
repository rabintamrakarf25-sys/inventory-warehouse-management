package model;

import java.time.LocalDateTime;

public abstract class Transaction {

    private int id;
    private int productId;
    private int quantity;
    private LocalDateTime transactionDate;

    protected Transaction(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
        this.transactionDate = LocalDateTime.now();
    }

    protected Transaction(
            int id,
            int productId,
            int quantity,
            LocalDateTime transactionDate) {

        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.transactionDate = transactionDate;
    }

    public abstract String getTransactionType();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}