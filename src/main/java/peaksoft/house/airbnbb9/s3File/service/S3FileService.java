package peaksoft.house.airbnbb9.s3File.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Component
public class S3FileService {
    @Value("${aws_bucket_name}")
    private String bucketName;
    @Value("${aws_bucket_path}")
    private String bucketPath;
    private final S3Client s3Client;
    public String upload(MultipartFile file) throws IOException {
        String key = System.currentTimeMillis() + file.getOriginalFilename();
        PutObjectRequest p = PutObjectRequest.builder()
                .bucket(bucketName)
                .contentType("jpeg")
                .contentType("png")
                .contentType("ogg")
                .contentType("mp3")
                .contentType("mpeg")
                .contentType("ogg")
                .contentType("m4a")
                .contentType("oga")
                .contentType("pdf")
                .contentLength(file.getSize())
                .key(key)
                .build();
        s3Client.putObject(p, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        Map.of("Link", bucketPath + key);
        return bucketPath+key;
    }
    public Map<String, String> delete(String fileLink) {
        try {
            String key = fileLink.substring(bucketPath.length());
            s3Client.deleteObject(dor -> dor.bucket(bucketName).key(key).build());
        } catch (S3Exception e) {
            throw new IllegalStateException(e.awsErrorDetails().errorMessage());
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
        return Map.of(
                "message", fileLink + " has been deleted."
        );
    }
}