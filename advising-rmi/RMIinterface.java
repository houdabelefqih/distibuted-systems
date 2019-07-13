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


import java.util.List;
import java.rmi.Remote;
import java.rmi.RemoteException;


// This interface's methods can be invoked from another JVM 
// Any object that implements this interface can be a remote object.
public interface RMIinterface extends Remote {
    
    
    /* The method SubmitRequest is the method that will take care of the student's requests to register 
     ** for courses AND also the advisor's decision to approve or disapprove, the same list will be used for both
     ** Each time this method is remotely invoked, it adds to received msg to the list of course and at the same
     ** time make a backup of that list by writing the msgs to a .txt file
     */
     void SubmitRequest(LeMessage msg) throws RemoteException;
     
     
     //This method simply returns the current list where courses are stored
     List<LeMessage> checkList() throws RemoteException;
     
     //This method removes a msg according to a condition passed
     void DeleteMsg(String condition) throws RemoteException;
     
     //This method updates the backup file each time it is invoked after a change to the list of messages
     //void updateFile();
}


