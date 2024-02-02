package capstoneDesign.houseSharing.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue
    @Column(name = "payment_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    private String method;
    private String bank;
    private LocalDateTime dateTime;
    private int amount;

    /**
     * 연관관계 편의 메서드
     */
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        reservation.setPayment(this);
    }

    /**
     * 정적 팩토리 생성 메서드
     */
    public static Payment createPayment(Reservation reservation, String method, String bank, LocalDateTime dateTime) {
        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setMethod(method);
        payment.setBank(bank);
        payment.setDateTime(dateTime);
        payment.setAmount(reservation.getAmount());

        return payment;
    }
}
