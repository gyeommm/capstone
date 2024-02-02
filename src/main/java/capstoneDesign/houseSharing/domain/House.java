package capstoneDesign.houseSharing.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class House {

    @Id
    @GeneratedValue
    @Column(name = "house_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 1000)      // varchar(1000)
    private String content;

    private int price;
    private double averageScore;

    @Embedded
    @Column(unique = true)
    private Address address;

    @Embedded
    private Coordinate coordinate;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL)
    private List<ShareDateTime> shareDateTimes = new ArrayList<>();

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "house")
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL)
    private List<HousePhoto> housePhotos = new ArrayList<>();

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL)
    private List<SimilarityPhotoProvider> similarityPhotosProvider = new ArrayList<>();

    /**
     * 연관관계 편의 메서드
     */
    public void setMember(Member member) {
        this.member = member;
        member.getHouses().add(this);
    }

    /*public void addShareDateTime(ShareDateTime shareDateTime) {
        shareDateTime.setHouse(this);
        shareDateTimes.add(shareDateTime);
    }*/

    /**
     * 정적 팩토리 생성 메서드
     */
    public static House createHouse(Member member, String content, int price, Address address, Coordinate coordinate) {
        House house = new House();
        house.setMember(member);
        house.setContent(content);
        house.setPrice(price);
        house.setAddress(address);
        house.setCoordinate(coordinate);
        /*for (ShareDateTime shareDateTime : shareDateTimes) {
            house.addShareDateTime(shareDateTime);
        }*/

        return house;
    }
}