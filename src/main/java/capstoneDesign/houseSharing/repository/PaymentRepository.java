package capstoneDesign.houseSharing.repository;

import capstoneDesign.houseSharing.domain.Payment;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepository {

    private final EntityManager em;

    public Long save(Payment payment) {
        em.persist(payment);
        return payment.getId();
    }

    public Payment findOne(Long id) {
        return em.find(Payment.class, id);
    }

    public void delete(Long id) {
        em.remove(findOne(id));
    }
}
