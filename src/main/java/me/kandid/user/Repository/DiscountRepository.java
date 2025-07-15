package me.kandid.user.Repository;

import me.kandid.user.Model.Product.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Discount findDiscountByDiscountedFromAfterAndDiscountedToBeforeAndProductCode(Instant now, Instant now1, String code);
}
