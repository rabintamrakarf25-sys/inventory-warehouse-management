package model;

import java.time.LocalDateTime;

public class StockInTransaction extends Transaction {

    public StockInTransaction(
            int productId,
            int quantity) {

        super(productId, quantity);
    }

    public StockInTransaction(
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
        return "IN";
    }
}