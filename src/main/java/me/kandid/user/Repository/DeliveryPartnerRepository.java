package me.kandid.user.Repository;

import me.kandid.user.Model.Employee.DeliveryPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryPartnerRepository extends JpaRepository<DeliveryPartner, Integer> {
    DeliveryPartner getByUsername(String username);
}
