package capstoneDesign.houseSharing.repository;

import capstoneDesign.houseSharing.domain.Member;
import capstoneDesign.houseSharing.domain.TrustScore;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

//@SpringBootTest
public class MemberRepositoryTest {

//    @Autowired MemberRepository memberRepository;
//    @Autowired TrustScoreRepository trustScoreRepository;
//
//    @Test
//    @Transactional
//    public void 회원저장() throws Exception {
//        // given
//        Member member = Member.createMember("정인겸", true,
//                LocalDate.of(1998, 11, 17), "ingyum", "1234567");
//
//        // when
//        Long savedId = memberRepository.save(member);
//        Member findMember = memberRepository.findOne(savedId);
//
//        // then
//        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
//        Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
//        Assertions.assertThat(findMember.getBirth()).isEqualTo(member.getBirth());
//        Assertions.assertThat(findMember.getLogin_id()).isEqualTo(member.getLogin_id());
//        Assertions.assertThat(findMember.getLogin_pw()).isEqualTo(member.getLogin_pw());
//    }
//
//    @Test
//    @Transactional
//    public void 회원탈퇴() throws Exception {
//        // given
//        Member member1 = Member.createMember("정인겸", true,
//                LocalDate.of(1998, 11, 17), "ingyum", "1234567");
//        Member member2 = Member.createMember("이원상", true,
//                LocalDate.of(1998, 05, 13), "wonsang", "123124514");
//        Member member3 = Member.createMember("배준상", true,
//                LocalDate.of(1998, 03, 26), "junsang", "76534");
//        Long savedId1 = memberRepository.save(member1);
//        Long savedId2 = memberRepository.save(member2);
//        Long savedId3 = memberRepository.save(member3);
//
//        TrustScore trustScore1 = TrustScore.createTrustScore(member1, 5);
//        TrustScore trustScore2 = TrustScore.createTrustScore(member2, 4);
//        TrustScore trustScore3 = TrustScore.createTrustScore(member1, 3);
//        TrustScore trustScore4 = TrustScore.createTrustScore(member3, 2);
//        TrustScore trustScore5 = TrustScore.createTrustScore(member3, 1);
//
///*        member1.addTrustScore(trustScore1, trustScore3);
//        member2.addTrustScore(trustScore2);
//        member3.addTrustScore(trustScore4, trustScore5);*/
//
//        trustScoreRepository.save(trustScore1);
//        trustScoreRepository.save(trustScore2);
//        trustScoreRepository.save(trustScore3);
//        trustScoreRepository.save(trustScore4);
//        trustScoreRepository.save(trustScore5);
//
//        // when
//        memberRepository.deleteOne(savedId1);
//
//        // then
//
//    }

/*    @Test
    @Transactional
    public void 이름과휴대폰번호로회원조회() throws Exception {
        // given
        Member member1 = Member.createMember("정인겸", true,
                LocalDate.of(1998, 11, 17), "correct", "12345");     // 기준
        Long savedId1 = memberRepository.save(member1);

        Member member2 = Member.createMember("정인겸", true,
                LocalDate.of(1998, 05, 13), "01098765432", "wrong1", "98765");      // 번호가 다른 경우
        Long savedId2 = memberRepository.save(member2);

        Member member3 = Member.createMember("이원상", true,
                LocalDate.of(1998, 11, 17), "01043218765", "wrong2", "54321");      // 이름과 번호가 다른 경우
        Long savedId3 = memberRepository.save(member3);

        // when
        List<Member> findMembers = memberRepository.findByNameAndPhone("정인겸", "01012345678");
        System.out.println("size = " + findMembers.size());

        // then
        for (Member m : findMembers){
            Assertions.assertThat(m.getName()).isEqualTo(member1.getName());
            Assertions.assertThat(m.getPhone_number()).isEqualTo(member1.getPhone_number());

            System.out.println("login_id = " + m.getLogin_id() + " login_pw = " + m.getLogin_pw());
        }
    }*/
}