package capstoneDesign.houseSharing.repository;

import capstoneDesign.houseSharing.domain.Reservation;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {

    private final EntityManager em;

    public Long save(Reservation reservation) {
        em.persist(reservation);
        return reservation.getId();
    }

    public Reservation findOne(Long id) {
        return em.find(Reservation.class, id);
    }

    public List<Reservation> findAllByMember(Long memberId) {
        return em.createQuery("select r from Reservation r where r.member.id = :memberId", Reservation.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    // 주거 삭제 시, 예약에서의 주거id를 모두 null로 바꿔주기 위함
    public List<Reservation> findAllByHouse(Long houseId) {
        return em.createQuery("select r from Reservation r where r.house.id = :houseId", Reservation.class)
                .setParameter("houseId", houseId)
                .getResultList();
    }

    public void delete(Long id) {
        em.remove(findOne(id));
    }
}
