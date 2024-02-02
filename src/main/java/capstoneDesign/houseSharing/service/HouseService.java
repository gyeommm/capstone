package capstoneDesign.houseSharing.service;

import capstoneDesign.houseSharing.awsS3.S3Service;
import capstoneDesign.houseSharing.domain.*;
import capstoneDesign.houseSharing.repository.HouseRepository;
import capstoneDesign.houseSharing.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HouseService {

    private final HouseRepository houseRepository;
    private final ReservationService reservationService;
    private final HousePhotoService housePhotoService;
    private final SimilarityPhotoProviderService similarityPhotoProviderService;
    private final BorderLinePhotoService borderLinePhotoService;
    private final S3Service s3Service;


    @Transactional

    public Long register(House house) {
        return houseRepository.save(house);
    }

    public House findHouse(Long id) {
        return houseRepository.findOne(id);
    }

    public List<House> findRegistersByMember(Long memberId) {
        return houseRepository.findAllByMember(memberId);
    }

    public List<House> findAllHouse() {
        return houseRepository.findAll();
    }

    @Transactional
    public void updateHouse(Long id, String content, int price, Address address) {
        House house = findHouse(id);
        house.setContent(content);
        house.setPrice(price);
        house.setAddress(address);
    }

    @Transactional
    public void updateAverageScore(Long houseId, double average) {
        House house = findHouse(houseId);
        house.setAverageScore(average);
    }

    @Transactional
    public void deleteHouse(Long id) {
        houseRepository.deleteOne(id);
    }
}
