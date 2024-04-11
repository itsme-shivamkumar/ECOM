package in.zeta.ecom.services;

import in.zeta.ecom.entity.Address;
import in.zeta.ecom.entity.AddressRequest;
import in.zeta.ecom.entity.User;
import in.zeta.ecom.exceptions.NoAddressFoundForUser;
import in.zeta.ecom.repo.AddressRepo;
import in.zeta.ecom.repo.UserRepo;
import in.zeta.ecom.utils.RandomIdGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserInfoServiceTest {
    @Mock
    private RandomIdGenerator rig;

    @Mock
    private UserRepo userRepo;

    @Mock
    private AddressRepo addressRepo;

    @InjectMocks
    private UserInfoService userInfoService;

    @Test
    void shouldFetchAllAddressesByPhone() throws NoAddressFoundForUser {
        String phoneNum = "1234567890";
        User mockUser = new User();
        List<Address> addresses = new ArrayList<>();
        Address mockAddress =  Address.builder().id("mockId").uid("mockUid").addressName("mockAddressName").state("mockState").city("mockCity").landmark("mockLandmark").pincode("mockPincode").build();
        addresses.add(mockAddress);
        mockUser.setAddresses(addresses);

        Mockito.when(userRepo.findUserByPhoneNumber(phoneNum)).thenReturn(mockUser);

        List<Address> result = userInfoService.fetchAllAddressesByPhone(phoneNum);

        assertEquals(addresses, result);
    }

    @Test
    void shouldThrowNoAddressFoundForUserException() {
        String phoneNum = "9876543210";

        Mockito.when(userRepo.findUserByPhoneNumber(phoneNum)).thenReturn(null);

        assertThrows(NoAddressFoundForUser.class,
                () -> userInfoService.fetchAllAddressesByPhone(phoneNum),
                "Should throw NoAddressFoundForUser exception when no user found");
    }

    @Test
    void shouldSaveAddress() {
        AddressRequest addressRequest = AddressRequest.builder().addressName("mockAddressName").country("mockCountry").uid("mockUid").state("mockState").city("mockCity").landmark("mockLandmark").pincode("mockPincode").build();
        Address mockAddress = new Address("mockId", addressRequest);
        Mockito.when(rig.generateRandomId()).thenReturn("mockRandomId");
        Mockito.when(addressRepo.save(Mockito.any(Address.class))).thenReturn(mockAddress);

        String result = userInfoService.saveAddress(addressRequest);

        assertEquals("Successfully Added Addess with id: mockRandomId", result);
    }
}
