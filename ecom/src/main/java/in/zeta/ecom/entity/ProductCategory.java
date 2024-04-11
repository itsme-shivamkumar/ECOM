package in.zeta.ecom.entity;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "product_categories")
public class ProductCategory {

    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "productCategoryId")
    private List<Product> products;
}
