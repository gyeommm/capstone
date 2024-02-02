package capstoneDesign.houseSharing.service;

import capstoneDesign.houseSharing.domain.TrustScore;
import capstoneDesign.houseSharing.repository.TrustScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrustScoreService {

    private final TrustScoreRepository trustScoreRepository;

    @Transactional
    public Long appendTrustScore(TrustScore trustScore) {
        return trustScoreRepository.save(trustScore);
    }

    public int calculateAverage(Long memberId) {
        List<TrustScore> trustScores = trustScoreRepository.findTrustScoresByMember(memberId);

        double sum = 0;
        for (TrustScore trustScore : trustScores) {
            sum += trustScore.getScore();
        }
        sum /= trustScores.size();

        return (int) Math.round(sum);
    }

    public boolean isAlreadyTestByReservation(Long reservationId) {
        return trustScoreRepository.isExistByReservation(reservationId);
    }
}
