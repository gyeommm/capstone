package capstoneDesign.houseSharing.service;

import capstoneDesign.houseSharing.domain.Reservation;
import capstoneDesign.houseSharing.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Transactional
    public Long reservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Reservation findReservation(Long id) {
        return reservationRepository.findOne(id);
    }

    public List<Reservation> findReservationsByMember(Long memberId) {
        return reservationRepository.findAllByMember(memberId);
    }

    public List<Reservation> findReservationsByHouse(Long houseId) {
        return reservationRepository.findAllByHouse(houseId);
    }

    @Transactional
    public void cancel(Long id) {
        reservationRepository.delete(id);
    }

}
