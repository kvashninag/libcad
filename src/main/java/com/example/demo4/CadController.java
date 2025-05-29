package com.example.demo4;

import com.example.demo4.service.CadServiceRequest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1")
public class CadController {

    @PostMapping(value = "/image")
    ResponseEntity<String> create() {
        return ResponseEntity.ok(CadServiceRequest.createImageExt());
    }

    @GetMapping(value = "/image/{imageId}/jpeg")
    ResponseEntity<FileSystemResource> jpeg(@PathVariable String imageId) {
        return ResponseEntity.ok(CadServiceRequest.exportImageJpegExt(imageId));
    }

    @DeleteMapping(value = "/image/{imageId}")
    ResponseEntity<?> close(@PathVariable String imageId) {
        CadServiceRequest.closeImageExt(imageId);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
