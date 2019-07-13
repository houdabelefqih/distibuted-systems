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
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class ServerThesaurus  extends JFrame {

        //DECLARATIONS OF COMPONENTS OF THE GUI
        JFrame frame;
        JPanel panel;
        JTextArea text_area;

        //DECLARATIONS OF COMMUNICATION COMPONENTS   
        int portNumber = 12345;//Setting the port number from which the server will receive requests 
        static ServerSocket server;
	static Socket conn;

        public ServerThesaurus() throws UnknownHostException, IOException {


            //Creating the frame + all its components
                frame = new JFrame("Server");
                panel =new JPanel(new GridBagLayout());            
                text_area = new JTextArea(); 

                //Properties of the parent container, frame
                frame.setSize(350, 350);
                frame.setLocationRelativeTo(null); //for the window to be placed in the center of the screen.
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// exit with the X button at top right corner
		panel.setLayout(null); 
                frame.add(panel);//Adding the panel to the frame parent    

                
                //Setting dimensions of text area and adding it to the frame
		text_area.setBounds(30, 70, 270, 150);
                text_area.setLineWrap(true);//wrap text around the rext area
		panel.add(text_area);
                       
                
                 server = new ServerSocket(12345, 1, InetAddress.getLocalHost());
                  conn = server.accept();
                
           /*To keep the server always listening, we handle requests in an infinite loop*/
                 while (true) {
                     
                     // Get a reference to the socket's input
                     DataInputStream dis = new DataInputStream(conn.getInputStream());
                     String word_to_lookup  = dis.readUTF(); //read requested word
                     
                     /***********************************************************************/
                     /* CODE FOR LOOKING UP THE WORD IN THE THE THESAURUS FILE */
                     /* AND SENDING THE APPROPRIATE ANSWER */
                     /***********************************************************************/
                          String dir = System.getProperty("user.dir") + "\\"; 
                          String fileName = dir + "thesaurus.txt";
                          
                          //display the word to lookup
                           text_area.setText(text_area.getText() + "\n" + word_to_lookup);  

                          // Open the requested file and throw exception if the file is not found
                          FileReader fr = null;
                          BufferedReader reader = null;
                          
                          boolean fileExists = true;
                          try
                              {
                              fr = new FileReader(fileName);
                              }    
                          catch (FileNotFoundException e)
                              {
                              fileExists = false;
                              }
                     
                  

                   if (fileExists)
                           {
                     try{
                            //Wrap filereader in a buffreader
                            reader = new BufferedReader(fr);

                            String line; //this will hold one line at a time
                        
                         //while we don't hit the empty line
                         while((line = reader.readLine()) != null)
                         {   
                         /*to get the first word of the line in thesaurus file*/
                         String arr[] = line.split(" ", 2);
                         String first_word = arr[0];
                        
                         
                         /*Compare the word requested by client to every first word of thesaurus file
                         until a match is found
                         */
                         if (word_to_lookup.equals(first_word))//if there's a match
                         {
                             // Get a reference to the socket's input
                             DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                             
                             // Send back the whole line to the client
                             dos.writeUTF(line);
                             
                             break;
                            
                         }

                         }//end of while loop
          
                         
                     }//end of try
                    
                     
                     catch (FileNotFoundException e) {
                         e.printStackTrace();}
                         
                

                 
                     catch (IOException e) {
                         e.printStackTrace();
                     } finally {
                         if (reader != null) {
                             reader.close();
                         }
                     }
                    
                           }//end if fileexist
                      
                     
                   
                   else {
                       text_area.setText('\n'+ "FILE NOT FOUND" + "\n" + fileName );
                   }
                 
        
                 }//end while true

        }//end method
      
    public static void main(String[] args) {
            try {
                // instanciate ServerThesaurus class
                new ServerThesaurus();
            } catch (IOException ex) {
                
            }
    }
    
}





