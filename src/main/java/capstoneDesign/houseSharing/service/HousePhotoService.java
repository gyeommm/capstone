package capstoneDesign.houseSharing.service;

import capstoneDesign.houseSharing.domain.House;
import capstoneDesign.houseSharing.domain.HousePhoto;
import capstoneDesign.houseSharing.repository.HousePhotoRepository;
import capstoneDesign.houseSharing.repository.HouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HousePhotoService {

    private final HousePhotoRepository housePhotoRepository;
    private final HouseRepository houseRepository;

    @Transactional
    public Long addPhoto(HousePhoto housePhoto) {
        return housePhotoRepository.save(housePhoto);
    }

    public List<HousePhoto> findPhotosByHouse(Long houseId) {
        return housePhotoRepository.findAllByHouse(houseId);
    }

    @Transactional
    public void updatePhotos(Long houseId, List<HousePhoto> housePhotos) {
        House house = houseRepository.findOne(houseId);
        house.setHousePhotos(housePhotos);
    }

}
