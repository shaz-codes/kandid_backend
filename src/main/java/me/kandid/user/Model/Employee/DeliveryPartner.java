package me.kandid.user.Model.Employee;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Schema(name = "Delivery Partner Details", description = "Details about the delivery partner")
@Entity
@Data
public class DeliveryPartner {
    @Id
    @Schema(name = "Phone", description = "Phone Number of the delivery person", example = "9161086557", required = true)
    private long phone;
    @Schema(name = "Name", description = "Name of delivery partner", example = "Samriddh Verma")
    private String name;
}
