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


import java.awt.*;
import javax.swing.*;
import java.util.List;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;

//MAIN CLASS
public class NotificationProcess {
    
    
       //DECLARATIONS OF COMPONENTS OF THE GUI
        JFrame frame;
        JPanel panel;
        JButton button;
        JTextField studentname;
        JLabel label1, label2, label3;
        JTextArea approved, disapproved;
        
        //A stub is a representation (proxy) of the remote object at client. 
        //It resides in the client system; it acts as a gateway for the client program.
        private RMIinterface stub;

        /* NotificationProcess method creates the GUI user interface with all its components
        ** and Binds the remote object by the name lab2 so that the remote methods of the RMI class
        ** can be invoked, for notification process the methods invoked is going to be checkList and DeleteMsg
        ** to check the list's objects (ie courses) on the Message Queueing Server and identify the ones with 
        ** the parameter "destination" set to "student". Then read the "decison" parameter and display for
        ** a particular students the approved and denied courses.
        ** The retrieved messages are physically deleted from the list with the DeleteMsg method
        ** The list is checked every 7seconds. In case there's no messages for the students from the advisor
        ** it displays a "No messages"
        */
        public NotificationProcess() throws RemoteException, NotBoundException, 
                                            MalformedURLException, InterruptedException
        
        {
             // Binds the remote object by the name lab2
            //For the purpose of this lab clients and server program is executed on the same machine 
            //so localhost is used. 
            //In order to access the remote object from another machine,
            //localhost is to be replaced with the IP address where the remote object is present.
            stub =(RMIinterface)Naming.lookup("rmi://localhost:1900"+"/lab2");
        
              //Creating the frame + all its components and initialize text areas and fields to empty
                frame = new JFrame("Notifications");
                panel =new JPanel(new GridBagLayout());
                studentname = new JTextField("");
                approved = new JTextArea("");
                disapproved = new JTextArea("");

                //Properties of the parent container, frame
                frame.setSize(400, 400);
                frame.setLocationRelativeTo(null); //for the window to be placed in the center of the screen.
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// exit with the X button at top right corner
		panel.setLayout(null); 
                frame.add(panel);//Adding the panel to the frame parent    


                //Labels dimensions and adding them to the frame
                label1 = new JLabel("Student name:");
                label1.setBounds(30, 20, 100, 30);
                
                label2 = new JLabel("Approved:");
                label2.setBounds(30, 100, 200, 30);
                
                label3 = new JLabel("Disapproved:");
                label3.setBounds(200, 100, 200, 30);
                
                panel.add(label1);
                panel.add(label2);
                panel.add(label3);
               
                //Setting dimensions of text field/text areas and adding them to the frame
		studentname.setBounds(125, 20, 200, 30);
		panel.add(studentname);
                approved.setBounds(30, 130, 150, 150);
		panel.add(approved);
                disapproved.setBounds(200, 130, 150, 150);
		panel.add(disapproved);
                
            /* The while loop is for continuous checking of the messages stored in the list at
            ** the Message Queueing Server as long as the process is running.
            ** Inside the loop, the checkList() method is invoked on the server, the notification process gets
            ** the list in return. It then proceeds to check messages, and displays the approved and denied courses
            ** for the student. If the list is emty or if none of the messages were addressed to the student
            ** the process sleeps and waits for 7seconds and then proceeds to another check
            */     
            while(true){   
                
                //Create our object of type list to hold the returned list from the MessageQueueinServer
                List<LeMessage> mylist;
                //Invoke the remote method checkList() to get the current list of messages on the server
                mylist = stub.checkList();
                
                /* Check all messages on the list on the Message Queueing server
                ** For each message check the "destination" field, if it is set to "student" 
                ** then increment "forstudent" value by 1 and display the course in the appropriate 
                ** text area, either APPROVED OR DIAPPROVED
                */
                    int forstudent =0;//initialize counter of messages to student
                    
                    //Check first of all if the list is empty, ie no message at all
                    // A "NO MESSAGES" is displayed in the studentname text field in that case
                    if (mylist.isEmpty())
                    { //the process will wait for 7seconds and then proceed to another check of messages
                      studentname.setText("NO MESSAGES");
                      Thread.sleep(7000);
                      
                      //CLEAR ALL FIELDS FOR NEXT CHECK
                      studentname.setText("");
                      approved.setText(""); 
                      disapproved.setText("");
                      continue; 
                    }//end of sleeping in case list is empty
                    
                   //if the list is not empty
                    else  {
                        
                    for (LeMessage msg : mylist) //for each message
                    {
                       //if the message in the list is destined to the student 
                       if (msg.getDestination().equals("student"))
                       {
                           //increment the counter of messages destined to student by 1
                           forstudent++;
                           //This if statement below is to display the name of the student only once
                           //when we find the first message detistined to the student
                           if(forstudent ==1){studentname.setText(msg.getStudentname());}
                           
                           /* The Decision from the advisor is either 0:Disapproved or 1:Approved
                            ** if decision for a course is 0 ie denied request, print course in the disapproved textarea
                            ** if it was approved then print course in the approved textarea
                            */
                           switch (msg.getDecision()) {
                               case 0:
                                   disapproved.setText(disapproved.getText() + msg.getCourse()+ "\n");
                                   break;
                               case 1:
                                   approved.setText(approved.getText() + msg.getCourse()+ "\n");
                                   break;
                               default:
                                   break;
                           }//end of switch statement
                          
                       }//end of if condition on destination (to student or to advisor)
                    }//end of the for loop that goes through all contents of the messages list
                    
                /**************** REMOVE MESSAGE AFTER DISPLAY *****************************/
                /*invoke method to remove a msg destined to the student from the list on the MQS*/
                /* GO THROUGH THE LIST AGAIN AND REMOVE ALL OBJECTS DESTINED TO STUDENT*/
                   stub.DeleteMsg("student");
                           
                  
                   /* if all messages have been checked but there were none from the advisor to student 
                    ** ie forstudent variable is equal to 0, then the process sleeps for 7s before checking again
                    ** A "NO MESSAGES" is displayed in the studentname text field in that case
                    */
                    if (forstudent==0)
                    { //the process will wait for 7seconds and then proceed to another check of messages
                      studentname.setText("NO MESSAGES");  
                      Thread.sleep(7000);
                      continue; }//end of sleeping in case there are no msgs for the student
                         
                 
                }//end of else (list is not empty)
             
                /* BEFORE PROCEEDING TO THE NEXT CHECK, WAIT 7S AND CLEAR ALL TEXTFIELDS AND AREAS*/    
                Thread.sleep(7000);
                studentname.setText("");//clear textfield for next check
                approved.setText(""); disapproved.setText("");//clear textareas for next check
                    
            }//end of while(true) for continuous checking of messages
        }
        
        
    //main method used to instanciate NotificationProcess class
    public static void main(String[] args) throws Exception  {
            NotificationProcess notifProcess = new NotificationProcess();
    }
    
}
