package in.zeta.ecom.repo;


import in.zeta.ecom.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface SessionRepo extends JpaRepository<Session, String> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE session status SET status=?1 WHERE id=?2", nativeQuery = true)
    void updateSessionStatus(@Param("status") String status, @Param("id") String id);

    @Query(value = "SELECT * from session where id = ?1",nativeQuery = true)
    Session findSessionById(@Param("sessionId") String sessionId);
}
