package me.kandid.user.Repository.Customer;

import me.kandid.user.Model.Customer.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {
    List<OrderItems> getAllByProductSku(String productSku);

    long countByProductSku(String productSku);
}
