package me.kandid.user.Service;

import me.kandid.user.Model.Customer.OrderItems;

import java.util.List;

public interface OrderItemsService {

    List<OrderItems> getNumberOfOrderItemsWithSku(String sku);


}
