package in.zeta.ecom.repo;

import in.zeta.ecom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, String> {


    @Query(value = "SELECT * FROM user_info u WHERE u.phn_num=?1", nativeQuery = true)
    User findUserByPhoneNumber(@Param("phoneNum") String phoneNum);

    @Query(value = "SELECT * FROM user_info u WHERE u.email=?1", nativeQuery = true)
    User findUserByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM user_info WHERE id=?1",nativeQuery = true)
    User findUserByUID(@Param("userId") String userId);

}
