package me.kandid.user.Repository.Customer;

import me.kandid.user.Model.Customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Customer getCustomerByPhone(long phone);

    Customer getCustomerByEmail(String email);

    boolean existsByPhone(long phone);
}
