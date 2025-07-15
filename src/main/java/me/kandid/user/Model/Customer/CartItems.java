package me.kandid.user.Model.Customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
@Schema(title = "Cart Items", description = "Product with it's quantity")
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "Randomly generated id", description = "Randomly generated id for internal tracking")
    private long id;
    @Schema(title = "Customer's Phone", description = "Customer's Phone, used internally")
    private long customerPhone;
    @Schema(title = "SKU", description = "Unique Product Code with Size seperated by -", example = "PROD001-M")
    private String productSku;
    @Schema(title = "Quantity of product of the specified SKU", example = "3")
    private int quantity;
}
