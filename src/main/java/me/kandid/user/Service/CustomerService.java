package me.kandid.user.Service;

import me.kandid.user.Model.Customer.Customer;
import me.kandid.user.Model.Customer.CustomerAddress;
import me.kandid.user.Model.Customer.CustomerOrder;
import me.kandid.user.Model.Product.Types.CartProduct;
import me.kandid.user.Model.Product.Types.Product;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface CustomerService {
    Customer getCustomer(long phone);

    String sendOTP(String phone) throws IOException, InterruptedException;

    long verifyOTP(String id, String code) throws Exception;

    boolean customerCreate(long phone);

    Customer saveCustomer(Customer customer);

    Customer updateCustomer(Customer customer);

    CustomerAddress getCustomerAddress(long id);

    List<CustomerAddress> getAllCustomerAddress(long customerPhone);

    CustomerAddress addCustomerAddress(CustomerAddress customerAddress, long customerPhone);

    CustomerAddress updateCustomerAddress(CustomerAddress customerAddress, long customerPhone);

    void deleteCustomerAddress(CustomerAddress customerAddress, long customerPhone);

    Set<Product> getCustomerWishlist(long customerPhone);

    void addToCustomerWishlist(long customerPhone, String productCode);

    void removeFromCustomerWishlist(long customerPhone, String productCode);

    List<CustomerOrder> getAllCustomerOrders(long customerPhone);

    CustomerOrder getCustomerOrderById(long id, long customerPhone);

    List<CartProduct> getCustomerCart(long customerPhone);

    List<CartProduct> addToCustomerCart(long customerPhone, CartProduct cartItem);

    List<CartProduct> editCustomerCart(long customerPhone, CartProduct cartItem);

    List<CartProduct> removeFromCustomerCart(long customerPhone, CartProduct cartItem);
}
