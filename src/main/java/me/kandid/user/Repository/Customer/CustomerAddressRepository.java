package me.kandid.user.Repository.Customer;

import me.kandid.user.Model.Customer.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {
    CustomerAddress getCustomerAddressById(long id);

    List<CustomerAddress> getCustomerAddressesByCustomerPhone(long customerPhone);
}
