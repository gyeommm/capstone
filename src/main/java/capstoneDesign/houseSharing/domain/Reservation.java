package capstoneDesign.houseSharing.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id @GeneratedValue
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id")
    private House house;

    private LocalDateTime start;
    private LocalDateTime end;

    private int amount;

    // 주거가 삭제돼도 예약은 삭제되지 않기에 예약 정보를 보여주기 위해 주소를 따로 가짐.
    private Address address;

    @OneToOne(mappedBy = "reservation", fetch = FetchType.LAZY)
    private Review review;

    @OneToOne(mappedBy = "reservation", fetch = FetchType.LAZY)
    private TrustScore trustScore;

    /**
     * 연관관계 편의 메서드
     */
    public void setMember(Member member) {
        this.member = member;
        member.getReservations().add(this);
    }

    public void setHouse(House house) {
        if (this.house != null) {
            this.house.getReservations().remove(this);
        }

        this.house = house;
        if (house != null) {
            house.getReservations().add(this);
        }

        /*this.house = house;
        house.getReservations().add(this);*/
    }

    /**
     * 정적 팩토리 생성 메서드
     */
    public static Reservation createReservation(Member member, House house, LocalDateTime start, LocalDateTime end) {
        Reservation reservation = new Reservation();
        reservation.setMember(member);
        reservation.setHouse(house);
        reservation.setStart(start);
        reservation.setEnd(end);

        int amount = house.getPrice() * calculateCycle(start, end);
        reservation.setAmount(amount);

        reservation.setAddress(house.getAddress());

        return reservation;
    }

    /**
     * 비즈니스 로직
     */
    public static int calculateCycle(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        return ( (int) duration.getSeconds() ) / 60 / 10;
    }
}