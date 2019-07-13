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
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;


//MAIN CLASS 
public class StudentProcess extends JFrame implements ActionListener {

    //DECLARATIONS OF COMPONENTS OF THE GUI
        JFrame frame;
        JPanel panel;
        JLabel label1;
        JLabel label2;
        JButton button;
        JTextField name_field;
        JCheckBox checkbox1;
        JCheckBox checkbox2;
        JCheckBox checkbox3;
        JCheckBox checkbox4;
        
        boolean clicked;//boolean to check wether or not the button have been clicked
        
        //A stub is a representation (proxy) of the remote object at client. 
        //It resides in the client system; it acts as a gateway for the client program.
        private RMIinterface stub;
        
       // StudentProcess method creates the GUI user interface with all its components
       // and Binds the remote object by the name lab2 so that the remote methods of the RMI class
       // can be invoked, for student process the only method invoked is going to be SubmitRequest
       // to send the MQS server the stduents requests for registartion
       // A student may register for multiple classes at the same time
       public StudentProcess() throws RemoteException, NotBoundException, MalformedURLException, InterruptedException 
          {   
                clicked = false;//initialize clicked to false
                
                // Binds the remote object by the name lab2
                //For the purpose of this lab clients and server program is executed on the same machine 
                //so localhost is used. 
                //In order to access the remote object from another machine,
                //localhost is to be replaced with the IP address where the remote object is present.
                stub =(RMIinterface)Naming.lookup("rmi://localhost:1900"+"/lab2");
                
                //Creating the frame + all its components
                frame = new JFrame("Student");
                panel =new JPanel(new GridBagLayout());
                name_field = new JTextField();//to hold student's name
     
		button = new JButton("Request registration");
                
                //Properties of the parent container, frame
                frame.setSize(400, 400);
                frame.setLocationRelativeTo(null); //for the window to be placed in the center of the screen.
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// exit with the X button at top right corner
		panel.setLayout(null); 
                frame.add(panel);//Adding the panel to the frame parent    
                
                button.setBounds(100, 300, 170, 30);//button positionning
                panel.add(button);
                button.setEnabled(true);

                //Label 1 creation + setting its dimensions 
                label1 = new JLabel("Student name:");
                label1.setBounds(30, 20, 100, 30);
                panel.add(label1);
               
                //Setting dimensions of text field and adding it to the frame
		name_field.setBounds(125, 20, 200, 30);
		panel.add(name_field);
                
                System.err.println("FFF");  
                
                //Creating checkboxes, setting their dimensions, setting initial value to unchecked and
                //adding them to the frame
                checkbox1 = new JCheckBox("CSE5306", true);//make the first course initially checked
                checkbox1.setBounds(30,80, 250,30);  
                
                checkbox2 = new JCheckBox("CSE5344", false);
                checkbox2.setBounds(30,120, 250,30); 
                
                checkbox3 = new JCheckBox("CSE5350", false);
                checkbox3.setBounds(30,160, 250,30);
                             
                checkbox4 = new JCheckBox("CSE5380", false);
                checkbox4.setBounds(30,200, 250,30);
                
        
                panel.add(checkbox1);
                panel.add(checkbox2);
                panel.add(checkbox3);
                panel.add(checkbox4);
            
            //Add Action listener for the send request button
            button.addActionListener(this);
            
            //This loop will keep the program running and waiting for the button to be clicked
            //clicked is initialized to "false" at first, when the button is clicked, the actionPeformed()
            //method will be called, when it is called the clicked parameter is first set to "true"
            //The request from student to advisor is sent to the MQS and at the end clicked is set to "false"
            while (!clicked) {
                    Thread.sleep(1000);}//wait for 1s
            
    
           } //end of Student process method 

    //main method used to instanciate the StudentProcess class
    public static void main(String[] args) throws Exception  { 
          new StudentProcess();
            
            
    }//end of main method

