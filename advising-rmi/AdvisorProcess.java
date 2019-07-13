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

import java.net.*;
import java.awt.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.*;


public class AdvisorProcess {
    
    //DECLARATIONS OF COMPONENTS OF THE GUI
        JFrame frame;
        JPanel panel;
        JLabel label1, label2, label3;
        JTextArea msgs_retrieved, approved, disapproved;
        
        //A stub is a representation (proxy) of the remote object at client. 
        //It resides in the client system; it acts as a gateway for the client program.
        private RMIinterface stub;
        
/* AdvisorProcess method creates the GUI user interface with all its components
** and Binds the remote object by the name lab2 so that the remote methods of the RMI class
** can be invoked, for advisor process the methods invoked are going to be checkList, SubmitRequest and DeleteMsg
** to check the list's objects (ie courses) on the Message Queueing Server and identify the ones with 
** the parameter "destination" set to "advisor". 
** Then generates randomly a decision for each request and Submit it with SubmitRequest method
** For each message retrieved, delete it from the list of messages with DeleteMsg method
** The list is checked every 3seconds. In case there's no messages for the advisor from the student
** it displays a "No messages"
*/     
 public AdvisorProcess() throws RemoteException, NotBoundException, MalformedURLException, InterruptedException
        
        {
            // Binds the remote object by the name lab2
            //For the purpose of this lab clients and server program is executed on the same machine 
            //so localhost is used. 
            //In order to access the remote object from another machine,
            //localhost is to be replaced with the IP address where the remote object is present.
            stub =(RMIinterface)Naming.lookup("rmi://localhost:1900"+"/lab2");
        
            //Creating the frame + all its components and initialize text areas and fields to empty
                frame = new JFrame("Advisor");
                panel =new JPanel(new GridBagLayout());
                msgs_retrieved = new JTextArea("");
                approved = new JTextArea("");
                disapproved = new JTextArea("");
                
                //Properties of the parent container, frame
                frame.setSize(600, 600);
                frame.setLocationRelativeTo(null); //for the window to be placed in the center of the screen.
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// exit with the X button at top right corner
		panel.setLayout(null); 
                frame.add(panel);//Adding the panel to the frame parent    

                //Labels dimensions and adding them to the frame
                label1 = new JLabel("Requests from student:");
                label1.setBounds(30, 20, 200, 30);
                
                label2 = new JLabel("Approved:");
                label2.setBounds(30, 270, 200, 30);
                
                label3 = new JLabel("Disapproved:");
                label3.setBounds(300, 270, 200, 30);
                
                panel.add(label1);
                panel.add(label2);
                panel.add(label3);
               
                //Setting dimensions of text areas and adding them to the frame
		msgs_retrieved.setBounds(30, 50, 520, 200);
		panel.add(msgs_retrieved);
                approved.setBounds(30, 300, 250, 200);
		panel.add(approved);
                disapproved.setBounds(300, 300, 250, 200);
		panel.add(disapproved);
                
         
            /* The while loop is for continuous checking of the messages stored in the list at
            ** the Message Queueing Server as long as the process is running.
            ** Inside the loop, the checkList() method is invoked on the server, the advisor process gets
            ** the list in return. It then proceeds to check messages, approves or disapproves randomly 
            ** by setting the "decision" field of the message object to either 0 for denied or 1 for approved
            ** and displays the decision for each course.
            ** If the list is emty or if none of the messages were addressed to the advisor
            ** the process sleeps and waits for 3seconds and then proceeds to another check
            */  
                
            while(true)
            { 
                //Create our object of type list to hold the returned list from the MessageQueueinServer
                java.util.List<LeMessage> mylist;
                //Invoke the remote method checkList() to get the current list of messages on the server
                mylist = stub.checkList();
                
            
                /* Check all messages on the list on the Message Queueing server
                ** For each message check the "destination" field, if it is set to "advisor" 
                ** then increment "foradvisor" value by 1 and display the course in the appropriate 
                ** text area, either APPROVED OR DIAPPROVED
                */
                    int foradvisor =0;//initialize counter of messages to advisor
                    
                    //Check first of all if the list is empty, ie no message at all
                    // A "NO MESSAGES" is displayed in the all messages text area in that case
                    if (mylist.isEmpty())
                    { //the process will wait for 7seconds and then proceed to another check of messages
                      msgs_retrieved.setText("NO MESSAGES");
                      Thread.sleep(3000);
                      
                      //CLEAR ALL FIELDS FOR NEXT CHECK
                      msgs_retrieved.setText("");
                      approved.setText(""); 
                      disapproved.setText("");
                      continue; 
                    }//end of sleeping in case list is empty
            
  //if the list is not empty
      else  {
              
            /*
            ** FIRST DISPLAY ALL MESSAGES ADDRESSED TO THE ADVISOR
            */
            for (LeMessage msg : mylist)//for every message on the list
            {
                //for every message that is destined to the advisor retrieve and display
                if (msg.getDestination().equals("advisor"))
                {
                msgs_retrieved.setText(msgs_retrieved.getText() + "\n" + "Student:"+msg.getStudentname() + 
                                       " requests decision for: " + msg.getCourse() + "\n");
                }
            } //for each message          
                        
            for (LeMessage msg : mylist) //for each message on the list
            {
               //if the message in the list is destined to the advisor 
               if (msg.getDestination().equals("advisor"))
               {
                   //increment the counter of messages destined to advisor by 1
                   foradvisor++;
                   
                 /***************The decision is made randomly for each course****************/
                    //The line below generates a random number in the range [0, 1[ 
                    //if the random number is < than 0.5 we assign to it the value 0
                    //if the random bumber is > or = 0.5 we assign it the value 1
                    //Decision generated either 0 or 1, respectively Denied or Approved
                    int random_decision=(Math.random()<0.5)?0:1;

                    
                    //create object LeMessage with arguments : name of the students, course, 
                    //destination (i.e for the advisor process it is going to be "student"
                    //AND the random decision generated either 0 or 1, Denied or Approved
                    LeMessage answer = new LeMessage(   msg.getStudentname(), 
                                                        msg.getCourse(), 
                                                        "student",
                                                        random_decision);
                    
                    // Calling the remote method SubmitRequest using the advisor's answer as message
                    //This will add the message to the list on the MQS for courses to be checked 
                    //by the notification process
                     stub.SubmitRequest(answer);
                   
                   /* The Decision from the advisor is either 0:Disapproved or 1:Approved
                    ** if decision for a course is 0 ie denied request, print course in the disapproved textarea
                    ** if it was approved then print course in the approved textarea
                    */
                   switch (answer.getDecision()) {
                       case 0:
                           disapproved.setText(disapproved.getText() + answer.getCourse()+ "\n");
                           break;
                       case 1:
                           approved.setText(approved.getText() + answer.getCourse()+ "\n");
                           break;
                       default:
                           break;
                   }//end of switch statement

               }//end of if condition on destination (to advisor)
            }//end of the for loop that goes through all contents of the messages list
            

            /********************* REMOVE MESSAGE AFTER DISPLAY *****************************/
            /*invoke method to remove a msg destined to the advisor from the list on the MQS*/
            /* GO THROUGH THE LIST AGAIN AND REMOVE ALL OBJECTS DESTINED TO ADVISOR*/

              stub.DeleteMsg("advisor");

           /* if all messages have been checked but there were none from the student to advisor
            ** ie foradvisor variable is equal to 0, then the process sleeps for 3s before checking again
            ** A "NO MESSAGES" is displayed in all messages text area in that case
            */
            if (foradvisor==0)
            { //the process will wait for 3seconds and then proceed to another check of messages
              msgs_retrieved.setText("NO MESSAGES");  
              Thread.sleep(3000);
                msgs_retrieved.setText("");
                approved.setText(""); disapproved.setText("");
              continue; }//end of sleeping in case there are no msgs for the advisor


        }//end of else (list is not empty)
                    
        /* BEFORE PROCEEDING TO THE NEXT CHECK, WAIT 3S AND CLEAR ALL TEXT AREAS*/  
        Thread.sleep(3000);
        msgs_retrieved.setText("");
        approved.setText(""); disapproved.setText("");
                    
            }//end of infinite while loop for continuous checking
        
        }//end of AdvisorProcess method
        
        
    //main method used to instanciate AdvisortProcess class
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException, InterruptedException  {
            AdvisorProcess advisorProcess = new AdvisorProcess();
    }

    
}
