/*
** Student Name: Houda Belefqih
** Student ID : 1001511875
** 5306-004 FALL 2017
** LAB2
*/

/* REFERENCES :
** http://www.geeksforgeeks.org/remote-method-invocation-in-java/
** https://www.mkyong.com/java/java-rmi-distributed-objects-example/
** https://www.tutorialspoint.com/java_rmi/java_rmi_gui_application.htm
** http://www.java67.com/2014/03/2-ways-to-remove-elementsobjects-from-ArrayList-java.html
*/



import java.io.BufferedReader;
import java.util.List;
import java.nio.file.Path;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.Serializable;
import java.util.ListIterator;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.nio.charset.Charset;


import java.rmi.server.UnicastRemoteObject;

/*The class extends to UnicastRemoteObject class of java.rmi package. 
**Also, a default constructor needs to be created to throw the java.rmi.RemoteException from its parent 
constructor in class.*/
public class RMIclass extends UnicastRemoteObject implements RMIinterface {

     //Create the list that will hold all the LeMessage objects from student and advisor
     List<LeMessage> myList= new ArrayList<LeMessage>();


    //This is the constructor for the remote object, it invokes the superclass constructor
     //It also handles the errors thrown by the Unicast remote object
     public RMIclass () throws RemoteException, IOException
        {
            //dir and fileName are used to locate the backup.txt file in the current directory
            String dir = System.getProperty("user.dir") + "\\"; 
            String fileName = dir + "backup.txt";
            
            //FileReader class is a general tool to read in characters from a File. 
            //The BufferedReader class wraps around Readers FileReader to buffer the input and improve efficiency
            FileReader fr = null;
            BufferedReader reader = null;
                 
            //Test if the backup file exists
            boolean fileExists = true;
                 try
                     {fr = new FileReader(fileName);}    
                 catch (FileNotFoundException e)
                     {fileExists = false;}
                    
        //if file exists, open it and load each parameter into a LeMessage object and add the message
        //to the list
            if (fileExists)
                     {
             try{
                 //Wrap filereader in a buffreader
                 reader = new BufferedReader(fr);

                 String line; //this will hold one line at a time frome the txt file

                 //while we don't hit the empty line
                 while((line = reader.readLine()) != null)
                 {  

                 /*to get the parameters in each line and store them in the list of messages*/
                 //student name, course, destination, decision of advisor
                 String arr[] = line.split(" ", 4);//split the line into 4 strings
                 LeMessage newMessage = new LeMessage(arr[0], arr[1], arr[2], 
                                                    Integer.parseInt(arr[3]));
                 myList.add(newMessage);//add to the list of messages

                 }//end of while loop

             }//end of try
                    
                     catch (FileNotFoundException e) {}
                     catch (IOException e) {} 
                    finally {
                         if (reader != null) {
                             reader.close();//close our stream
                         }
                     }
                     
                 }
           
        }//end of RMI constructor
         
  
     /* The method SubmitRequest is the method that will take care of the student's requests to register 
     ** for courses AND also the advisor's decision to approve or disapprove, the same list will be used for both
     ** Each time this method is remotely invoked, it adds to received msg to the list of course and at the same
     ** time make a backup of that list by writing the msgs to a .txt file
     */
        public void SubmitRequest(LeMessage msg) throws RemoteException
        {
            myList.add(msg);//add the msg to the list
            updateFile();//update back file by writing in it the new list contents
            
        }
       
        //This method simply returns the current list where courses are stored
        public List<LeMessage> checkList() throws RemoteException
        { 
            return myList;
        }//end checkList
      
     //This method removes a msg from the list when it is retrieved 
     //according to a condition passed
     public void DeleteMsg(String condition) throws RemoteException
     { 
         for (ListIterator<LeMessage> iter = myList.listIterator(); iter.hasNext();) {
                LeMessage msgObject = iter.next();
                if (msgObject.getDestination().equals(condition)) {
                    iter.remove();
                }
            }//end for loop
         
      //Update back up file when deleting messages
      updateFile();
      
     }//end DeletMsg
     
     //This method updates the backup file each time it is invoked after a change to the list of messages
     public void updateFile()
     {
         String dir = System.getProperty("user.dir") + "\\"; 
         String filePath = dir + "backup.txt";

        Path fileP = Paths.get(filePath);
        Charset charset = Charset.forName("utf-8");

        try (BufferedWriter writer = Files.newBufferedWriter(fileP, charset)) {
            //For each message object in the message list
            for (LeMessage msg : myList) {
                
                //Construct the string to write in the file with the objects values of
                //student name, course, destination and decision, each two separated by a space
                String line = msg.getStudentname() + " " +  msg.getCourse()+ " "
                           +  msg.getDestination() + " " +  msg.getDecision();
                writer.write(line, 0, line.length());
                writer.newLine();//add a new line after each message
            }
            
            writer.close();

        } catch (IOException e) {}
    }//end of updateFile 

}

//This is the class that defines our Message objects
class LeMessage implements Serializable {
    String studentname;
    String course; 
    String destination;
    int decision;//This is for the Advisor to set, 0 :student's request denied; and 1: Approved
    
    //Class constructor of object type LeMessage
    public LeMessage(String studentname,String course, String destination, int decision){
        this.studentname=studentname;
        this.course=course;
        this.destination=destination;
        this.decision=decision;
 
    }
    
    //Method to get destination of msg either student or advisor
    //if destination = advisor, the advisor process will use it to retrieve requests from student
    //if destination = student, the notification process will use it to retrive approval/disapproval of the advisor
    public String getDestination() {return destination;}
    public String getCourse() {return course;}//method to get the requested course
    public String getStudentname() {return studentname;}//method to get studentname
    public int getDecision(){return decision;}//method to check for advisor's decision

    
}
