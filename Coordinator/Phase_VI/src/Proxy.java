import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Proxy implements Runnable, Observer {
	
	// Attributes
	String name;
	int id;
	//InputStream in;
	//OutputStream out; 
	Dispatcher d;
	Game g;
	
	// Constructor
	Proxy(String name, int id, InputStream in, OutputStream out, Dispatcher d, Game g) throws IOException{
		
		// initialize attributes
		this.name = name;
		this.id = id;
		//this.in = new BufferedInputStream(in);
		//this.out = new BufferedOutputStream(out);
		this.d = d;
		this.g = g;
		
		d.register(this); 										// register yourself as a proxy with dispatcher d
		
		new Thread(this).start(); 								// start the thread
		
	}
	
	// This is how the proxy passes messages to the dispatcher
	void send_msg(msg m) throws IOException {
		//Debug.trace(this.name+": Sending "+m.getPayload()+" to the dispatcher");
		d.send_msg(this, m); 									// send to the dispatcher a pointer to the sending proxy object along with the message
	}
 
	// This is how the proxy receives messages from the dispatcher
	// This is the callback function that the dispatcher calls
	abstract public void process_message(msg m) throws IOException;
	
	// Proxy run method
	public void run() {		
		
		while(true);
		
	} //end run method

}