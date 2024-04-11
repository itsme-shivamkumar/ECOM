package in.zeta.ecom.repo;

import in.zeta.ecom.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoriesRepo extends JpaRepository<ProductCategory, String> {
    @Query(value = "SELECT * FROM product_categories p WHERE p.name=:name", nativeQuery = true)
    ProductCategory findByName(@Param("name") String name);
}
