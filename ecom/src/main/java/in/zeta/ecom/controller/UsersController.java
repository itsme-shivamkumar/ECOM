package in.zeta.ecom.controller;


import in.zeta.ecom.entity.Address;
import in.zeta.ecom.entity.AddressRequest;
import in.zeta.ecom.entity.User;
import in.zeta.ecom.exceptions.NoAddressFoundForUser;
import in.zeta.ecom.exceptions.NotAuthorizedException;
import in.zeta.ecom.services.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsersController {

    private UserInfoService userInfoService;


    @Autowired
    public UsersController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }


    @GetMapping("/users/info")
    public ResponseEntity<User> getUserInfo(@RequestParam String userId, @RequestHeader("Authorization") String authToken) throws NotAuthorizedException {
        return ResponseEntity.ok(userInfoService.findUserByUid(userId,authToken.substring(7)));
    }

    @GetMapping("/users/delivery-address")
    public ResponseEntity<List<Address>> getAddressesByPhoneNumber(@RequestParam String phoneNum) throws NoAddressFoundForUser {
        return ResponseEntity.ok(userInfoService.fetchAllAddressesByPhone(phoneNum));
    }

    // user_specific
    @PostMapping("/users/save/delivery-address")
    public String saveAddressByUID(@RequestBody AddressRequest request) {
        return userInfoService.saveAddress(request);
    }

}
