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
@Schema(title = "Customer Order", description = "Details about the customer's order")
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "Order ID", description = "Unique identifier for the order", example = "123456789")
    private long id;

    @Column(nullable = false)
    @Schema(title = "Customer Phone", description = "Phone number of the customer who placed the order", example = "9161086557")
    private long customerPhone;

    @CreationTimestamp
    @Column(updatable = false)
    @Schema(title = "Creation Timestamp", description = "Timestamp when the order was created", example = "2023-10-01T12:00:00Z")
    private Instant created;

    @UpdateTimestamp
    @Schema(title = "Modification Timestamp", description = "Timestamp when the staus of order last updated", example = "2023-10-01T12:00:00Z")
    private Instant modified;

    @Transient
    @Schema(title = "Items", description = "List of items in the order")
    private List<OrderItems> items;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_partner")
    @Schema(title = "Delivery Partner", description = "Details of the delivery partner assigned to this order")
    private DeliveryPartner deliveryPartner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address")
    @Schema(title = "Customer Address", description = "Address where the order is to be delivered")
    private CustomerAddress customerAddress;

    @Schema(title = "Status", description = "Current status of the order", example = "PENDING")
    private String status;

    @Schema(title = "Payment Method", description = "Method used for payment", example = "CASH_ON_DELIVERY")
    private String paymentMethod;

    @Schema(title = "Payment Status", description = "Current status of the payment", example = "PENDING")
    private String paymentStatus;

    @Schema(title = "Comment", description = "Any additional comment or instruction for the order", example = "Please deliver before 6 PM")
    private String comment;

    @Schema(title = "Bill Amount", description = "Total amount for the order", example = "7999")
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
