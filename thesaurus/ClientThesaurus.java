/*
 * Student Name: Houda Belefqih
 * Student ID : 1001511875
 * 5306-004 FALL 2017
 */

/* REFERENCES :
http://www.codingdevil.com/2014/02/simple-chat-application-using-java-socket-programming-2.html
http://tapas4web.blogspot.com/2011/04/client-server-gui-chating-application.html
http://www.iith.ac.in/~tbr/teaching/lab1.html
*/

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ClientThesaurus extends JFrame implements ActionListener{

    //DECLARATIONS OF COMPONENTS OF THE GUI
        JFrame frame;
        JPanel panel;
        JButton button;
        JTextArea text_area;
        JTextField text_field;
        

        
    //DECLARATIONS OF COMMUNICATION COMPONENTS   
        Socket socket;
        int serverPort = 12345;
        String filePath = "/";//initialization of filepath with default directory
        BufferedReader is = null; 
	DataOutputStream os = null;
	FileOutputStream fileOutputStream = null;

    
        public ClientThesaurus() throws UnknownHostException, IOException
           {    
                //Creating the frame + all its components
                frame = new JFrame("Client");
                panel =new JPanel(new GridBagLayout());
                text_field = new JTextField();
                text_area = new JTextArea();  
		button = new JButton("Send");
                
                //Properties of the parent container, frame
                frame.setSize(350, 350);
                frame.setLocationRelativeTo(null); //for the window to be placed in the center of the screen.
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// exit with the X button at top right corner
		panel.setLayout(null); 
                frame.add(panel);//Adding the panel to the frame parent    
                
                button.setBounds(120, 250, 95, 30);//button positionning
                panel.add(button);
                button.setEnabled(false);

                //Setting dimensions of text field and adding it to the frame
		text_field.setBounds(30, 20, 200, 30);
		panel.add(text_field);
                
                //Setting dimensions of text area and adding it to the frame
		text_area.setBounds(30, 70, 270, 150);
                text_area.setLineWrap(true);//wrap text around the rext area
		panel.add(text_area);
                
                button.addActionListener(this);//Action listener for the send button
                
                /*********** CONNECTION TO SERVER AND SENDING DATA********/

                try {
			
                        //the client process needs both the server's ip address and its listening port
                        //in order to initiate a communication
                        //since we are running the two processes on the same host
                        //we use the getLocalHost method for the local IP address

			//try to connect to the server
			socket = new Socket(InetAddress.getLocalHost(), serverPort);//serverPort : 12345	
                        button.setEnabled(true);

                     }
                
                catch (IllegalArgumentException e) 
                {   //display connection error in textarea
                    text_area.setText('\n'
				+ "Failed to connect to server: \n" + e);

                        try {
                          Thread.sleep(9000);
                          System.exit(0);
                            } 
                        catch (InterruptedException e1) 
                             {   text_area.setText('\n'
				+ "Failed to connect to server: \n" + e);}
                
                } 

    
           
           } //end of cllientserverthesaurus method
    
    public static void main(String[] args) throws IOException {
        // instanciate ClientThesaurus class
        new ClientThesaurus();
    }
    
 



    @Override
    public void actionPerformed(ActionEvent e) {
      
        if ((e.getSource() == button) && (!"".equals(text_field.getText())))	
        {
            try {
                //Creating reference to socket's outputStream
                os = new DataOutputStream(socket.getOutputStream());
                
                String word_to_lookup = text_field.getText();
                
                //Sending the wordtolookup
                 os.writeUTF(word_to_lookup);
                 //flushing the output stream when finished 
                 os.flush();
               
                 
                /*READ THE ANSWER FROM SERVER*/ 
                  DataInputStream dis = new DataInputStream(socket.getInputStream());
                    String string_synonyms  = dis.readUTF();
		  //displaying server's answer in textarea
                   text_area.setText('\n' + string_synonyms);
                  //Clearing text field for a new entry
                    text_field.setText("");
                   //this is just to separate between different entries
                  
                   
                  

                        
            } catch (IllegalArgumentException ex) 
                {   //display connection error in textarea
                    text_area.setText("\n"+ ex);
                
                
                try {
		  Thread.sleep(3000);
		  System.exit(0);
		    } 
                catch (InterruptedException e1) 
                     {}
                
                } 
		
                catch (IOException e2) 
                {text_area.setText("\n" + e2) ;}
    
    
        
    } 
     
    }
    
}//end of main class


