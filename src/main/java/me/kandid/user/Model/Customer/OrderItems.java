package me.kandid.user.Model.Customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Schema(name = "Order Items", description = "Items in a customer's order")
public class OrderItems {
    @Id
    @Schema(name = "Item ID", description = "Unique identifier for the order item, used internally", example = "{orderID}-{uniqueItemNumber}, e.g., \'ORD123-001\'")
    private String itemId;
    @Schema(name = "Customer's Phone", description = "Phone number of the customer who placed the order", example = "9161086557")
    private long customerPhone;
    @Schema(name = "Product SKU", description = "Unique identifier for the product, including size", example = "PROD001-M")
    private String productSku;
    @Schema(name = "Order ID", description = "Unique identifier for the order to which this item belongs", example = "ORD123")
    private String orderId;
    @Schema(name = "Quantity", description = "Quantity of the product ordered", example = "2")
    private int quantity;
}
