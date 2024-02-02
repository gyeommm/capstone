package capstoneDesign.houseSharing.controller;

import capstoneDesign.houseSharing.awsS3.S3Service;
import capstoneDesign.houseSharing.domain.*;
import capstoneDesign.houseSharing.response.CommonResponse;
import capstoneDesign.houseSharing.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TrustScoreService trustScoreService;
    private final HouseService houseService;
    private final HousePhotoService housePhotoService;
    private final SimilarityPhotoProviderService similarityPhotoProviderService;
    private final BorderLinePhotoService borderLinePhotoService;
    private final S3Service s3Service;
    private final ReservationService reservationService;

    /**
     * 회원가입
     */
    @PostMapping("/join")
    public JoinMemberResponse joinMember(@RequestBody @Valid JoinMemberRequest request) {
        Member member = Member.createMember(request.getName(), request.isMan(), request.getBirth(),
                request.getLogin_id(), request.getLogin_pw());

        memberService.join(member);

        return new JoinMemberResponse(true, "회원가입 성공");
    }

    @Data
    static class JoinMemberRequest {

        @Size(min = 2, max = 20, message = "The length of name must be 2 ~ 20")
        @NotBlank(message = "name must not be blank")
        private String name;

        //@NotBlank(message = "sex must not be blank")   // boolean은 기본적으로 null을 허용 안함
        private boolean man;

        @NotNull(message = "birth must not be null")
        private LocalDate birth;

        @Size(min = 3, max = 20, message = "The length of login_id must be 3 ~ 20")
        @NotBlank(message = "login_id must not be blank")
        private String login_id;

        @Size(min = 5, max = 20, message = "The length of login_pw must be 5 ~ 20")
        @NotBlank(message = "login_pw must not be blank")
        private String login_pw;
    }

    @Data
    @AllArgsConstructor
    public static class JoinMemberResponse {

        private boolean success;
        private String message;
    }

    /**
     * 회원 정보 출력
     */
    @GetMapping("/members/{id}")
    public MemberInfoResponse printMemberInfo(@PathVariable("id") Long id) {
        Member member = memberService.findMember(id);
        return new MemberInfoResponse(member.getName(), member.isMan(), member.getBirth(), member.getAverageScore());
    }

    @Data
    @AllArgsConstructor
    static class MemberInfoResponse {
        private String name;
        private boolean man;
        private LocalDate birth;
        private int averageScore;
    }

    /**
     * 회원에게 신뢰도 점수 추가
     */
    @PostMapping("/reservations/{id}/score")
    public CommonResponse addTrustScore(@PathVariable("id") Long reservationId,
                                               @RequestBody @Valid AddTrustScoreRequest request) {
        Reservation reservation = reservationService.findReservation(reservationId);
        boolean isAlreadyTest = trustScoreService.isAlreadyTestByReservation(reservation.getId());
        if (isAlreadyTest == true) {
            return new CommonResponse(false, "이미 등록된 신뢰도 점수가 존재합니다.");
        }

        Member member = memberService.findMember(reservation.getMember().getId());

        TrustScore trustScore = TrustScore.createTrustScore(member, reservation, request.getScore());
        trustScoreService.appendTrustScore(trustScore);
        int averageScore = trustScoreService.calculateAverage(member.getId());
        memberService.updateAverageScore(member.getId(), averageScore);

        return new CommonResponse(true, "신뢰도 점수 추가 성공");
    }

    @Data
    static class AddTrustScoreRequest {
        //@NotBlank(message = "score must not be blank")
        private int score;
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/members/{id}/withdraw")
    public CommonResponse withdrawMember(@PathVariable("id") Long id) {
        // 회원이 등록한 주거를 찾고, 그 주거마다의 예약이 가지고있는 주거id를 null로 업데이트
        List<House> houses = houseService.findRegistersByMember(id);
        for (House house : houses) {
            List<Reservation> reservations = reservationService.findReservationsByHouse(house.getId());
            for (Reservation reservation : reservations) {
                reservation.setHouse(null);
            }

            List<HousePhoto> housePhotos = housePhotoService.findPhotosByHouse(house.getId());
            for (HousePhoto photo : housePhotos) {
                s3Service.delete(photo.getUrl());
            }

            List<SimilarityPhotoProvider> providerPhotos = similarityPhotoProviderService.findPhotosByHouse(house.getId());
            for (SimilarityPhotoProvider photo : providerPhotos) {
                s3Service.delete(photo.getUrl());

                BorderLinePhoto borderLinePhoto = borderLinePhotoService.findPhotoBySimilarityPhotoProvider(photo.getId());
                s3Service.delete(borderLinePhoto.getUrl());
            }
        }

        memberService.withdraw(id);
        return new CommonResponse(true, "회원 탈퇴 성공");
    }
}
