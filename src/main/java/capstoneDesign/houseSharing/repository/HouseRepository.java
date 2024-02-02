package capstoneDesign.houseSharing.repository;

import capstoneDesign.houseSharing.domain.House;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HouseRepository {

    private final EntityManager em;

    public Long save(House house) {
        em.persist(house);
        return house.getId();
    }

    public House findOne(Long id) {
        return em.find(House.class, id);
    }

    /*public House findByAddress(Address address) {
        return em.createQuery("select h from House h where h.address = :address", House.class)
                .setParameter("address", address)
                .getSingleResult();
    }*/

    public List<House> findAllByMember(Long memberId) {
        return em.createQuery("select h from House h where h.member.id = :memberId", House.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public List<House> findAll() {
        return em.createQuery("select h from House h", House.class)
                .getResultList();
    }

    public void deleteOne(Long id) {
        em.remove(findOne(id));
    }

}
