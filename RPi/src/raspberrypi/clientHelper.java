package raspberrypi;
import java.io.*;
import java.net.*;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

public class clientHelper  implements Runnable {
	
	public static GpioPinDigitalOutput triggerPin ;
	public  static GpioPinDigitalInput    echoPin ;
	
	final static GpioController gpio = GpioFactory.getInstance();
	
	
	@SuppressWarnings("deprecation")
	public void run() {
		
		
		System.out.println("clientHelper Thread started");
	    clientHelper.initializeTriggerpin();
		int serverPort = 9999;
		InetAddress host = null;
		while(true){
			
			byte[] ipAddr = new byte[] { (byte) 192, (byte) 168, (byte)1, (byte) 187 };
            InetAddress hos=null;
			try {
				hos = InetAddress.getByAddress(ipAddr);
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} 
			
			    Socket sock=null;
			    ObjectOutputStream outputStream=null;
				try {
					sock = new Socket(hos,50000);
					 outputStream  = new ObjectOutputStream(sock.getOutputStream());	
				} 
				catch (IOException e1) {
					System.out.println(" ClientHelper  try hub not alive");
					Main.startBackgroundTask=true;
				} 

	       
			
			try {
		  
			triggerPin.high(); // Make trigger pin HIGH
			Thread.sleep((long) 0.01);// Delay for 10 microseconds
			triggerPin.low(); 
		
			while(echoPin.isLow()){ 
				
			}
			long startTime= System.nanoTime(); 
			while(echoPin.isHigh()){ 
				
			}
			long endTime= System.nanoTime(); 
			double distance = (((endTime-startTime)/1e3)/2) / 29.1 ;
	
		    
			
		
            if(Main.globalOverride=="0")
            {
            	System.out.println("I AM THE KING");
            	if(distance > 50 && distance <=120)
    			{
    				//switch on  green light 
    				if(ledController.greenpin.isLow())
    				{
    					ledController.greenpin.toggle();
    				}
    				
    				if(ledController.redpin.isHigh())
    				{
    					ledController.redpin.toggle();
    				}
    				
    				
    				if(ledController.yellowpin.isHigh())
    				{
    					ledController.yellowpin.toggle();
    				}
    				
    			
    				
    			}//end if green range
    			else if(distance <= 50 && distance >15)
    			{
    				//yellow
    				if(ledController.greenpin.isHigh())
    				{
    					ledController.greenpin.toggle();
    				}
    				
    				if(ledController.redpin.isHigh())
    				{
    					ledController.redpin.toggle();
    				}
    				
    				
    				if(ledController.yellowpin.isLow())
    				{
    					ledController.yellowpin.toggle();
    				}
    				
    			}//end if yellow range
    			
    			
    			else if(distance >= 2 && distance <= 15)
    			{
    				//switch on  red light 
    				if(ledController.greenpin.isHigh())
    				{
    					ledController.greenpin.toggle();
    				}
    				
    				if(ledController.redpin.isLow())
    				{
    					ledController.redpin.toggle();
    				}
    				
    				
    				if(ledController.yellowpin.isHigh())
    				{
    					ledController.yellowpin.toggle();
    				}
    				
    				
    				
    				
    				
    			}//end if red range
    			else
    			{
    				//switch off all led 
    				if(ledController.greenpin.isHigh())
    				{
    					ledController.greenpin.toggle();
    				}
    				
    				if(ledController.redpin.isHigh())
    				{
    					ledController.redpin.toggle();
    				}
    				
    				
    				if(ledController.yellowpin.isHigh())
    				{
    					ledController.yellowpin.toggle();
    				}
    				
    			}
            	
            }
            else
            {
            	System.out.println("Do Nothing");
            }
			
			
			
			  String ledStatus = ledController.getLedStatus();
			  String macaddress = PiSystemInformation.getMacAddress() ;
			   StringBuilder sb = new StringBuilder() ;
	      	   sb.append("pi1");
	      	   sb.append("#");
	      	   sb.append("update");
	      	   sb.append("#");
	      	   sb.append(macaddress);  //mac value
	      	   sb.append("#");
	      	   sb.append("override");
	      	   sb.append("#");
	      	   sb.append(Main.globalOverride);
	           sb.append("#");
	      	   sb.append(ledStatus);
	      	   sb.append("#");
               sb.append("sensor");
               sb.append("#");
               sb.append(String.valueOf(distance)); //ultrasonic sensor max value 
               
               
                
	            outputStream.writeObject(sb.toString()); 
	            outputStream.flush();
	            System.out.println("sent updates");
			    System.out.println(sb.toString());
			    Thread.sleep(3000);
			
		 }catch(UnknownHostException ex) {
			 System.out.println(" clientHelper 2  unknownHostException");
		}
		catch(IOException e){
			System.out.println(" clientHelper IOEXCEPTION");
			Main.startBackgroundTask=true;
		} catch (InterruptedException e) {
			System.out.println(" clientHelper InterruptedException");
			Main.startBackgroundTask=true;
		}
		}
     
	}//end run 
	
	public static void initializeTriggerpin()
	{
		if(Main.isTriggerInitialized)
		{
		triggerPin =  gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04); // Trigger pin as OUTPUT
		echoPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05,PinPullResistance.PULL_DOWN); // Echo pin as INPUT
		Main.isTriggerInitialized=false;
		}
		
	}

}
