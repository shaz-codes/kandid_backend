package me.kandid.user.Model.Product;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Brand {
    @Id
    private int id;
    private String displayName;
    private String description;
    private String fullName;
    private String logoUrl;
    private String officeAddress;
    private String storeAddress;
    private String gstId;
    private String website;
}
