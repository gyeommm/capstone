package capstoneDesign.houseSharing.service;

import capstoneDesign.houseSharing.domain.House;
import capstoneDesign.houseSharing.domain.SimilarityPhotoProvider;
import capstoneDesign.houseSharing.repository.HouseRepository;
import capstoneDesign.houseSharing.repository.SimilarityPhotoProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SimilarityPhotoProviderService {

    private final SimilarityPhotoProviderRepository similarityPhotoProviderRepository;
    private final HouseRepository houseRepository;

    @Transactional
    public Long addPhoto(SimilarityPhotoProvider similarityPhotoProvider) {
        return similarityPhotoProviderRepository.save(similarityPhotoProvider);
    }

    public List<SimilarityPhotoProvider> findPhotosByHouse(Long houseId) {
        return similarityPhotoProviderRepository.findAllByHouse(houseId);
    }

    @Transactional
    public void updatePhotos(Long houseId, List<SimilarityPhotoProvider> similarityPhotosProvider) {
        House house = houseRepository.findOne(houseId);
        house.setSimilarityPhotosProvider(similarityPhotosProvider);
    }
}
