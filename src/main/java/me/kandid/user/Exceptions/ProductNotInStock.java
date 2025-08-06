package me.kandid.user.Exceptions;

public class ProductNotInStock extends RuntimeException {
    public ProductNotInStock(String message) {
        super(message + "was not in stock");
    }
}
