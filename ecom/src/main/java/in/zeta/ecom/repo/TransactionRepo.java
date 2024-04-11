package in.zeta.ecom.repo;

import in.zeta.ecom.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, String> {

    @Query(value = "select * from transactions where oid=?1", nativeQuery = true)
    Transaction findTransaction(@Param("oid") String oid);
}
