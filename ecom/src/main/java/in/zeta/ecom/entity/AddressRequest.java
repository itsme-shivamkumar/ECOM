package in.zeta.ecom.entity;


import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AddressRequest {
    private String uid;
    private String addressName;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private String pincode;
}
