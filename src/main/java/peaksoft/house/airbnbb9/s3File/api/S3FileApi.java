package peaksoft.house.airbnbb9.s3File.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import peaksoft.house.airbnbb9.s3File.service.S3FileService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "S3-File API", description = "API for S3-File management")
public class S3FileApi {
    private final S3FileService s3FileService;

    @Operation(summary = "Upload file", description = "This is upload file ")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> uploadFile(@RequestParam MultipartFile file) {
        return s3FileService.uploadFile(file);
    }
    @Operation(summary = "Delete file", description = "This is delete file ")
    @DeleteMapping
    public Map<String, String> deleteFile(@RequestParam String fileName) {
        return s3FileService.deleteFile(fileName);
    }

}