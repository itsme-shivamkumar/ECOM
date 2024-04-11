package in.zeta.ecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "user_info")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "role")
    private String role;

    @Column(name = "phn_num")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_dt", columnDefinition = "TIMESTAMP")
    private Date createdDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_dt", columnDefinition = "TIMESTAMP")
    private Date updatedDate;

    @Column(name = "prof_sts")
    private String profileStatus;


    @Column(name = "password")
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "uid")
    private List<Address> addresses;
}
