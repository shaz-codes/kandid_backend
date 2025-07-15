package me.kandid.user.Model.Customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
@Schema(title = "Order Items", description = "Items in a customer's order")
public class OrderItems {
    @Id
    @Schema(title = "Item ID", description = "Unique identifier for the order item, used internally", example = "{orderID}-{uniqueItemNumber}, e.g., \'ORD123-001\'")
    private String itemId;
    @Schema(title = "Customer's Phone", description = "Phone number of the customer who placed the order", example = "9161086557")
    private long customerPhone;
    @Schema(title = "Product SKU", description = "Unique identifier for the product, including size", example = "PROD001-M")
    private String productSku;
    @Schema(title = "Order ID", description = "Unique identifier for the order to which this item belongs", example = "ORD123")
    private String orderId;
    @Schema(title = "Quantity", description = "Quantity of the product ordered", example = "2")
    private int quantity;
}
