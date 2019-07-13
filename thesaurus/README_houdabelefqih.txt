/*
 * Student Name: Houda Belefqih
 * Student ID : 1001511875
 * 5306-004 FALL 2017
 */

_______________________________________________________
 REFERENCES :
http://www.codingdevil.com/2014/02/simple-chat-application-using-java-socket-programming-2.html
http://tapas4web.blogspot.com/2011/04/client-server-gui-chating-application.html
http://www.iith.ac.in/~tbr/teaching/lab1.html
*/
________________________________________________________

_________________________________
Programming language : Java
IDE : NetBeans IDE 8.2 
Compiler : jdk1.8.0_60
_________________________________


INSTRUCTIONS TO COMPILE : 
(PLEASE RUN SERVER BEFORE CLIENT)


SERVER : compile and run 

Using Windows Terminal :

1) CD to the directory where the source file (ServerThesaurus.java) is

2) Set the path to the java compiler with the jdk version found in Programes files/Java/jdk... with the command :  

 set path=%path%;C:\Program Files\Java\<jdk version>\bin 

In my case, the command was : 

set path=%path%;C:\Program Files\Java\jdk1.8.0_60

3) Compile the source file : javac WebServer.java

4) Run with : java WebServer 


********************************************************************

CLIENT: Compile and run (ON ANOTHER TERMINAL)

Using another Windows terminal :

1) CD to the directory where the source file (ClientThesaurus.java) is

2) If the path to the compiler has'nt been set yet, do step 2 in compiling Server above

3) Compile the source file : javac ClientThesaurus.java

4) Run with : java ClientThesaurus

********************************************************************


LIMITATIONS AND ASSUMPTIONS :


- OS : Windows 
- Both client and server are running on the SAME MACHINE 
- The port number is fixed : 12345
- The program does not handle words entered that do not exist in the file thesaurus.txt, please lookup only the words in it 




