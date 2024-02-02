package capstoneDesign.houseSharing.controller;

import capstoneDesign.houseSharing.awsS3.S3Service;
import capstoneDesign.houseSharing.domain.BorderLinePhoto;
import capstoneDesign.houseSharing.domain.Reservation;
import capstoneDesign.houseSharing.domain.SimilarityPhotoProvider;
import capstoneDesign.houseSharing.response.CommonResponse;
import capstoneDesign.houseSharing.service.BorderLinePhotoService;
import capstoneDesign.houseSharing.service.ReservationService;
import capstoneDesign.houseSharing.service.SimilarityPhotoProviderService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BorderLinePhotoController {

    private final BorderLinePhotoService borderLinePhotoService;
    private final ReservationService reservationService;
    private final SimilarityPhotoProviderService similarityPhotoProviderService;
    private final S3Service s3Service;

    /**
     * reservationId를 토대로 borderLinePhotos, providerPhotos 가져오기(유사도 검사를 위함)
     */
    @GetMapping("/reservations/{id}/borderLinePhotos")
    public DownloadResponse downloadPhotos(@PathVariable("id") Long reservationId) throws IOException {
        Reservation reservation = reservationService.findReservation(reservationId);
        List<SimilarityPhotoProvider> photos = similarityPhotoProviderService.findPhotosByHouse(reservation.getHouse().getId());

        List<byte[]> borderLinePhotos = new ArrayList<>();
        List<byte[]> providerPhotos = new ArrayList<>();

        for (SimilarityPhotoProvider photo : photos) {
            BorderLinePhoto borderLinePhoto = borderLinePhotoService.findPhotoBySimilarityPhotoProvider(photo.getId());

            borderLinePhotos.add(s3Service.download(borderLinePhoto.getUrl()));
            providerPhotos.add(s3Service.download(photo.getUrl()));
        }

        return new DownloadResponse(new CommonResponse(true, "경계선 사진들, 유사도 검사용 사진들 가져오기 성공"), photos.size(), borderLinePhotos, providerPhotos);
    }

    @Data
    @AllArgsConstructor
    static class DownloadResponse {
        private CommonResponse commonResponse;

        private int size;
        private List<byte[]> borderLinePhotos;
        private List<byte[]> providerPhotos;
    }
}
