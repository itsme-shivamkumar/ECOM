package in.zeta.ecom.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthSignupResponse {
    private String msg;
    private String email;
    private String name;
    private String role;
    private String phoneNumber;
    private String token;
    private String sessionId;
}
