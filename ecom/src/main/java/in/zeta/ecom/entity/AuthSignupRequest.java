package in.zeta.ecom.entity;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthSignupRequest {

    @Email(message = "email format is not valid")
    private String email;
    @NotNull
    private String password;
    private String name;
    private String role;
    @NotBlank(message = "phone number can not be blank")
    private String phoneNumber;
    private String profileStatus;
}
