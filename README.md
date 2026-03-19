**How to setup this program?**

Download it, and in your IDE (Intelij or Eclipse), click on import gradle project, and select the entire folder for this project. The IDE will automatically run the build.gradle and set up the necessary libraries
If the IDE does not automatically run the build script, you can run it manually in the command line or click on the build option inside of gradle tasks in the IDE

**How to run the program?**

Simply run the ServerDemo.java file through the IDE or command line. To run the tests, run the SQLServerTest.java file which is located inside tests/SQLServerTest.java, through the IDE aswell with JUnit

**How to use the program?**

Before running the program, you want to do the following steps:

Go onto http://45.55.230.108:8000/admin/ 
Open inspect element, and go into the Network Tab
Enter ``hadam@student.bridgew.edu`` as the email and ``cs490`` as the password
When you log in, Click on the http://45.55.230.108:8000/admin/login/?next=/admin/ packet that has the POST request
Click on Payload, and copy the value and paste it into the RoomBookingRequests.java file. make sure to override the csrfmiddlewaretoken field variable with the one you copied.

The sessions will be good for a couple of hours. If you get errors when attempting to do operations in the GUI, simply repeat the steps above.

Now, go to the next packet (http://45.55.230.108:8000/admin/ with GET request), and look for the Cookie request header. copy it's value, and paste it into the cookieSession field that is located inside RoomBookingRequests.java.

Once that is done, you can run the GUI by running the ServerDemo.java file
There will be an Add Room, Remove Room, Change Capacity, and Refresh Button.

Add Room: will add a room with a name and capacity to the server. 
Remove Room: will remove a room based off of a Room ID. Will also save a report in assets/reports/report.txt telling you what bookings were deleted alongside the room that you deleted.
Change Capacity: will change a rooms maximum booking capacity based off of the room id you input
Refresh: Refreshes the current room list from the server

There is error handling built into the GUI. if you input invalid characters into the text box or if something on the server doesn't exist, the status message, displayed in the middle of the screen will let you know.
The rooms list is a scrollable box located in the center of the GUI. It shows all rooms present in the server in a JSON format. 
