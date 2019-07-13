/*
** Student Name: Houda Belefqih
** Student ID : 1001511875
** 5306-004 FALL 2017
** LAB2
*/

REFERENCES :
http://www.geeksforgeeks.org/remote-method-invocation-in-java/ https://www.mkyong.com/java/java-rmi-distributed-objects-example/
https://www.tutorialspoint.com/java_rmi/java_rmi_gui_application.htm
http://www.java67.com/2014/03/2-ways-to-remove-elementsobjects-from-ArrayList-java.html
________________________________________________________

_________________________________
Programming language : Java
IDE : NetBeans IDE 8.2 
Compiler : jdk1.8.0_60
_________________________________


INSTRUCTIONS TO COMPILE & RUN : 
(PLEASE RUN SERVER BEFORE CLIENTS)
 
TO compile using Windows Terminal :

1) CD to the directory (hxb1875) where the sources are : AdvisorProcess.java, MQSprocess.java, NotificationProcess.java, RMIclass.java, RMIinterface.java and StudentProcess.java

2) Set the path to the java compiler with the jdk version found in Programes files/Java/jdk... with the command :  

 set path=%path%;C:\Program Files\Java\<jdk version>\bin 

In my case, the command was : 

set path=%path%;C:\Program Files\Java\jdk1.8.0_60

3) Compile all the files at once with : javac *.java

4) START the rmi registry firt with command :start rmi registry

5) START MQSprocess (server) before other processes with command : java MQSprocess 

6) Start other processes in any order with commands (each one in its own terminal) : java AdvisorProcess ; java NotificationProcess and java StudentProcess

LIMITATIONS AND ASSUMPTIONS :

- OS : Windows 
- Both client and server are running on the SAME MACHINE because when naming the remote object for the registry, an URL with localhost was used, it is however to be replaced with the IP address where the remote object is present for different machines
- The port number is fixed : 1900 
- All messages are stored in one arraylist
- StudentProcess can choose from 4 courses only (hardcoded)
- StudentProcess can send multiple registration requests at the same time,however duplicates registration requests for the same course are not handled on the server instead each request is treated indenpendently.
-When a request is sent by the student the name field is not cleared to avoid typing in the name for each request but all checkboxes are unchecked.
- For StudentProcess, to be able to send a request, the name field is required as well as at least one course checked 
- NotificationProcess assumes only one student is registering for courses, as it displays the student's name + the advisor's decision for that particular student only 
- Advisor process has no APPROVE/DISAPPROVE button, when generetating a random decision for a course, the decision is sent automatically to the MQS
- For NotificationProcess and AdvisorProcess, if there are message to display, wait (7seconds and 3seconds respectively) before clearing fields and checking the MQS again
- Messages are persistent and are stored in a backupfile : backup.txt file, if the MQS is shut down, the messages are retrieved from the backup.txt file into the list of messages



