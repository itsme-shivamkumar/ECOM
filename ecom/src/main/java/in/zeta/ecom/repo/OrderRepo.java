package in.zeta.ecom.repo;

import in.zeta.ecom.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, String> {

    @Query(value = "select * from orders where uid=?1", nativeQuery = true)
    List<Order> findOrdersByUid(@Param("uid") String uid);

    @Query(value = "select pid, count(pid) as no_of_orders from orders WHERE status='PLACED' group by pid order by no_of_orders desc", nativeQuery = true)
    List<Object[]> findTrendingProducts();
}
