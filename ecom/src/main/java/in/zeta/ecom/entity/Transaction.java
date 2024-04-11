package in.zeta.ecom.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "transactions")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "oid")
    private String orderId;

    @Column(name = "amnt")
    private double amount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_dt", columnDefinition = "TIMESTAMP")
    private Date createdDate;

    @Column(name = "type")
    private String type;
}
