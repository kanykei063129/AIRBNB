package peaksoft.house.airbnbb9.s3File.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import peaksoft.house.airbnbb9.s3File.service.S3FileService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "S3-File API", description = "API for S3-File management")
@PreAuthorize("hasAuthority('ADMIN')")
public class S3FileApi {
    private final S3FileService s3FileService;

    @Operation(summary = "Upload file", description = "Upload file to database")
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam(name = "file", required = false) MultipartFile file) throws IOException {
        return new ResponseEntity<>(s3FileService.uploadFile(file), HttpStatus.OK);
    }

    @DeleteMapping("/{fileName}")
    @Operation(summary = "Deleted file", description = "deleted s3 file")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        return new ResponseEntity<>(s3FileService.deleteFile(fileName), HttpStatus.OK);
    }
}
