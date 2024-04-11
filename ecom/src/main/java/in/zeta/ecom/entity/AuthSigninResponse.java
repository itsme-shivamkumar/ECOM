package in.zeta.ecom.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthSigninResponse {
    private String userId;
    private String email;
    private String name;
    private String phoneNum;
    private String sessionId;
    private String tokenId;
    private String msg;
}
