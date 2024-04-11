package in.zeta.ecom.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String id;

    private String productCategoryId;

    private String description;

    private String name;

    private double price;

    private int stock;

    private String currency;
}
