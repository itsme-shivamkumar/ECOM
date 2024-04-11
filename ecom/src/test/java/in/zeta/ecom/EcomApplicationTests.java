package in.zeta.ecom;

import in.zeta.ecom.entity.AuthSignupRequest;
import in.zeta.ecom.entity.AuthSignupResponse;
import in.zeta.ecom.entity.User;
import in.zeta.ecom.repo.UserRepo;
import in.zeta.ecom.services.AuthService;
import in.zeta.ecom.services.UserInfoService;
import in.zeta.ecom.utils.RandomIdGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class EcomApplicationTests {

}