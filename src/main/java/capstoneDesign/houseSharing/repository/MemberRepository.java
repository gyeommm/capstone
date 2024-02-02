package capstoneDesign.houseSharing.repository;

import capstoneDesign.houseSharing.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public void deleteOne(Long id) {
        em.remove(findOne(id));
    }

/*    public List<Member> findByNameAndPhone(String name, String phone_number) {
        return em.createQuery("select m from Member m where m.name = :name and m.phone_number = :phone_number", Member.class)
                .setParameter("name", name).setParameter("phone_number", phone_number)
                .getResultList();
    }*/
}