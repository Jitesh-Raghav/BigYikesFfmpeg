package com.example.bigyikesffmpeg.controller;

import com.example.bigyikesffmpeg.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {

    @Autowired
    private VideoService videoProcessingService;

    @PostMapping("/process")
    public ResponseEntity<FileSystemResource> processVideo(
            @RequestParam("video") MultipartFile videoFile,
            @RequestParam("story") String story
    ) throws IOException, InterruptedException {
        File processedVideo = videoProcessingService.processVideo(videoFile, story);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"output.mp4\"")
                .body(new FileSystemResource(processedVideo));
    }
}
