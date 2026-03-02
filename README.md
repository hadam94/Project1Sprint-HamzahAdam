**How to setup this program?**

Download it, and in your IDE (Intelij or Eclipse), click on import gradle project, and select the entire folder for this project. The IDE will automatically run the build.gradle and set up the necessary libraries
If the IDE does not automatically run the build script, you can run it manually in the command line or click on the build option inside of gradle tasks in the IDE

**How to run the program?**

Simply run the SpeechDemo.java file or the BookingDemo.java file through the IDE or command line. Tests through IDE aswell

**How to use the program?**

When you run the program, you will be prompted the following options:
**ENTER**: Start speaking into the microphone
**RELOAD**: Reloads current booking information stored from the server.
**EXIT**: Exits the program

When you press **enter** on your keyboard, the microphone will be set up, and you will have 10 seconds to say a prompt. After 10 seconds is passed, an AI generated response will be shown in the console, based off of what you asked and the current booked rooms that are currently in the server. After this, you will be prompted the 3 options shown above.

When you enter **exit** (not case sensitive) in the terminal, the program will close and the process will be terminated. simple as that.

When you enter **reload** (not case sensitive) in the terminal, the program will reload all current booking information stored in the booking server. This can be useful as somebody else could potentially book a new appointment while an AI generated response is happeneing at the same time, so it's important to use command from time to time.

As for secrets, due to time constraints, I was not able to hide the API Key in time, which is not good for security. However, I will assume that everyone won't take advantage of the API Key being exposed, and using to do things like spamming API Requests or to drain the amount of credits that I currently have.
