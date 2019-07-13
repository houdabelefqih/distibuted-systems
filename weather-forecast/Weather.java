/*
** Student Name: Houda Belefqih
** Student ID : 1001511875
** 5306-004 FALL 2017
** LAB3
*/

/* REFERENCES :
** - http://www.java2s.com/Code/Java/Network-Protocol/Grabbingapageusingsocket.htm
** - https://stackoverflow.com/questions/12575990/calendar-date-to-yyyy-mm-dd-format-in-java
*/

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import java.util.Calendar;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.ParseException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.net.HttpURLConnection;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

//Main class
public class Weather extends JFrame implements ActionListener {
    
    //DECLARATIONS OF COMPONENTS OF THE GUI
        JFrame frame;
        JPanel panel;
        JButton button;//refresh button
        //Labels
        JLabel label;
        JLabel latitude;
        JLabel WindSpeed;
        JLabel longitude;
        JLabel Temperature;
        JLabel WindDirection;
        JLabel DetailedForecast;
        
        //Text_fields that will hold values parsed from received answer from NWS website
        JTextField _WindSpeed;
        JTextField _Temperature;
        JTextField _WindDirection;
        JTextArea _DetailedForecast;
        
        boolean clicked;//boolean to check wether or not the refresh key have been clicked

       /* The constructor of the main class Weather() :
       **   1- First calls the method create_GUI() to create the user interface 
       **      with all the components that will hold the values of Temperature, Wind Speed, Wind Direction
       **      ,Detailed forecast and Icon, all received from API's answer
       **   2- Calls the connect_NWS() method that connects to the National Weather service API using the specific
       **      URL which includes the latitude and longitude of Las Palmas, Texas. The API's answer is in a JSON format
       **      The method then proceeds to retrieving and parsing the needed values by comparing first the local date and time
       **      and extracting the values of for that particular date/time.The HTTP connnection closes after all data was received
        **     and displayed to user.
        **  3- The process keeps running using a infinite while loop and waits for the eventual click of the refresh button.
        **     If the refresh button is clicked, the connect_NWS() is called and new forecast is retrieved for the same
        **     longitude/latitude
       */
       public Weather() throws InterruptedException, ParseException
          { 
            //Create user interface
            create_GUI();
            //Connect to the National Weather Service API
            connect_NWS();

            /* This while loop will keep the program running and waiting for the refresh button to be clicked
            ** clicked is initialized to "false" at first, when the key is clicked, the actionPeformed()
            ** method will be called, when it is called the clicked parameter is first set to "true" and then the 
            ** connect_NWS() is called for a refresh of the forecast
            */
            while (!clicked) {
            Thread.sleep(500);
            }//wait for 1/2second 

            
           } //end of Weather process constructor 

       /* Method that creates all the user interface components, called in main class constructor 
       ** to create the user interface 
       ** with all the components that will hold the values of Temperature, Wind Speed, Wind Direction,
       ** Detailed forecast and Icon, all received from API's answer + a refresh button 
       */
       private void create_GUI(){
       
                //Creating the frame + all its components
                frame = new JFrame("Client- Weather Forecast");
                panel =new JPanel(new GridBagLayout());
                
                //Properties of the parent container, frame
                frame.setSize(550, 550);
                frame.setLocationRelativeTo(null); //for the window to be placed in the center of the screen.
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// exit with the X button at top right corner
		panel.setLayout(null); 
                frame.add(panel);//Adding the panel to the frame parent  

                /** Creating labels and textfields setting their dimension andadding them to the frame **/
                label = new JLabel("Forecast for Las Palmas,TX:");
                label.setBounds(30, 20, 200, 30);
                panel.add(label);
             
                latitude = new JLabel("Latitude: 26.9522°N");
                latitude.setBounds(30, 60, 200, 30);
                panel.add(latitude);
 
                longitude = new JLabel("Longitude: 99.2747°W");
                longitude.setBounds(170, 60, 200, 30);
                panel.add(longitude);

                Temperature = new JLabel("Temperature:");
                Temperature.setBounds(30,100, 200,30); 
                _Temperature = new JTextField();
                _Temperature.setBounds(200, 100, 250, 30);
               
                WindSpeed = new JLabel("Wind Speed:");
                WindSpeed.setBounds(30,140, 200,30); 
                _WindSpeed = new JTextField();
                _WindSpeed.setBounds(200, 140, 250, 30);
                
                WindDirection = new JLabel("Wind Direction:");
                WindDirection.setBounds(30,180, 200,30); 
                _WindDirection = new JTextField();
                _WindDirection.setBounds(200, 180, 250, 30);
                
                DetailedForecast = new JLabel("Detailed Forecast :");
                DetailedForecast.setBounds(30,220, 200,30); 
                _DetailedForecast = new JTextArea();
                _DetailedForecast.setLineWrap(true);
                _DetailedForecast.setBounds(200, 220, 250, 100);
                
                button = new JButton("Refresh");
                button.setBounds(10, 460, 100, 30);
                panel.add(button);
                button.setEnabled(true);
                button.addActionListener(this);//add action listener to the refresh button
                
                //Add components to the panel in the main frame
                panel.add(Temperature);
                panel.add(_Temperature);
                panel.add(WindDirection);
                panel.add(_WindDirection);
                panel.add(WindSpeed);
                panel.add(_WindSpeed);
                panel.add(DetailedForecast);
                panel.add(_DetailedForecast);

       }

