package me.kandid.user.Model.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ProductFilter {
    @Id
    private String code;
    private String material;
    private String color;
    private String occasion;
    private String sleeve;
    private String fit;
    private String fitType;
    private String pattern;
    private String neckline;
    private String closure;
    private String closureType;
    private String riseStyle;
    private String style;
    private String aesthetic;
    private String trend;
}
