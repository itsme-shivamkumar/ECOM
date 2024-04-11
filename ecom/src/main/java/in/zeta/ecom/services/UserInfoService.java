package in.zeta.ecom.services;

import in.zeta.ecom.entity.Address;
import in.zeta.ecom.entity.AddressRequest;
import in.zeta.ecom.entity.User;
import in.zeta.ecom.exceptions.NoAddressFoundForUser;
import in.zeta.ecom.exceptions.NotAuthorizedException;
import in.zeta.ecom.repo.AddressRepo;
import in.zeta.ecom.repo.UserRepo;
import in.zeta.ecom.utils.JwtUtils;
import in.zeta.ecom.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserInfoService {


    private final RandomIdGenerator rig;


    private final UserRepo userRepo;
    private final JwtUtils jwtUtils;
    private final AddressRepo addressRepo;

    @Autowired
    public UserInfoService(RandomIdGenerator rig, UserRepo userRepo, AddressRepo addressRepo, JwtUtils jwtUtils) {
        this.rig = rig;
        this.userRepo = userRepo;
        this.addressRepo = addressRepo;
        this.jwtUtils=jwtUtils;
    }

    public User findUserByUid(String userId, String token) throws NotAuthorizedException{
        String email = jwtUtils.getEmailFromToken(token);
        User currUser = userRepo.findUserByEmail(email);
        if(Objects.equals(userId,currUser.getId())){
            return currUser;
        }
        else{
          throw new NotAuthorizedException("you are not authorized to get details of userId: "+userId);
        }
    }

    public List<Address> fetchAllAddressesByPhone(String phoneNum) throws NoAddressFoundForUser {

        User user = userRepo.findUserByPhoneNumber(phoneNum);
        if(user == null){
            throw new NoAddressFoundForUser(phoneNum);
        }
        return user.getAddresses();
    }

    public String saveAddress(AddressRequest request) {
        String aid = rig.generateRandomId();
        Address newAdd = new Address(aid, request);
        addressRepo.save(newAdd);
        return "Successfully Added Addess with id: " + aid;
    }
}
