package capstoneDesign.houseSharing.controller;

import capstoneDesign.houseSharing.awsS3.S3Service;
import capstoneDesign.houseSharing.domain.*;
import capstoneDesign.houseSharing.response.CommonResponse;
import capstoneDesign.houseSharing.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final HouseService houseService;
    private final MemberService memberService;
    private final ShareDateTimeService shareDateTimeService;
    private final S3Service s3Service;
    private final ReviewService reviewService;
    private final TrustScoreService trustScoreService;

    /**
     * 주거 예약
     */
    @PostMapping("/houses/{id}/reservation")
    public CommonResponse reservationHouse(@PathVariable("id") Long houseId, Long memberId, @RequestBody @Valid ReservationRequest request) {

        House house = houseService.findHouse(houseId);
        Member member = memberService.findMember(memberId);

        Long changedShareDateTimeId = shareDateTimeService.changeStateToFalse(houseId, request.getStart(), request.getEnd());
        shareDateTimeService.calculateAndAdd(changedShareDateTimeId, house, request.getStart(), request.getEnd());

        Reservation reservation = Reservation.createReservation(member, house, request.getStart(), request.getEnd());
        reservationService.reservation(reservation);

        return new CommonResponse(true, "예약 성공");
    }

    @Data
    static class ReservationRequest {
        @NotNull(message = "start(localDateTime) must not be null")
        private LocalDateTime start;

        @NotNull(message = "end(localDateTime) must not be null")
        private LocalDateTime end;
    }

    /**
     * 특정 회원의 예약 list
     */
    @GetMapping("/members/{id}/reservations")
    public ReservationListResponse reservationList(@PathVariable("id") Long memberId) throws IOException {
        List<Reservation> reservations = reservationService.findReservationsByMember(memberId);
        List<ReservationInfoDTO> reservationsInfo = new ArrayList<>();

        for (Reservation reservation : reservations) {
            House house = reservation.getHouse();
            Long houseId = null;
            byte[] photo = null;

            if (house != null) {
                houseId = house.getId();
                photo = s3Service.download(house.getHousePhotos().get(0).getUrl());
            }

            boolean isReviewWritten = reviewService.isReviewWrittenByReservation(reservation.getId());
            boolean isAlreadyTest = trustScoreService.isAlreadyTestByReservation(reservation.getId());

            reservationsInfo.add(new ReservationInfoDTO(reservation.getId(),
                    reservation.getStart(), reservation.getEnd(), reservation.getAmount(),
                    houseId, photo, reservation.getAddress().getFull_name(), reservation.getAddress().getDetail(),
                    isReviewWritten, isAlreadyTest));
        }
        return new ReservationListResponse(new CommonResponse(true, "예약 list 조회 성공"),
                reservationsInfo.size(), reservationsInfo);
    }

    @Data
    @AllArgsConstructor
    static class ReservationInfoDTO {
        private Long reservationId;
        private LocalDateTime start;
        private LocalDateTime end;
        private int amount;

        private Long houseId;
        private byte[] photo;
        private String full_name;
        private String detail;
        private boolean isReviewWritten;
        private boolean isAlreadyTest;
    }

    @Data
    @AllArgsConstructor
    static class ReservationListResponse {
        private CommonResponse commonResponse;

        private int size;
        private List<ReservationInfoDTO> reservationList;
    }

    /**
     * 예약 상세 페이지
     */
    @GetMapping("/reservation/{id}")
    public ReservationDetailInfoResponse reservationDetail(@PathVariable("id") Long id) {
        Reservation reservation = reservationService.findReservation(id);

        return new ReservationDetailInfoResponse(new CommonResponse(true, "예약 조회 성공"),
                reservation.getHouse().getAddress().getFull_name(), reservation.getHouse().getAddress().getDetail(),
                reservation.getStart(), reservation.getEnd(), reservation.getAmount());
    }

    @Data
    @AllArgsConstructor
    static class ReservationDetailInfoResponse {
        private CommonResponse commonResponse;

        private String full_name;
        private String detail;
        private LocalDateTime start;
        private LocalDateTime end;
        private int amount;
    }

    /**
     * 주거 예약 취소
     */
    @PostMapping("/reservations/{id}/cancel")
    public CommonResponse cancelReservation(@PathVariable("id") Long id) {
        reservationService.cancel(id);
        return new CommonResponse(true, "예약 취소 성공");
    }


}
