package capstoneDesign.houseSharing.repository;

import capstoneDesign.houseSharing.domain.HousePhoto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HousePhotoRepository {

    private final EntityManager em;

    public Long save(HousePhoto housePhoto) {
        em.persist(housePhoto);
        return housePhoto.getId();
    }

    public HousePhoto findOne(Long id) {
        return em.find(HousePhoto.class, id);
    }

    public List<HousePhoto> findAllByHouse(Long houseId) {
        return em.createQuery("select p from HousePhoto p where p.house.id = :houseId", HousePhoto.class)
                .setParameter("houseId", houseId)
                .getResultList();
    }

    public void deleteOne(Long id) {
        em.remove(findOne(id));
    }
}
