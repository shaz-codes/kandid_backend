package me.kandid.user.Exceptions;

public class CustomerNotFound extends RuntimeException {
    public CustomerNotFound(long customerPhone) {
        super("Customer not found with id: " + customerPhone);
    }
}
