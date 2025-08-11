package me.kandid.user.Service;

import me.kandid.user.Model.Requests.OrderRequest;

import java.io.IOException;
import java.net.URL;

public interface OrderService {
    URL checkout_prepaid(long customerPhone, OrderRequest orderRequest) throws IOException;

    URL checkout_confirmed(long customerPhone, long orderId) throws IOException;

    URL checkout_cod(long customerPhone, OrderRequest orderRequest) throws IOException;
}
