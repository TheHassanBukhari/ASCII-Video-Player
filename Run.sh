#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# Check for required files
if [ ! -f "src/Main.java" ]; then
    echo "ERROR: src/Main.java not found"
    exit 1
fi

if [ ! -f "video/video.mp4" ] && [ ! -f "video.mp4" ]; then
    echo "ERROR: No video file found"
    exit 1
fi

if [ ! -f "audio/audio.wav" ] && [ ! -f "audio.wav" ]; then
    echo "ERROR: No audio file found"
    exit 1
fi

# Create bin directory if needed
mkdir -p bin

# Compile and run (completely silent compilation)
javac -cp "/usr/share/java/opencv.jar:src" -d bin src/Main.java >/dev/null 2>&1
java -cp "/usr/share/java/opencv.jar:bin" -Djava.library.path=/usr/lib/jni --enable-native-access=ALL-UNNAMED Main
