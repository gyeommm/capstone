package capstoneDesign.houseSharing.repository;

import capstoneDesign.houseSharing.domain.Review;
import capstoneDesign.houseSharing.domain.TrustScore;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TrustScoreRepository {

    private final EntityManager em;

    public Long save(TrustScore trustScore) {
        em.persist(trustScore);
        return trustScore.getId();
    }

    public List<TrustScore> findTrustScoresByMember(Long memberId) {
        return em.createQuery("select s from TrustScore s where s.member.id = :memberId", TrustScore.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public boolean isExistByReservation(Long reservationId) {
        List<TrustScore> trustScores = em.createQuery("select s from TrustScore s where s.reservation.id = :reservationId", TrustScore.class)
                .setParameter("reservationId", reservationId)
                .getResultList();

        if (trustScores.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
