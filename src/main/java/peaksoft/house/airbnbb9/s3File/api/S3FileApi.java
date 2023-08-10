package peaksoft.house.airbnbb9.s3File.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import peaksoft.house.airbnbb9.s3File.service.S3FileService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "S3-File API", description = "API for S3-File management")
@PreAuthorize("hasAuthority('ADMIN')")
public class S3FileApi {
    private final S3FileService s3FileService;

    @Operation(summary = "Upload file", description = "This is upload file ")
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    String upload(@RequestParam(value = "file") MultipartFile multipartFile) throws IOException {
        return s3FileService.upload(multipartFile);
    }

    @Operation(summary = "Delete file", description = "This is delete file ")
    @DeleteMapping
    Map<String, String> delete(@RequestParam String fileLink) {
        return s3FileService.delete(fileLink);
    }
}
