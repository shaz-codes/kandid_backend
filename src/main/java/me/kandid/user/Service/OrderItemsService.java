package me.kandid.user.Service;

import me.kandid.user.Model.Customer.OrderItem;

import java.util.List;

public interface OrderItemsService {

    List<OrderItem> getNumberOfOrderItemsWithSku(String sku);


}
