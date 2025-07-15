package me.kandid.user.Model.Customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import me.kandid.user.Model.Employee.DeliveryPartner;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

// TODO: change strings to enums for all status
// TODO: add fields to support return, and figure out how to handle it in 

@Entity(name = "customer_orders")
@Data
@Schema(name = "Customer Order", description = "Details about the customer's order")
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "Order ID", description = "Unique identifier for the order", example = "123456789")
    private long id;

    @Column(nullable = false)
    @Schema(name = "Customer Phone", description = "Phone number of the customer who placed the order", example = "9161086557")
    private long customerPhone;

    @CreationTimestamp
    @Column(updatable = false)
    @Schema(name = "Creation Timestamp", description = "Timestamp when the order was created", example = "2023-10-01T12:00:00Z")
    private Instant created;

    @UpdateTimestamp
    @Schema(name = "Modification Timestamp", description = "Timestamp when the staus of order last updated", example = "2023-10-01T12:00:00Z")
    private Instant modified;

    @Transient
    @Schema(name = "Items", description = "List of items in the order")
    private List<OrderItems> items;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_partner")
    @Schema(name = "Delivery Partner", description = "Details of the delivery partner assigned to this order")
    private DeliveryPartner deliveryPartner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address")
    @Schema(name = "Customer Address", description = "Address where the order is to be delivered")
    private CustomerAddress customerAddress;

    @Schema(name = "Status", description = "Current status of the order", example = "PENDING")
    private String status;

    @Schema(name = "Payment Method", description = "Method used for payment", example = "CASH_ON_DELIVERY")
    private String paymentMethod;

    @Schema(name = "Payment Status", description = "Current status of the payment", example = "PENDING")
    private String paymentStatus;

    @Schema(name = "Comment", description = "Any additional comment or instruction for the order", example = "Please deliver before 6 PM")
    private String comment;

    @Schema(name = "Bill Amount", description = "Total amount for the order", example = "7999")
    private double billAmount;

    private Boolean isReturned;

    private Instant returnedAt;

    private Boolean isRefunded;

    private Instant refundedAt;

    private Boolean isDelivered;

    private Instant deliveredAt;

    private Boolean isPickedUp;

    private Boolean isCancelled;
}
