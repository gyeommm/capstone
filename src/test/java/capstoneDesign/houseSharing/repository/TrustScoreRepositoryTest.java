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
public class TrustScoreRepositoryTest {

//    @Autowired TrustScoreRepository trustScoreRepository;
//    @Autowired MemberRepository memberRepository;
//
//   @Test
//   @Transactional
//   public void 신뢰도점수저장() throws Exception {
//       // given
//       Member member1 = Member.createMember("정인겸", true,
//               LocalDate.of(1998, 11, 17), "ingyum", "1234567");
//       Member member2 = Member.createMember("이원상", true,
//               LocalDate.of(1998, 05, 13), "wonsang", "123124514");
//       Long savedId1 = memberRepository.save(member1);
//       Long savedId2 = memberRepository.save(member2);
//
//       TrustScore trustScore1 = TrustScore.createTrustScore(member1, 5);
//       TrustScore trustScore2 = TrustScore.createTrustScore(member2, 4);
//       TrustScore trustScore3 = TrustScore.createTrustScore(member1, 3);
//       TrustScore trustScore4 = TrustScore.createTrustScore(member2, 2);
//       TrustScore trustScore5 = TrustScore.createTrustScore(member1, 1);
//
///*     member1.addTrustScore(trustScore1, trustScore3, trustScore5);
//       member2.addTrustScore(trustScore2, trustScore4);*/
//
//       // when
//       trustScoreRepository.save(trustScore1);
//       trustScoreRepository.save(trustScore2);
//       trustScoreRepository.save(trustScore3);
//       trustScoreRepository.save(trustScore4);
//       trustScoreRepository.save(trustScore5);
//       List<TrustScore> findTrustScores = trustScoreRepository.findTrustScoresByMember(savedId1);
//       System.out.println("findTrustScores.size() = " + findTrustScores.size());
//
//       // then
//       int index = 0;
//       for (TrustScore trustScore : findTrustScores) {
//           Assertions.assertThat(trustScore.getMember().getId()).isEqualTo(member1.getId());
//           Assertions.assertThat(trustScore.getScore()).isEqualTo(member1.getTrustScores().get(index).getScore());
//           index++;
//       }
//   }
}