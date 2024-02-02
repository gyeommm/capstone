package capstoneDesign.houseSharing.repository;

import capstoneDesign.houseSharing.domain.BorderLinePhoto;
import capstoneDesign.houseSharing.domain.SimilarityPhotoProvider;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BorderLinePhotoRepository {
    private final EntityManager em;

    public Long save(BorderLinePhoto borderLinePhoto) {
        em.persist(borderLinePhoto);
        return borderLinePhoto.getId();
    }

    public BorderLinePhoto findOne(Long id) {
        return em.find(BorderLinePhoto.class, id);
    }

    public BorderLinePhoto findOneBySimilarityPhotoProvider(Long similarityPhotoProviderId) {
        return em.createQuery("select p from BorderLinePhoto p where p.similarityPhotoProvider.id = :similarityPhotoProviderId", BorderLinePhoto.class)
                .setParameter("similarityPhotoProviderId", similarityPhotoProviderId)
                .getSingleResult();
    }

    public void deleteOne(Long id) {
        em.remove(findOne(id));
    }
}
