package me.kandid.user.Model.Requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import me.kandid.user.Model.Customer.CustomerAddress;
import me.kandid.user.Model.Enums.OrderTypes;

@Schema(
        title = "Order Request",
        description = "Request body to be sent along checkout"
)
@Data
public class OrderRequest {

    @Schema(
            description = "Address of the customer where the product is to be delivered, REQUIRED, only the customerAddress id needed"
    )
    private CustomerAddress address;


    @Schema(
            description = "Order Types, TRY_AND_BUY or REGULAR"
    )
    private OrderTypes orderType;

    @Schema(
            description = "if Buy Now, then send true"
    )
    private Boolean buynow;

    @Schema(
            description = "if Buy Now, then send sku"
    )
    private String sku;

    @Schema(
            description = "if Buy Now, then send qty"
    )
    private int quantity;


}

