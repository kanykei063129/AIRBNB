package peaksoft.house.airbnbb9.s3File.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Getter
@Setter
public class S3FileService {
    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("${prefix-for-file-link}")
    private String prefixForFileLink;

    private final AmazonS3 s3Client;

    public Map<String, String> uploadFile(MultipartFile file) {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.addUserMetadata("Content-Type", file.getContentType());
            metadata.addUserMetadata("Content-Length", String.valueOf(file.getSize()));
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Map.of("Link", prefixForFileLink + fileName);
    }

    public Map<String, String> deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return Map.of("message", fileName + " removed ...");
    }
}