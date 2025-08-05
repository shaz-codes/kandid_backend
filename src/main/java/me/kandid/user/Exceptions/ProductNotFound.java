package me.kandid.user.Exceptions;

public class ProductNotFound extends RuntimeException {
    public ProductNotFound(String message) {
        super(message + " Not found");
    }
}
