package capstoneDesign.houseSharing.repository;

import capstoneDesign.houseSharing.domain.SimilarityPhotoProvider;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SimilarityPhotoProviderRepository {

    private final EntityManager em;

    public Long save(SimilarityPhotoProvider similarityPhotoProvider) {
        em.persist(similarityPhotoProvider);
        return similarityPhotoProvider.getId();
    }

    public SimilarityPhotoProvider findOne(Long id) {
        return em.find(SimilarityPhotoProvider.class, id);
    }

    public List<SimilarityPhotoProvider> findAllByHouse(Long houseId) {
        return em.createQuery("select p from SimilarityPhotoProvider p where p.house.id = :houseId", SimilarityPhotoProvider.class)
                .setParameter("houseId", houseId)
                .getResultList();
    }

    public void deleteOne(Long id) {
        em.remove(findOne(id));
    }
}
