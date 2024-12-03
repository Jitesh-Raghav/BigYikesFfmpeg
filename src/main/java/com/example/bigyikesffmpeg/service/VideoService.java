package com.example.bigyikesffmpeg.service;

import com.example.bigyikesffmpeg.util.SubtitleGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class VideoService {

    public File processVideo(MultipartFile videoFile, String story) throws IOException, InterruptedException {
        // Save the uploaded video
        File inputVideo = saveMultipartFile(videoFile, "input.mp4");

        // Generate ASS subtitle file
        File subtitleFile = SubtitleGenerator.generateAssSubtitleFile(story);

        // Process video with FFmpeg
        File outputVideo = burnSubtitles(inputVideo, subtitleFile);

        return outputVideo;
    }

    private File saveMultipartFile(MultipartFile file, String filename) throws IOException {
        File tempFile = new File(System.getProperty("java.io.tmpdir"), filename);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }
        return tempFile;
    }

    private File burnSubtitles(File video, File subtitles) throws IOException, InterruptedException {
        File outputVideo = new File(System.getProperty("java.io.tmpdir"), "output.mp4");

        String command = String.format("ffmpeg -i %s -vf ass=%s -c:v libx264 -preset ultrafast -crf 18 %s",
                video.getAbsolutePath(),
                subtitles.getAbsolutePath(),
                outputVideo.getAbsolutePath());

        Process process = new ProcessBuilder(command.split(" "))
                .redirectErrorStream(true)
                .start();

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg process failed.");
        }

        return outputVideo;
    }
}