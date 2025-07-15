package me.kandid.user.Repository.Customer;

import me.kandid.user.Model.Customer.OtpLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpLoginRepository extends JpaRepository<OtpLogin, Integer> {
    OtpLogin findByVerificationId(long verificationId);
}
