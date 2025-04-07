package me.kandid.user.Model.Customer;

import jakarta.persistence.*;
import lombok.Data;
import me.kandid.user.Model.Employee.DeliveryPartner;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Entity(name = "customer_orders")
@Data
public class CustomerOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long customerPhone;
    @CreationTimestamp
    @Column(updatable = false)
    private Instant created;
    @UpdateTimestamp
    private Instant modified;
    @ManyToMany
    private List<OrderItems> items;
    @OneToOne
    @JoinColumn(name = "delivery_partner")
    private DeliveryPartner deliveryPartner;
    @OneToOne
    @JoinColumn(name = "customer")
    private Customer customer;
    @OneToOne
    @JoinColumn(name = "address")
    private CustomerAddress customerAddress;
    private String status;
    private String paymentMethod;
    private String paymentStatus;
    // TODO change strings to enums
    private String comment;
    private long billAmountInPaise;
    private Boolean isReturned;
    private Boolean isDelivered;
    private Boolean isCancelled;
}
