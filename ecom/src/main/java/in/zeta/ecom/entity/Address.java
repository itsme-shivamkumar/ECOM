package in.zeta.ecom.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "address")
@Entity
public class Address {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "uid")
    private String uid;


    @Column(name = "add_nm")
    private String addressName;

    @Column(name = "landmark")
    private String landmark;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "pincode")
    private String pincode;


    public Address(String id, AddressRequest request) {
        this.id = id;
        this.uid = request.getUid();
        this.addressName = request.getAddressName();
        this.landmark = request.getLandmark();
        this.city = request.getCity();
        this.state = request.getState();
        this.country = request.getCountry();
        this.pincode = request.getPincode();
    }

}