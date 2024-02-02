package capstoneDesign.houseSharing.service;

import capstoneDesign.houseSharing.domain.House;
import capstoneDesign.houseSharing.domain.ShareDateTime;
import capstoneDesign.houseSharing.repository.ShareDateTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShareDateTimeService {

    private final ShareDateTimeRepository shareDateTimeRepository;

    @Transactional
    public Long add(ShareDateTime shareDateTime) {
        return shareDateTimeRepository.save(shareDateTime);
    }

    @Transactional
    public Long changeStateToFalse(Long houseId, LocalDateTime reservationStart, LocalDateTime reservationEnd) {
        ShareDateTime shareDateTime = shareDateTimeRepository.findOneByReservation(houseId, reservationStart, reservationEnd);

        if (shareDateTime.getState() == true) {
            shareDateTime.setState(false);
        }

        return shareDateTime.getId();
    }

    @Transactional
    public void calculateAndAdd(Long id, House house, LocalDateTime reservationStart, LocalDateTime reservationEnd) {
        ShareDateTime shareDateTime = shareDateTimeRepository.findOne(id);

        if (shareDateTime.getStart().equals(reservationStart)) {
            if (shareDateTime.getEnd().equals(reservationEnd)) {
                return;
            } else {
                ShareDateTime new2 = ShareDateTime.createShareDateTime(house, reservationEnd, shareDateTime.getEnd());
                shareDateTimeRepository.save(new2);
            }
        } else {
            if (shareDateTime.getEnd().equals(reservationEnd)) {
                ShareDateTime new1 = ShareDateTime.createShareDateTime(house, shareDateTime.getStart(), reservationStart);
                shareDateTimeRepository.save(new1);
            } else {
                ShareDateTime new1 = ShareDateTime.createShareDateTime(house, shareDateTime.getStart(), reservationStart);
                ShareDateTime new2 = ShareDateTime.createShareDateTime(house, reservationEnd, shareDateTime.getEnd());
                shareDateTimeRepository.save(new1);
                shareDateTimeRepository.save(new2);
            }
        }
    }



    public List<House> search(LocalDateTime start, LocalDateTime end) {
        return shareDateTimeRepository.findHousesByShareDateTime(start, end);
    }
}
