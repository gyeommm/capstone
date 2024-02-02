package capstoneDesign.houseSharing.controller;

import capstoneDesign.houseSharing.awsS3.S3Service;
import capstoneDesign.houseSharing.domain.*;
import capstoneDesign.houseSharing.response.CommonResponse;
import capstoneDesign.houseSharing.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;
    private final MemberService memberService;
    private final ReviewService reviewService;
    private final ShareDateTimeService shareDateTimeService;
    private final HousePhotoService housePhotoService;
    private final SimilarityPhotoProviderService similarityPhotoProviderService;
    private final BorderLinePhotoService borderLinePhotoService;
    private final S3Service s3Service;
    private final ReservationService reservationService;

    @Value("${kakao.url}")
    private String GEOCODE_URL;
    @Value("${kakao.rest-api-key}")
    private String GEOCODE_KEY;

    /**
     * 주거 등록
     */
    @PostMapping("/register")
    public CommonResponse registerHouse(Long memberId,
                                        @RequestPart @Valid RegisterHouseRequest request,
                                        @RequestPart List<MultipartFile> housePhotos,
                                        @RequestPart List<MultipartFile> providerPhotos,
                                        @RequestPart List<MultipartFile> borderLinePhotos) throws Exception {
        if (request == null) {
            return new CommonResponse(false, "주거 정보가 있어야합니다.");
        }
        if (housePhotos == null) {
            return new CommonResponse(false, "주거 사진이 있어야합니다.");
        }
        if (providerPhotos == null) {
            return new CommonResponse(false, "유사도 검사용 사진(제공자)가 있어야합니다.");
        }
        if (borderLinePhotos == null) {
            return new CommonResponse(false, "경계선 사진이 있어야합니다.");
        }
        if (providerPhotos.size() != borderLinePhotos.size()) {
            return new CommonResponse(false, "유사도 검사용 사진(제공자)과 경계선 사진은 개수가 같아야합니다.");
        }

        URL obj = new URL(GEOCODE_URL + URLEncoder.encode(request.getFull_address(), StandardCharsets.UTF_8));

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", GEOCODE_KEY);
        con.setRequestProperty("content-type", "application/json");
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setDefaultUseCaches(false);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        String jsonString = response.toString();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(jsonString);
        JSONArray documentsArray = (JSONArray) jsonObj.get("documents");
        JSONObject roadAddressObj = (JSONObject) ((JSONObject) documentsArray.get(0)).get("road_address");

        Address address = new Address(roadAddressObj.get("zone_no").toString(),
                roadAddressObj.get("address_name").toString(), request.getDetail());
        Coordinate coordinate = new Coordinate(roadAddressObj.get("y").toString(), roadAddressObj.get("x").toString());

        Member member = memberService.findMember(memberId);

        House house = House.createHouse(member, request.getContent(), request.getPrice(), address, coordinate);
        Long houseId = houseService.register(house);

        if (request.getStart().size() == request.getEnd().size()) {

            // 입력 받은 시작 시각과 종료 시각 검증
            int size = request.getStart().size();
            for (int index = 0; index < size; index++) {

                LocalDateTime start = request.getStart().get(index);
                LocalDateTime end = request.getEnd().get(index);

                if (end.isBefore(start)) {
                    return new CommonResponse(false, "'시작일시 < 종료일시' 이어야 합니다.");
                }
            }

            for (int index = 0; index < size; index++) {
                ShareDateTime shareDateTime = ShareDateTime.createShareDateTime(house, request.getStart().get(index), request.getEnd().get(index));
                shareDateTimeService.add(shareDateTime);
            }
        }

        for (MultipartFile photo : housePhotos) {
            String url = s3Service.upload(photo, houseId + "/housePhotos/");

            HousePhoto housePhoto = HousePhoto.createHousePhoto(house, url);
            housePhotoService.addPhoto(housePhoto);
        }

        for (int index = 0; index < providerPhotos.size(); index++) {
            String url = s3Service.upload(providerPhotos.get(index), houseId + "/similarityPhotosProvider/");

            SimilarityPhotoProvider similarityPhotoProvider = SimilarityPhotoProvider.createSimilarityPhotoProvider(house, url);
            similarityPhotoProviderService.addPhoto(similarityPhotoProvider);

            String url2 = s3Service.upload(borderLinePhotos.get(index), houseId + "/borderLinePhotos/");

            BorderLinePhoto borderLinePhoto = BorderLinePhoto.createBorderLinePhoto(similarityPhotoProvider, url2);
            borderLinePhotoService.addPhoto(borderLinePhoto);
        }

        return new CommonResponse(true, "주거 등록 성공");
    }

    @Data
    static class RegisterHouseRequest {
        private String content;

        //@NotBlank(message = "price must not be blank")
        private int price;

        @NotBlank(message = "address must not be blank")
        private String full_address;

        @NotBlank(message = "detail_address must not be blank")
        private String detail;

        @NotNull(message = "start(localDateTime) must not be null")
        private List<LocalDateTime> start;

        @NotNull(message = "end(localDateTime) must not be null")
        private List<LocalDateTime> end;
    }

    /**
     * 주거 상세 페이지
     */
    @GetMapping("/houses/{id}")
    public HouseDetailInfoResponse houseDetail(@PathVariable("id") Long id) throws IOException {
        House house = houseService.findHouse(id);
        List<byte[]> photos = new ArrayList<>();

        for (HousePhoto photo : house.getHousePhotos()) {
            photos.add(s3Service.download(photo.getUrl()));
        }

        return new HouseDetailInfoResponse(new CommonResponse(true, "주거 상세 정보 조회 성공"),
                photos, house.getAddress().getFull_name(), house.getAddress().getDetail(),
                house.getContent(), house.getPrice(), house.getAverageScore());
    }

    @Data
    @AllArgsConstructor
    static class HouseDetailInfoResponse {
        private CommonResponse commonResponse;

        private List<byte[]> photos;
        private String full_name;
        private String detail;
        private String content;
        private int price;
        private double averageScore;
    }

    /**
     * 특정 회원이 등록한 주거 list
     */
    @GetMapping("/members/{id}/houses")
    public HouseInfoByMemberListResponse registerList(@PathVariable("id") Long memberId) throws IOException {
        List<House> houses = houseService.findRegistersByMember(memberId);
        List<HouseInfoByMemberDTO> registers = new ArrayList<>();

        for (House house : houses) {
            byte[] photo = s3Service.download(house.getHousePhotos().get(0).getUrl());

            registers.add(new HouseInfoByMemberDTO(house.getId(), photo,
                    house.getAddress().getFull_name(), house.getAddress().getDetail(),
                    house.getPrice(), house.getAverageScore()));
        }

        return new HouseInfoByMemberListResponse(new CommonResponse(true, "특정 회원이 등록한 주거 list 조회 성공"),
                registers.size(), registers);
    }

    @Data
    @AllArgsConstructor
    static class HouseInfoByMemberDTO {
        private Long id;
        private byte[] photo;
        private String full_name;
        private String detail;
        private int price;
        private double averageScore;
    }

    @Data
    @AllArgsConstructor
    static class HouseInfoByMemberListResponse {
        private CommonResponse commonResponse;

        private int size;
        private List<HouseInfoByMemberDTO> houseList;
    }

    /**
     * 모든 주거 정보1(지도에 표현을 위한)
     */
    @GetMapping("/houses1")
    public AllHouseListResponse1 AllHouses1() {
        List<House> houses = houseService.findAllHouse();
        List<AllHouseInfoDTO1> responses = new ArrayList<>();

        for (House house : houses) {
            responses.add(new AllHouseInfoDTO1(house.getId(), house.getAddress().getFull_name(),
                    house.getPrice(), house.getAverageScore(), house.getCoordinate().getLatitude(),
                    house.getCoordinate().getLongitude()));
        }

        return new AllHouseListResponse1(new CommonResponse(true, "모든 주거 조회 성공"),
                responses.size(), responses);
    }

    @Data
    @AllArgsConstructor
    static class AllHouseInfoDTO1 {
        private Long id;
        private String full_name;
        private int price;
        private double averageScore;
        private String latitude;
        private String longitude;
    }

    @Data
    @AllArgsConstructor
    static class AllHouseListResponse1 {
        private CommonResponse commonResponse;

        private int size;
        private List<AllHouseInfoDTO1> houseList;
    }

    /**
     * 모든 주거 정보2(목록에 표현을 위한)
     */
    @GetMapping("/houses2")
    public AllHouseListResponse2 AllHouses2() throws IOException {
        List<House> houses = houseService.findAllHouse();
        List<AllHouseInfoDTO2> responses = new ArrayList<>();

        for (House house : houses) {
            byte[] photo = s3Service.download(house.getHousePhotos().get(0).getUrl());

            responses.add(new AllHouseInfoDTO2(house.getId(), photo,
                    house.getAddress().getFull_name(), house.getAddress().getDetail(),
                    house.getPrice(), house.getAverageScore()));
        }

        return new AllHouseListResponse2(new CommonResponse(true, "모든 주거 조회 성공"),
                responses.size(), responses);
    }

    @Data
    @AllArgsConstructor
    static class AllHouseInfoDTO2 {
        private Long id;
        private byte[] photo;
        private String full_name;
        private String detail;
        private int price;
        private double averageScore;
    }

    @Data
    @AllArgsConstructor
    static class AllHouseListResponse2 {
        private CommonResponse commonResponse;

        private int size;
        private List<AllHouseInfoDTO2> houseList;
    }

    /**
     * 등록된 주거 삭제
     */
    @DeleteMapping("/houses/{id}/delete")
    public CommonResponse delete(@PathVariable("id") Long houseId) {
        List<Reservation> reservations = reservationService.findReservationsByHouse(houseId);
        for (Reservation reservation : reservations) {
            reservation.setHouse(null);
        }

        List<HousePhoto> housePhotos = housePhotoService.findPhotosByHouse(houseId);
        for (HousePhoto photo : housePhotos) {
            s3Service.delete(photo.getUrl());
        }

        List<SimilarityPhotoProvider> providerPhotos = similarityPhotoProviderService.findPhotosByHouse(houseId);
        for (SimilarityPhotoProvider photo : providerPhotos) {
            s3Service.delete(photo.getUrl());

            BorderLinePhoto borderLinePhoto = borderLinePhotoService.findPhotoBySimilarityPhotoProvider(photo.getId());
            s3Service.delete(borderLinePhoto.getUrl());
        }

        houseService.deleteHouse(houseId);
        return new CommonResponse(true, "등록된 주거 삭제 성공");
    }

    /**
     * 리뷰 등록
     */
    @PostMapping("/reservations/{id}/review")
    public CommonResponse review(@PathVariable("id") Long reservationId, @RequestBody @Valid ReviewRequest request) {

        Reservation reservation = reservationService.findReservation(reservationId);
        boolean isReviewWritten = reviewService.isReviewWrittenByReservation(reservation.getId());
        if (isReviewWritten == true) {
            return new CommonResponse(false, "이미 등록된 리뷰가 존재합니다.");
        }

        House house = houseService.findHouse(reservation.getHouse().getId());

        Review review = Review.createReview(house, reservation, request.getStar(), request.getContent());
        reviewService.review(review);

        double averageScore = reviewService.calculateAverage(house.getId());
        averageScore = Math.round(averageScore * 100) / 100.0;
        houseService.updateAverageScore(house.getId(), averageScore);

        return new CommonResponse(true, "리뷰 등록 성공");
    }

    @Data
    static class ReviewRequest {
        //@NotBlank(message = "star must not be blank")
        private double star;

        private String content;
    }

    /**
     * 특정 주거의 리뷰 list
     */
    @GetMapping("/houses/{id}/reviews")
    public ReviewListResponse reviewList(@PathVariable("id") Long houseId) {
        List<Review> reviews = reviewService.findReviewsByHouse(houseId);
        List<ReviewListDTO> responses = new ArrayList<>();

        for (Review review : reviews) {
            responses.add(new ReviewListDTO(review.getStar(), review.getContent()));
        }

        return new ReviewListResponse(new CommonResponse(true, "주거 list 조회 성공"),
                responses.size(), responses);
    }

    @Data
    @AllArgsConstructor
    static class ReviewListDTO {
        private double star;
        private String content;
    }

    @Data
    @AllArgsConstructor
    static class ReviewListResponse {
        private CommonResponse commonResponse;

        private int size;
        private List<ReviewListDTO> reviewList;
    }

    /**
     * 시작, 종료 시간으로 주거 검색(지도에 표현을 위한)
     */
    @GetMapping("/search1")
    public searchHouseListResponse1 search1(LocalDateTime start, LocalDateTime end) throws IOException {
        if (end.isBefore(start)) {
            return new searchHouseListResponse1(new CommonResponse(false, "'시작일시 < 종료일시' 이어야 합니다."),
                    0, null);
        }

        List<House> houses = shareDateTimeService.search(start, end);
        List<searchHouseDTO1> responses = new ArrayList<>();
        for (House house : houses) {
            responses.add(new searchHouseDTO1(house.getId(), house.getAddress().getFull_name(),
                    house.getPrice(), house.getAverageScore(), house.getCoordinate().getLatitude(),
                    house.getCoordinate().getLongitude()));
        }

        return new searchHouseListResponse1(new CommonResponse(true, "주거 검색 성공"),
                responses.size(), responses);
    }

    @Data
    @AllArgsConstructor
    static class searchHouseDTO1 {
        private Long id;
        private String full_name;
        private int price;
        private double averageScore;
        private String latitude;
        private String longitude;
    }

    @Data
    @AllArgsConstructor
    static class searchHouseListResponse1 {
        private CommonResponse commonResponse;

        private int size;
        private List<searchHouseDTO1> houseList;
    }


    /**
     * 시작, 종료 시간으로 주거 검색(목록에 표현을 위한)
     */
    @GetMapping("/search2")
    public searchHouseListResponse2 search2(LocalDateTime start, LocalDateTime end) throws IOException {
        if (end.isBefore(start)) {
            return new searchHouseListResponse2(new CommonResponse(false, "'시작일시 < 종료일시' 이어야 합니다."),
                    0, null);
        }

        List<House> houses = shareDateTimeService.search(start, end);
        List<searchHouseDTO2> responses = new ArrayList<>();
        for (House house : houses) {
            byte[] photo = s3Service.download(house.getHousePhotos().get(0).getUrl());

            responses.add(new searchHouseDTO2(house.getId(), photo, house.getAddress().getFull_name(),
                    house.getAddress().getDetail(), house.getPrice(), house.getAverageScore()));
        }

        return new searchHouseListResponse2(new CommonResponse(true, "주거 검색 성공"),
                responses.size(), responses);
    }

    @Data
    @AllArgsConstructor
    static class searchHouseDTO2 {
        private Long id;
        private byte[] photo;
        private String full_name;
        private String detail;
        private int price;
        private double averageScore;
    }

    @Data
    @AllArgsConstructor
    static class searchHouseListResponse2 {
        private CommonResponse commonResponse;

        private int size;
        private List<searchHouseDTO2> houseList;
    }

}