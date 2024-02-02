package capstoneDesign.houseSharing.awsS3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile object, String folder) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(object.getContentType());
        objectMetadata.setContentLength(object.getSize());

        String originalFileName = object.getOriginalFilename();
        int index = Objects.requireNonNull(originalFileName).lastIndexOf(".");
        String contentType = originalFileName.substring(index + 1);

        String storeFileName = UUID.randomUUID() + "." + contentType;   // 저장될 파일명
        String url = "houses/" + folder + storeFileName;                           // 저장 경로

        try (InputStream inputStream = object.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, url, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        }

        return url;
    }

    public byte[] download(String url) throws IOException {

        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucket, url));
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        /*  로컬에 저장하는 코드
        File file = new File(name);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        outputStream.close();
        */

        return bytes;
    }

    public void delete(String url) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, url));
    }
}
