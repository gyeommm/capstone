package capstoneDesign.houseSharing.repository;

import capstoneDesign.houseSharing.domain.House;
import capstoneDesign.houseSharing.domain.ShareDateTime;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ShareDateTimeRepository {

    private final EntityManager em;

    public Long save(ShareDateTime shareDateTime) {
        em.persist(shareDateTime);
        return shareDateTime.getId();
    }

    public ShareDateTime findOne(Long id) {
        return em.find(ShareDateTime.class, id);
    }

    /*public void delete(Long id) {
        em.remove(findOne(id));
    }*/

    public ShareDateTime findOneByReservation(Long houseId, LocalDateTime start, LocalDateTime end) {
        return em.createQuery("select s from ShareDateTime s where " +
                        "s.house.id = :houseId and s.state = true and " +
                        "s.start <= :start and s.end >= :end", ShareDateTime.class)
                .setParameter("houseId", houseId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
    }

    public List<House> findHousesByShareDateTime(LocalDateTime start, LocalDateTime end) {
        return em.createQuery("select s.house from ShareDateTime s " +
                        "where s.state = true and s.start <= :start and s.end >= :end", House.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}
