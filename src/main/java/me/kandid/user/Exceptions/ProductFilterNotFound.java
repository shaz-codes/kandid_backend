package me.kandid.user.Exceptions;

public class ProductFilterNotFound extends RuntimeException {
    public ProductFilterNotFound() {
        super("Product filter not found");
    }
}
