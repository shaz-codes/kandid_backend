package me.kandid.user.Service;

import me.kandid.user.Model.Product.Types.OrderProduct;

import java.util.List;

public interface OrderItemsService {

    List<OrderProduct> getNumberOfOrderItemsWithSku(String sku);


}
