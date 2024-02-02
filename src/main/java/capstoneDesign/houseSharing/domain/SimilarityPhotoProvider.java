package capstoneDesign.houseSharing.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SimilarityPhotoProvider {

    @Id
    @GeneratedValue
    @Column(name = "similarity_photo_provider_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id")
    private House house;

    private String url;

    @OneToOne(mappedBy = "similarityPhotoProvider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BorderLinePhoto borderLinePhoto;

    /**
     * 연관관계 편의 메서드
     */
    public void setHouse(House house) {
        this.house = house;
        house.getSimilarityPhotosProvider().add(this);
    }

    /**
     * 정적 팩토리 생성 메서드
     */
    public static SimilarityPhotoProvider createSimilarityPhotoProvider(House house, String url) {
        SimilarityPhotoProvider similarityPhotoProvider = new SimilarityPhotoProvider();
        similarityPhotoProvider.setHouse(house);
        similarityPhotoProvider.setUrl(url);

        return similarityPhotoProvider;
    }
}
