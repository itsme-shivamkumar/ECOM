package in.zeta.ecom.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "uid")
    private String userId;
    @Column(name = "pid")
    private String productId;
    @Column(name = "qty")
    private int quantity;
    @Column(name = "prc")
    private double price;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_dt", columnDefinition = "TIMESTAMP")
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_dt", columnDefinition = "TIMESTAMP")
    private Date updatedDate;
    @Column(name = "status")
    private String status;
    @OneToMany(mappedBy = "orderId")
    List<Transaction> transactions;

}
