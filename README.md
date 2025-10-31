# ASCII Video Player (Java + OpenCV)

## Overview
A Java-based terminal program that plays videos as ASCII art synchronized with audio.  
It was built as a personal learning project to practice multithreading, file handling, and OpenCV integration — all executed through the Linux terminal using a `.sh` script.

---

## Features
- Converts video frames to ASCII characters based on brightness levels.  
- Plays synchronized audio and video in real time.  
- Uses OpenCV for video frame extraction and processing.  
- Demonstrates multithreading for playback synchronization.  
- Fully Linux-ready via a shell script (no IDE required).  

---

## Technologies Used
- **Java (JDK 17+)**  
- **OpenCV (Java bindings)**  
- **javax.sound.sampled** for audio  
- **Multithreading and timing control**  
- **ASCII brightness mapping**  

---

## Usage (Linux)
```bash
# 1. Install dependencies
sudo apt install libopencv-dev

# 2. Make the shell script executable
chmod +x run.sh

# 3. Run the program
./run.sh

# Example run.sh file
#!/bin/bash
# Compile Java files with OpenCV
javac -cp /usr/share/java/opencv.jar:. src/*.java
# Run the main class
java -cp /usr/share/java/opencv.jar:. src.Main
```

---

## Windows Users
This setup is designed for Linux.  
Windows users can clone the repository and create their own `.bat` or executable file using equivalent compile and run commands.  
The core Java logic remains platform-independent.

---

## Notes
This project was created as a learning exercise to connect theory with practical coding.  
It explores multithreading, timing synchronization, and ASCII rendering concepts in a simple yet creative way.  
The current implementation is functional but not optimized — future versions will focus on performance and cleaner structure.

---

## Author
**Syed Hassan Ali Bukhari**  
- GitHub: [@TheHassanBukhari](https://github.com/TheHassanBukhari)  
- LinkedIn: [Syed Hassan Ali Bukhari](https://www.linkedin.com/in/syed-hassan-ali-bukhari-1225b9338/)  
- Email: [thehassanbukhari@gmail.com](mailto:thehassanbukhari@gmail.com)

---

## License
This project is licensed under the [MIT License](./LICENSE).
