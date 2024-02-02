package capstoneDesign.houseSharing.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id")
    private House house;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    private double star;

    @Column(length = 100)       // varchar(100)
    private String content;

    /**
     * 연관관계 편의 메서드
     */
    public void setHouse(House house) {
        this.house = house;
        house.getReviews().add(this);
    }

    public void setReservation(Reservation reservation) {
        if (this.reservation != null) {
            this.reservation.setReview(null);
        }

        this.reservation = reservation;
        if (reservation != null) {
            reservation.setReview(this);
        }
    }

    /**
     * 정적 팩토리 생성 메서드
     */
    public static Review createReview(House house, Reservation reservation, double star, String content) {
        Review review = new Review();
        review.setHouse(house);
        review.setReservation(reservation);
        review.setStar(star);
        review.setContent(content);
        return review;
    }
}
