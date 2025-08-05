package me.kandid.user.Service;

import me.kandid.user.Model.Customer.CustomerOrder;

public interface PayUService {

    String checkOut(CustomerOrder order, String surl, String furl) throws Exception;
}
