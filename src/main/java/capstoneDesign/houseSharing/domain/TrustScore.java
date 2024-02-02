package capstoneDesign.houseSharing.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrustScore {

    @Id
    @GeneratedValue
    @Column(name = "trustScore_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    private int score;

    /**
     * 연관관계 편의 메서드
     */
    public void setMember(Member member) {
        this.member = member;
        member.getTrustScores().add((this));
    }

    public void setReservation(Reservation reservation) {
        if (this.reservation != null) {
            this.reservation.setTrustScore(null);
        }

        this.reservation = reservation;
        if (reservation != null) {
            reservation.setTrustScore(this);
        }
    }

    /**
     * 정적 팩토리 생성 메서드
     */
    public static TrustScore createTrustScore(Member member, Reservation reservation, int score) {
        TrustScore trustScore = new TrustScore();
        trustScore.setMember(member);
        trustScore.setReservation(reservation);
        trustScore.setScore(score);

        return trustScore;
    }
}
