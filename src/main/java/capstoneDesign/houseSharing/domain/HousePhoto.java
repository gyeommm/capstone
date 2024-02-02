package capstoneDesign.houseSharing.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HousePhoto {

    @Id
    @GeneratedValue
    @Column(name = "house_photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id")
    private House house;

    private String url;

    /**
     * 연관관계 편의 메서드
     */
    public void setHouse(House house) {
        this.house = house;
        house.getHousePhotos().add(this);
    }

    /**
     * 정적 팩토리 생성 메서드
     */
    public static HousePhoto createHousePhoto(House house, String url) {
        HousePhoto housePhoto = new HousePhoto();
        housePhoto.setHouse(house);
        housePhoto.setUrl(url);

        return housePhoto;
    }
}
