package capstoneDesign.houseSharing.service;

import capstoneDesign.houseSharing.domain.BorderLinePhoto;
import capstoneDesign.houseSharing.domain.SimilarityPhotoProvider;
import capstoneDesign.houseSharing.repository.BorderLinePhotoRepository;
import capstoneDesign.houseSharing.repository.SimilarityPhotoProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BorderLinePhotoService {

    private final BorderLinePhotoRepository borderLinePhotoRepository;
    private final SimilarityPhotoProviderRepository similarityPhotoProviderRepository;

    @Transactional
    public Long addPhoto(BorderLinePhoto borderLinePhoto) {
        return borderLinePhotoRepository.save(borderLinePhoto);
    }

    public BorderLinePhoto findPhoto(Long id) {
        return borderLinePhotoRepository.findOne(id);
    }

    public BorderLinePhoto findPhotoBySimilarityPhotoProvider(Long similarityPhotoProviderId) {
        return borderLinePhotoRepository.findOneBySimilarityPhotoProvider(similarityPhotoProviderId);
    }

    @Transactional
    public void updatePhoto(Long similarityPhotoProviderId, BorderLinePhoto borderLinePhoto) {
        SimilarityPhotoProvider similarityPhotoProvider = similarityPhotoProviderRepository.findOne(similarityPhotoProviderId);
        similarityPhotoProvider.setBorderLinePhoto(borderLinePhoto);
    }
}
