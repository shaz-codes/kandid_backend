package me.kandid.user.Service;

import me.kandid.user.Model.Customer.*;

import java.io.IOException;
import java.util.List;

public interface CustomerService {

    String sendOTP(long phone) throws IOException, InterruptedException;

    String verifyOTP(String id, String otp) throws IOException, InterruptedException;

    Customer getCustomer(long id);

    boolean customerExist(long id);

    Customer saveCustomer(Customer customer);

    Customer updateCustomer(Customer customer);

    void deleteCustomer(Customer customer);

    Customer getCustomerByEmail(String email);

    CustomerAddress getCustomerAddress(long id);

    List<CustomerAddress> getAllCustomerAddress(long customerPhone);

    CustomerAddress addCustomerAddress(CustomerAddress customerAddress);

    CustomerAddress updateCustomerAddress(CustomerAddress customerAddress);

    void deleteCustomerAddress(CustomerAddress customerAddress);

    CustomerWishlist getCustomerWishlist(long customerPhone);

    void createCustomerWishlist(long customerPhone);

    CustomerWishlist addToCustomerWishlist(long customerPhone, String productCode);

    CustomerWishlist removeFromCustomerWishlist(long customerPhone, String productCode);

    List<CustomerOrder> getAllCustomerOrders(long customerPhone);

    CustomerOrder getCustomerOrderById(long id, long customerPhone);

    List<CartItems> getCustomerCart(long customerPhone);

    List<CartItems> addToCustomerCart(long customerPhone, CartItems cartItem);

    void removeFromCustomerCart(long customerPhone, CartItems cartItem);
}
