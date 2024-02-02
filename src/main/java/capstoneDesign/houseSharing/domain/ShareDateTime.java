package capstoneDesign.houseSharing.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShareDateTime {

    @Id
    @GeneratedValue
    @Column(name = "share_datetime_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id")
    private House house;

    private LocalDateTime start;
    private LocalDateTime end;

    @ColumnDefault("true")
    private Boolean state;

    /**
     * 연관관계 편의 메서드
     */
    public void setHouse(House house) {
        this.house = house;
        house.getShareDateTimes().add(this);
    }

    /**
     * 정적 팩토리 생성 메서드
     */
    public static ShareDateTime createShareDateTime(House house, LocalDateTime start, LocalDateTime end) {
        ShareDateTime shareDateTime = new ShareDateTime();
        shareDateTime.setHouse(house);
        shareDateTime.setStart(start);
        shareDateTime.setEnd(end);
        shareDateTime.setState(true);

        return shareDateTime;
    }
}
