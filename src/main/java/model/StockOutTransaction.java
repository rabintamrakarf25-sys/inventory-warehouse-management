package model;

import java.time.LocalDateTime;

public class StockOutTransaction extends Transaction {

    public StockOutTransaction(
            int productId,
            int quantity) {

        super(productId, quantity);
    }

    public StockOutTransaction(
            int id,
            int productId,
            int quantity,
            LocalDateTime transactionDate) {

        super(
                id,
                productId,
                quantity,
                transactionDate
        );
    }

    @Override
    public String getTransactionType() {
        return "OUT";
    }
}