import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import javax.sound.sampled.*;
import java.io.File;
import java.util.Scanner;

public class Main {
    private static final String ASCII_CHARS = " .:-=+*#%@";
    private static Clip audioClip;
    private static long videoStartTime;

    static {
        try {
            System.load("/usr/lib/jni/libopencv_java454d.so");
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Failed to load OpenCV library");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    playVideoWithAudio("video/video.mp4", "audio/audio.wav");
                    break;
                case "2":
                    testASCIIConversion();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice!");
            }

            if (!choice.equals("3")) {
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    private static void showMenu() {
        clearConsole();
        System.out.println("1. Play video");
        System.out.println("2. Test display");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
    }

    private static void playVideoWithAudio(String videoPath, String audioPath) {
        if (!checkFile(videoPath) || !checkFile(audioPath)) return;

        System.out.println("Starting playback...");

        if (!loadAudio(audioPath)) {
            System.out.println("Failed to load audio");
            return;
        }

        videoStartTime = System.currentTimeMillis();
        audioClip.setMicrosecondPosition(0);
        audioClip.start();
        
        playVideoInternal(videoPath, true);
        
        stopAudio();
    }

    private static boolean loadAudio(String audioPath) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(audioPath));
            audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            return true;
        } catch (Exception e) {
            System.out.println("Audio error: " + e.getMessage());
            return false;
        }
    }

    private static void playVideoInternal(String videoPath, boolean withAudio) {
        VideoCapture capture = new VideoCapture(videoPath);

        if (!capture.isOpened()) {
            System.out.println("Error: Cannot open video file");
            return;
        }

        double fps = capture.get(5);
        if (fps <= 0) fps = 24;
        long frameDelay = (long) (1000 / fps);
        int totalFrames = (int) capture.get(7);
        if (totalFrames <= 0) totalFrames = 1;

        double videoDuration = totalFrames / fps;
        double audioDuration = audioClip.getMicrosecondLength() / 1000000.0;
        double syncFactor = audioDuration / videoDuration;

        Mat frame = new Mat();
        int frameCount = 0;

        while (capture.read(frame)) {
            if (frame.empty()) break;

            long frameStartTime = System.currentTimeMillis();
            
            String asciiFrame = convertFrameToASCII(frame);
            clearConsole();
            System.out.println(asciiFrame);

            if (withAudio && audioClip != null && audioClip.isRunning()) {
                double expectedVideoTime = frameCount / fps;
                double expectedAudioTime = expectedVideoTime * syncFactor;
                long expectedAudioMicros = (long) (expectedAudioTime * 1000000);
                long currentAudioMicros = audioClip.getMicrosecondPosition();
                
                if (currentAudioMicros < expectedAudioMicros - 100000) {
                    audioClip.setMicrosecondPosition(expectedAudioMicros);
                }
            }

            double currentTime = (System.currentTimeMillis() - videoStartTime) / 1000.0;
            System.out.printf("Frame: %d/%d | Time: %.1fs", ++frameCount, totalFrames, currentTime);
            if (withAudio) System.out.print(" | Audio: ON");
            System.out.println();

            long processingTime = System.currentTimeMillis() - frameStartTime;
            long adjustedDelay = Math.max(0, frameDelay - processingTime);
            
            try { Thread.sleep(adjustedDelay); } catch (InterruptedException e) { break; }
        }

        capture.release();
    }

    private static String convertFrameToASCII(Mat frame) {
        int asciiWidth = 80;
        int asciiHeight = 30;
        
        Mat resized = new Mat();
        Imgproc.resize(frame, resized, new Size(asciiWidth, asciiHeight));
        
        Mat gray = new Mat();
        Imgproc.cvtColor(resized, gray, Imgproc.COLOR_BGR2GRAY);
        
        Mat blurred = new Mat();
        Imgproc.GaussianBlur(gray, blurred, new Size(3, 3), 0);
        
        Mat enhanced = new Mat();
        blurred.convertTo(enhanced, -1, 1.4, 10);
        
        StringBuilder asciiArt = new StringBuilder();
        
        for (int row = 0; row < enhanced.rows(); row++) {
            for (int col = 0; col < enhanced.cols(); col++) {
                double[] pixel = enhanced.get(row, col);
                int brightness = (int) pixel[0];
                
                int index;
                if (brightness < 15) {
                    index = 0;
                } else if (brightness < 35) {
                    index = 1;
                } else if (brightness < 60) {
                    index = 2;
                } else if (brightness < 85) {
                    index = 3;
                } else if (brightness < 110) {
                    index = 4;
                } else if (brightness < 135) {
                    index = 5;
                } else if (brightness < 160) {
                    index = 6;
                } else if (brightness < 185) {
                    index = 7;
                } else if (brightness < 210) {
                    index = 8;
                } else {
                    index = 9;
                }
                
                asciiArt.append(ASCII_CHARS.charAt(index));
            }
            asciiArt.append("\n");
        }
        
        return asciiArt.toString();
    }

    private static void stopAudio() {
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
            audioClip.close();
        }
    }

    private static void testASCIIConversion() {
        System.out.println("Display test");
        System.out.println("Characters: " + ASCII_CHARS);
        
        String testPattern = "";
        
        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < 60; x++) {
                double gradient = (double) x / 60;
                int brightness = (int) (gradient * 255);
                
                int index;
                if (brightness < 15) index = 0;
                else if (brightness < 35) index = 1;
                else if (brightness < 60) index = 2;
                else if (brightness < 85) index = 3;
                else if (brightness < 110) index = 4;
                else if (brightness < 135) index = 5;
                else if (brightness < 160) index = 6;
                else if (brightness < 185) index = 7;
                else if (brightness < 210) index = 8;
                else index = 9;
                
                testPattern += ASCII_CHARS.charAt(index);
            }
            testPattern += "\n";
        }
        
        System.out.println(testPattern);
        System.out.println("Screen size: 80x30");
    }

    private static boolean checkFile(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    private static void clearConsole() {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }
}