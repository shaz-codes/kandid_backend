package me.kandid.user.Repository.Customer;

import me.kandid.user.Model.Customer.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrdersRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> getCustomerOrdersByCustomerPhone(long customerPhone);

    CustomerOrder getCustomerOrderById(long id);

    CustomerOrder getCustomerOrderByIdAndCustomerPhone(long id, long customerPhone);
}
