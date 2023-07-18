package peaksoft.house.airbnbb9.s3File.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import peaksoft.house.airbnbb9.s3File.service.S3FileService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class S3FileApi {
    private final S3FileService s3FileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return new ResponseEntity<>(s3FileService.uploadFile(file), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        return new ResponseEntity<>(s3FileService.deleteFile(fileName), HttpStatus.OK);
    }
}
