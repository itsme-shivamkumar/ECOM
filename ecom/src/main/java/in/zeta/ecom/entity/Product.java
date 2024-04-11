package in.zeta.ecom.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "pcid")
    private String productCategoryId;

    @Column(name = "dsc")
    private String description;

    @Column(name = "name")
    private String name;

    @Column(name = "prc")
    private double price;

    @Column(name = "stk")
    private int stock;

    @Column(name = "curr_id")
    private String currency;
}
