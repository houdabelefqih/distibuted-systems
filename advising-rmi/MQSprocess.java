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



import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException; 

//Main class
public class MQSprocess extends RMIclass {
    
    //constructor of the class
    public MQSprocess() throws RemoteException, IOException {}
    
    //The main method here defines all components needed for rmi services and MQS to be able to host those services
    //A registry is created to store the remote object
    public static void main(String[] args) throws Exception  
    {
        try{
          //First thing to do when the MQS starts is to create a backupfile
          // in the current directory
          String dir = System.getProperty("user.dir") + "\\"; 
          String filePath = dir + "backup.txt";           
          File file = new File("filePath");
     
        //Instanciation of the implementation class   
        RMIinterface obj = new RMIclass();
        
        // rmiregistry within the server JVM with
       // port number 1900
       LocateRegistry.createRegistry(1900);

       // Binds the remote object by the name lab2
       //For the purpose of this lab clients and server program is executed on the same machine 
       //so localhost is used. 
       //In order to access the remote object from another machine,
       //localhost is to be replaced with the IP address where the remote object is present.
       Naming.rebind("rmi://localhost:1900"+
                     "/lab2",obj);
        
       System.err.println("Server ready");
      
        
        }
        
        catch (RemoteException e){}
    }
    
}