       /*  Method that connects to the National Weather service API using the specific
       **  URL which includes the latitude and longitude of Las Palmas, Texas. The API's answer is in a JSON format
       **  The method then proceeds to retrieving and parsing the needed values by comparing first the local date and time
       **  and extracting the values of for that particular date/time.The HTTP connnection closes after all data was received
       **  and displayed to user.
       */
       private void connect_NWS() throws ParseException{
        //Print message whenever method is called
        System.out.println("START FORECAST");  
        
        //Initialise text field to empty sring
        _Temperature.setText("");
        _WindDirection.setText("");
        _WindSpeed.setText("");
        _DetailedForecast.setText("");
        
    try
        {   
        //Construct URL with latitude and longitude hardcoded at the end
        //and set up a connection to the API using the constructed URL
        String myUrl = "https://api.weather.gov/points/26.9522,-99.2747/forecast";
        
        /* Connection to API via HTTP */
        URL url = new URL(myUrl);
        URLConnection urlConnection = url.openConnection();
        HttpURLConnection connection = null;
        connection = (HttpURLConnection) urlConnection;
        /* Connection to API via HTTP */

        /* This block uses the calendar instance based on the current time in 
        ** the default time zone with the default locale then formats the returned date value to 
        ** yyyy-MM-dd format. It also gets the hour from the calendar returned value. It formats it to a
        ** two digits number.
        ** The final value of the current date is constructed to match the API's date format which comes in : 
        ** YYYY-MM-DDTHH:MM:SS. And because the service is updated hourly only, we only need to use the Hour portion
        */
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        String currentHour = String.format("%02d",hours);
        //Final date/time value to be compared
        String myCurrentTime = formatted +"T"+currentHour;
        System.out.println("Current Time/Hour in YYYY-MM-DDTHH format :"+ myCurrentTime);
        
        //Buffer to store the API's answer
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        
       try 
        {
            String inputLine;//this will hold on line at a time from the buffer 
            //initialize strings to empty
            String startTime, endTime, temperature, windspeed,winddirection,icon, detailedforecast ="";
               
              //While loop to read line by line the input buffer data
            while ((inputLine = in.readLine()) != null) {

            /* When looping through buffer, if the "startTime" word is found, get the date time value 
            ** and store it int startTime variable. Then read the next line that WE KNOW contains the
            ** endTime value. Store it in endTime variable.
            */
                if (inputLine.contains("\"startTime\""))
                  {
                    String[] parts = inputLine.split("\": \"");
                    //This is to remove the last comma found after the URL "," 
                    startTime = parts[1].substring(0, parts[1].length() - 14);
                    //Read next line that will contain endtime
                    inputLine =in.readLine();
                    parts = inputLine.split("\": \"");
                    //This is to remove the last comma found after the URL "," 
                    endTime = parts[1].substring(0, parts[1].length() - 14);

                    /*
                    ** Check if our current local time is between the date/time frame :
                    ** startTime & EndTime to get appropriate forecast
                    ** endTime < myCurrentTime <= startTime
                    ** if it is proceed to extraction of forecast info
                    ** if not reiterate through the while loop that reads line by line
                    */
                    if(myCurrentTime.compareTo(startTime) >= 0 && myCurrentTime.compareTo(endTime)<0)  
                    {
                    /*************************************************************************************/
                    /* WHEN READING LINES, WE KNOW EXACTLY THE FORMAT AND CAN CALCULATE HOW MANY LINES */
                                /* TO READ TO FIND A SPECIFIC FORECAST INFORMATION */
                    /*************************************************************************************/
                    /*
                    THE FORMAT OF THE RESPONSE MESSAGE BEING: 
                    "number": 1,
                    "name": "Overnight",
                    "startTime": "2017-12-03T01:00:00-06:00",
                    "endTime": "2017-12-03T06:00:00-06:00",
                    "isDaytime": false,
                    "temperature": 66,
                    "temperatureUnit": "F",
                    "temperatureTrend": null,
                    "windSpeed": "8 mph",
                    "windDirection": "SE",
                    "icon": "https://api.weather.gov/icons/land/night/fog?size=medium",
                    "shortForecast": "Patchy Fog",
                    "detailedForecast": "Patchy fog. Mostly cloudy, with a low around 66. Southeast wind around 8 mph."
                    */
                        /* For each information we retrieve:
                        ** We split the line in two with delimiter being the : ":  symbols 
                        ** ie " followed by : followed by a space
                        ** and extract the second part excluding the last character which is a comma.
                        ** Then remove any " character from the string
                        ** EXAMPLE : inputLine = "windDirection": "SE",
                        ** parts = inputLine.split("\": ") gives output : parts[0]= "windDirection":  parts[1]= "SE",
                        ** winddirection = parts[1].substring(0, parts[1].length() - 1) gives winddirection = "SE"
                        ** Finally : winddirection = winddirection.replace("\"","") remove the " and gives winddirection = SE
                        */
                        
                        //Read two lines after endTime line to find the temperature
                        inputLine =in.readLine();
                        inputLine =in.readLine();
                        
                        //temperature 
                        parts = inputLine.split("\": ");
                        temperature = parts[1].substring(0, parts[1].length() - 1);
                        //read one line for temperature unit 
                        inputLine =in.readLine();
                        parts = inputLine.split("\": ");
                        String unit = parts[1].substring(0, parts[1].length() - 1);
                        unit = unit.replace("\"","");
                        _Temperature.setText(temperature+ "\u00b0" +unit); // "\u00b0" is unicode of the ° sign
                        


                        //Read two lines after Temperature
                        inputLine =in.readLine();
                        inputLine =in.readLine();

                        //windSpeed
                        parts = inputLine.split("\": ");
                        windspeed = parts[1].substring(0, parts[1].length() - 1);
                        windspeed = windspeed.replace("\"","");
                        _WindSpeed.setText(windspeed);

                        //Read one line after wind Speed
                        inputLine =in.readLine();

                        //windDirection
                        parts = inputLine.split("\": ");
                        winddirection = parts[1].substring(0, parts[1].length() - 1);
                        winddirection = winddirection.replace("\"","");
                        _WindDirection.setText(winddirection);

                        //Read one line after wind direction
                        inputLine =in.readLine();

                        //icon 
                        parts = inputLine.split("\": ");
                        icon = parts[1].substring(0, parts[1].length() - 1);
                        icon = icon.replace("\"","");
                        
                        /* AFTER EXTRACTION ICON URL
                        ** SET UP NEW HTTP CONNECTION TO GET THE URL ICON
                        ** GET THE IMAGE STREAM INTO THE BUFFERIMAGE and display the received icon in the GUI frame 
                        */
                        try {

                            URL imageurl = new URL(icon);
                            URLConnection urlConnectionIcon = imageurl.openConnection();
                            HttpURLConnection connectionIcon = null;
                            connectionIcon = (HttpURLConnection) urlConnectionIcon;
                            BufferedImage myimage = ImageIO.read(imageurl);
                            JLabel picLabel = new JLabel(new ImageIcon(myimage));
                            picLabel.setBounds(200, 300, 200, 200);
                            panel.add(picLabel);
                            frame.setVisible(true);
                            
                            //CLOSE BUFFER IMAGE AND HTTP CONNECTION for getting the icon
                            myimage.flush();
                            connectionIcon.disconnect();

                        } catch (MalformedURLException ex) {
                            System.out.println("Malformed URL");}

                        //read two lines after icon
                        inputLine =in.readLine();
                        inputLine =in.readLine();

                        //DetailedForecast
                        parts = inputLine.split("\":");
                        detailedforecast = parts[1].substring(0, parts[1].length() - 1);
                        detailedforecast = detailedforecast.replace("\"","");
                        _DetailedForecast.setText(detailedforecast);
                        
                    }//end of if comparing local date/time
                  
                  }////end of if line contains the word StartTime
                            
            }//end while loop that reads line by line

        }//end of 2nd try block
            
            catch (IOException e){}
            
            //Finally close the streams
            finally {
                if (in != null)
                {
                  try {
                    in.close();//close buffer
                    connection.disconnect();//disconnect http connection
                    } 
                  catch (IOException e) {}
                }
                    }//end of finally block
    }//end of 1st try block
       
    catch (IOException e)
    {}
       }//end of connect_NWS method

    //main method used to instanciate the Weather class
    public static void main(String[] args) throws Exception  { 
            new Weather();
            
            
    }//end of main method
    
/* 
** This method is called if the refresh button is clicked, the connect_NWS() is called 
** and new forecast is retrieved for the same longitude/latitude
*/
  @Override
  public void actionPerformed(ActionEvent e) {
            clicked = true; 
            //Make sure what triggered the event is clicking on refresh button
            if ((e.getSource() == button))
                    {try {
                        connect_NWS();//Reconnect to Nationa Weather Service API and extract forecast information
                        clicked = false;//set the boolean of wheteher the refresh button was clicked back to false
                } catch (ParseException ex) {
                    Logger.getLogger(Weather.class.getName()).log(Level.SEVERE, null, ex);
                }
}
  }
}
