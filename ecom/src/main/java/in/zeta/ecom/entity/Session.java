package in.zeta.ecom.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "session")
@Entity
public class Session {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "status")
    private String status;

}
