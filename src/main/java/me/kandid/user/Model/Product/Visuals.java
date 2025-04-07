package me.kandid.user.Model.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Visuals {
    @Id
    private long id;
    private String type;
    private String dataURL;
}
