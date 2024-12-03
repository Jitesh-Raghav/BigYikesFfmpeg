package com.example.bigyikesffmpeg.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SubtitleGenerator {

    public static File generateAssSubtitleFile(String story) throws IOException {
        File subtitleFile = new File(System.getProperty("java.io.tmpdir"), "subtitles.ass");

        String[] words = story.split("\\s+");
        int startTime = 0; // Start at 0 seconds
        int duration = 1; // Each word displayed for 1 second

        try (FileWriter writer = new FileWriter(subtitleFile)) {
            writer.write("[Script Info]\n");
            writer.write("Title: Word-by-Word Subtitles\n");
            writer.write("ScriptType: v4.00+\n\n");

            writer.write("[V4+ Styles]\n");
            writer.write("Format: Name, Fontname, Fontsize, PrimaryColour, Bold, Italic, Underline, StrikeOut, "
                    + "ScaleX, ScaleY, Spacing, Angle, BorderStyle, Outline, Shadow, Alignment, "
                    + "MarginL, MarginR, MarginV, Encoding\n");
            writer.write("Style: Default,Arial,48,&H00FFFFFF,&H00000000,1,0,0,0,100,100,0,0,1,2,1,5,10,10,10,1\n\n");

            writer.write("[Events]\n");
            writer.write("Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text\n");

            for (String word : words) {
                String start = formatTime(startTime);
                String end = formatTime(startTime + duration);
                writer.write(String.format("Dialogue: 0,%s,%s,Default,,0,0,0,,{\\an5}%s\n", start, end, word));
                startTime += duration;
            }
        }

        return subtitleFile;
    }

    private static String formatTime(int seconds) {
        int hrs = seconds / 3600;
        int mins = (seconds % 3600) / 60;
        int secs = seconds % 60;
        return String.format("%01d:%02d:%02d.%02d", hrs, mins, secs, 0);
    }
}