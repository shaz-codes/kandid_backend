package me.kandid.user.Exceptions;

public class WishlistDoesNotExist extends RuntimeException {
    public WishlistDoesNotExist(long phone) {
        super(phone + "'s Wishlist Not Found");
    }
}
