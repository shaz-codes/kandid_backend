package me.kandid.user.Model.Product;


import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class Product {
    @Id
    private String code;
    private String name;
    private String description;
    private Boolean active;
    private Boolean available;
//    available will be derived
//    from ProductVariant
//    after checking inventory
//    and will get final updated when
//    the last order's return period gets over

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "visuals_id")
    private Visuals visuals;
    private long priceInPaise;
    private String category;
    private String subCategory;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(name = "")
    private List<ProductVariants> inventory;
    // the availble inventory in each products->productvariant
    // will be derived seeing the orders and fullfillment orders

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private ProductFilter filter;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Discount> discount;

}
