package capstoneDesign.houseSharing.repository;

import capstoneDesign.houseSharing.domain.Review;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {

    private final EntityManager em;

    public Long save(Review review) {
        em.persist(review);
        return review.getId();
    }

    public List<Review> findAllByHouse(Long houseId) {
        return em.createQuery("select r from Review r where r.house.id = :houseId", Review.class)
                .setParameter("houseId", houseId)
                .getResultList();
    }

    public boolean isExistByReservation(Long reservationId) {
        List<Review> reviews = em.createQuery("select r from Review r where r.reservation.id = :reservationId", Review.class)
                .setParameter("reservationId", reservationId)
                .getResultList();

        if (reviews.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
