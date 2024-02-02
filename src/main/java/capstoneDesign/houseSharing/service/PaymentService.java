package capstoneDesign.houseSharing.service;

import capstoneDesign.houseSharing.domain.Payment;
import capstoneDesign.houseSharing.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Long payment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public int getPrice(Long paymentId) {
        Payment payment = paymentRepository.findOne(paymentId);
        return payment.getAmount();
    }

    @Transactional
    public void cancel(Long id) {
        paymentRepository.delete(id);
    }
}
