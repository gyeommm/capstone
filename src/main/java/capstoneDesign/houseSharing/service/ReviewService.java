package capstoneDesign.houseSharing.service;

import capstoneDesign.houseSharing.domain.Review;
import capstoneDesign.houseSharing.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public Long review(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> findReviewsByHouse(Long houseId) {
        return reviewRepository.findAllByHouse(houseId);
    }

    public boolean isReviewWrittenByReservation(Long reservationId) {
        return reviewRepository.isExistByReservation(reservationId);
    }

    public double calculateAverage(Long houseId) {
        List<Review> reviews = findReviewsByHouse(houseId);

        double sum = 0;
        for (Review review : reviews) {
            sum += review.getStar();
        }
        sum /= reviews.size();

        return sum;
    }
}
