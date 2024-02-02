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
public class BorderLinePhoto {
    @Id
    @GeneratedValue
    @Column(name = "border_line_id")
    private Long id;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "similarity_photo_provider_id")
    private SimilarityPhotoProvider similarityPhotoProvider;

    private String url;

    /**
     * 연관관계 편의 메서드
     */
    public void setSimilarityPhotoProvider(SimilarityPhotoProvider similarityPhotoProvider) {
        this.similarityPhotoProvider = similarityPhotoProvider;
        similarityPhotoProvider.setBorderLinePhoto(this);
    }

    /**
     * 정적 팩토리 생성 메서드
     */
    public static BorderLinePhoto createBorderLinePhoto(SimilarityPhotoProvider similarityPhotoProvider, String url) {
        BorderLinePhoto borderLinePhoto = new BorderLinePhoto();
        borderLinePhoto.setSimilarityPhotoProvider(similarityPhotoProvider);
        borderLinePhoto.setUrl(url);

        return borderLinePhoto;
    }
}
