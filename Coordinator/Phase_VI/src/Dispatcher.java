import java.io.IOException;
import java.util.ArrayList;
import jssc.SerialPort;
import jssc.SerialPortException;
////import java.util.Random; // only for testing

public class Dispatcher implements Runnable, Subject {

	// Attributes
	ArrayList<Proxy> proxies; 
	SerialPortHandle sph;
	msg message; // placeholder for the msg for observer pattern
	
	// Constructor
	Dispatcher(SerialPortHandle sph) {
		
		this.sph = sph;
		message = new msg();											// create the message data member
		proxies = new ArrayList<Proxy>(); 								// create an array list to remember proxies 
		new Thread(this).start();										// start the thread		
		
	}
	
	// Add a new proxy to the list of proxies known by the dispatcher
	public void register(Proxy proxy) throws IOException {
		//Debug.trace("Adding " + proxy.name + " as ED" + proxies.size() + " to list of proxies known to " + this);
		proxies.add(proxy); 											// adds the proxy object to proxies list
	}
	
	// Remove a proxy from the list
	public void remove(Proxy proxy) {
		int i = proxies.indexOf(proxy);
		if (i >= 0) proxies.remove(i);
	}
	
	// Broadcast the message to all proxies (observers)
	public void notifyObservers() throws IOException {
		
		if (proxies.size() > 0) { 	
			for (int i = 0; i < proxies.size(); i++) 
				proxies.get(i).process_message(new msg(message.m));		// broadcast the msg to all proxies registered to the dispatcher
		}
		
	}
	
	// Call this method if a message is received by dispatcher from ED
	void mChanged() throws IOException {
		notifyObservers();
	}

	
	// This is how the dispatcher sends messages through the (ZigBee) Serial port (in 4 steps)
	public void send_msg(Proxy proxy, msg m) throws IOException {
		
		// (1) Check if there are any proxies registered in the dispatcher table
		if (proxies.size() == 0) {
			Debug.trace("Dispatcher ERROR: Message m="+m.getPayload()+" received from "+proxy.name+", no existing ED to send to!");
			return;
		}
		
		// (2) Check if the proxy trying to send a message is actually in the dispatcher list of proxies
		int index; 
		boolean found = false;
		for (index = 0; index < proxies.size(); index++) {
			if (proxies.get(index) == proxy) { 							
				found = true;											// If it is, retrieve its associated end device ID (ED#) and store in index
				break;
			}
		}
		if (!found) {
			Debug.trace("Dispatcher ERROR: Message m="+m.getPayload()+" received from "+proxy.name+", sending proxy not registered in dispatcher!");
			return;														// If not, ignore the message send request
		}
		
		// (3) Embed the ID onto the message and create the "byte version" of the message
		m.setId((byte)index);
		
		// (4) Send the byte version of the message through Serial port
		String s = Byte.toString(m.m);									// Convert this byte into string
		sph.printLine(s);												// Because the SerialPort readLine and printLine methods only take in strings
																		// to be changed later on because this conversion is redundant
		//Debug.trace("Dispatcher: Message m="+m.getPayload()+" received from "+proxy.name+", sending to ED"+m.getId());

	}

	// Dispatcher run method
	@Override
	public void run() {
		
		try { //try block that catches any exception
			
			String buf = "";
			
			while(true) { 												// The run method of the dispatcher simply watches the serial port for any incoming bytes
																		// and processes these bytes before passing them to the proxy (through callback method)	
				// Wait for incoming bytes, read if any
				buf = sph.readLine(); 									// watch the serial port for any incoming input
				////byte r = (byte)(Math.abs(rand.nextInt() % 128)); 	// generates a random byte r from 0 to 127 (inclusive)
				
				if (buf != null) {
					// Split received byte (message) into ID and payload components
					byte r = Byte.parseByte(buf); 
					message.m = r;
					mChanged();
				} else Debug.trace("Incoming message discarded: no proxies registered in the dispatcher!");	
												
				
			}	
			
		} catch (Exception e) { //if any exception is caught, terminate program
			e.printStackTrace();	
			System.exit(1);
		}	
			
	} //end run method
		
}








// Serial Port Handle Class
/*
Author: Mr. Suresh Radder
Date: 14 Oct, 2017
*/
class SerialPortHandle {

 SerialPort sp;
 String path;

 public SerialPortHandle(String path) {
     super();
     this.sp = new SerialPort(path);
     ;
     this.path = path;
     try {
         sp.openPort();
         sp.setParams(9600, 8, 1, 0);
     } catch (SerialPortException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
     }// Open serial port

 }

 public String readLine() {
     StringBuffer string = new StringBuffer();
     boolean quit = false;
     while (!quit) {
         byte[] buffer;
         try {
             buffer = sp.readBytes(1);
             // Read 1 bytes from serial port
             if (buffer[0] != 13) {
                 string.append((char) (buffer[0]));
             }
             if (buffer[0] == 13) {
                 // Read the following 10 character
                 sp.readBytes(1);
                 quit = true;
             }
	
         } catch (SerialPortException e1) {
             // TODO Auto-generated catch block
             e1.printStackTrace();
         }

     }
     return string.toString();
 }

 public void printLine(String s) {
     byte byteArray[] = s.getBytes();
     try {
         sp.writeBytes(byteArray);
         sp.writeByte((byte) '\n');
     } catch (SerialPortException e1) {
         // TODO Auto-generated catch block
         e1.printStackTrace();
     }

 }

}