    @Override
    /* The actionPerformed method below is called when the submit button is clicked, it does the following:
    ** - Checks if name field is empty(i.e student didn't put his/her name)pops up a warning message in that case
    ** - Checks set value of all checkboxes(we assume that there are only 4 courses to choose from)
    ** - If a checkbox was "cheacked", a LeMessage object is created with arguments name of the students, 
    **   course,destination (i.e for the student process it is going to be "advisor") and decision (which is 
         going to be 0 for all student's requests (iedenied by default, until the advisor makes a decision)
         then the object LeMessage is sent to the MessageQueueingService Process via the stub by invoking the
         remote method SubmitRequest(LeMessage msg) 
    ** - At the end, the namefield is re-initialized to empty and all checkboxes are unchecked for a new request
    */
        public void actionPerformed(ActionEvent e) {
            clicked = true; 
            try {
                //Check if name field of student is not empty and that the "Send Request"
                // button is what triggered the event
                if ((e.getSource() == button) && (!"".equals(name_field.getText()))) { 
                    if (checkbox1.isSelected()) {
                    try
                    {
                        //create object LeMessage with arguments : name of the students, course and
                        //destination i.e for the student process it is going to be "advisor"
                        LeMessage msg = new LeMessage(name_field.getText(),"CSE5306", "advisor", 0);

                        // Calling the remote method SubmitRequest using the student's request as message
                        //This will add the message to the list on the MQS for courses to be checked 
                        //by the advisor
                        stub.SubmitRequest(msg);

                    }
                        catch (RemoteException ee) {} //catch remote exception
                    }
                    
                    if (checkbox2.isSelected()) {
                        try
                        {
                        //create object LeMessage with arguments : name of the students, course and
                        //destination i.e for the student process it is going to be "advisor"
                        LeMessage msg = new LeMessage(name_field.getText(),"CSE5344", "advisor", 0);

                        // Calling the remote method SubmitRequest using the student's request as message
                        //This will add the message to the list on the MQS for courses to be checked 
                        //by the advisor
                        stub.SubmitRequest(msg);
                        }

                        catch (RemoteException ee) {}
                    }
                    if (checkbox3.isSelected()) {
                        try
                        {
                        //create object LeMessage with arguments : name of the students, course and
                        //destination i.e for the student process it is going to be "advisor"
                        LeMessage msg = new LeMessage(name_field.getText(),"CSE5350", "advisor", 0 );

                        // Calling the remote method SubmitRequest using the student's request as message
                        //This will add the message to the list on the MQS for courses to be checked 
                        //by the advisor
                        stub.SubmitRequest(msg);

                        }

                        catch (RemoteException ee) {}
                    }
                    if (checkbox4.isSelected()) {
                        try
                        {  
                        //create object LeMessage with arguments : name of the students, course and
                        //destination i.e for the student process it is going to be "advisor"
                        LeMessage msg = new LeMessage(name_field.getText(),"CSE5380", "advisor", 0);

                        // Calling the remote method SubmitRequest using the student's request as message
                        //This will add the message to the list on the MQS for courses to be checked 
                        //by the advisor
                        stub.SubmitRequest(msg);
                        }

                        catch (RemoteException ee) {}
                    }
                    
                    //This in case the student didn't check any checkbox
                    if (!checkbox1.isSelected()
                        && !checkbox2.isSelected()
                        && !checkbox3.isSelected()
                        && !checkbox4.isSelected())
                    {
                        JOptionPane.showMessageDialog(null,
                            "PLEASE CHECK AT LEAST ONE CHECKBOX",
                            "CHECKBOX",
                            JOptionPane.WARNING_MESSAGE);
                    
                    }
                    
                    //After sending the request, we uncheck all checkboxes
                    //The name field stays for easiness
                    checkbox1.setSelected(false);
                    checkbox2.setSelected(false);
                    checkbox3.setSelected(false);
                    checkbox4.setSelected(false);
                    clicked = false;
                } else {
                    JOptionPane.showMessageDialog(null,
                            "PLEASE ENTER A STUDENT NAME",
                            "EMPTY FIELD",
                            JOptionPane.WARNING_MESSAGE);
                }
            }catch (HeadlessException ex)
            {
                try {
                    Thread.sleep(3000);
                    System.exit(0);
                }
                catch (InterruptedException e1)
                {}


            }
        } //end of action performed method

    }//end of main class



    

