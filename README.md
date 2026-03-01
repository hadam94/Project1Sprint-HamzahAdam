**How to setup this program?**

Download it, and in your IDE (Intelij or Eclipse), click on import gradle project, and select the entire folder for this project. The IDE will automatically run the build.gradle and set up the necessary libraries
If the IDE does not automatically run the build script, you can run it manually in the command line or click on the build option inside of gradle tasks in the IDE

**How to run the program?**

Simply run the SpeechDemo.java file or the BookingDemo.java file through the IDE or command line. Tests through IDE aswell

**How to use the program?**

When you run the program, you will be prompted to press enter to start speaking, or exit. when you press enter, the microphone will be setup and the program will alert you once that has been set up. When the microphone has been setup, you have 10 seconds to speak your prompt. After that time has passed, the prompt that you have speaken will be shown in the console, and then it will be feeded into the AI model, along with the list of rooms and bookings that have been done, which the AI will generate a response based on. After the AI generates a response (which may take some time due to a large amount of bookings being currently in the server as of 3/1) you will be prompted again if you want to speak or press exit. If you type **exit** (not case sensitive) into the terminal and press enter, the program will terminate.
