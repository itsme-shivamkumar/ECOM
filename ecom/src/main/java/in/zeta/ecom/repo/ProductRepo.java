package in.zeta.ecom.repo;

import in.zeta.ecom.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, String> {

    @Query(value = "select * from product p where p.stk < ?1", nativeQuery = true)
    List<Product> getAllProductsLessThanStock(@Param("amnt") int amnt);
}
