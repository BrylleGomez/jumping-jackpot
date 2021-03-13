import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProxyACM extends Proxy {
	
	// Constructor
	ProxyACM(String name, int id, InputStream in, OutputStream out, Dispatcher d, Game g) throws IOException {
		super(name, id, in, out, d, g);
		// TODO Auto-generated constructor stub
	}
	
	public void process_message(msg m) throws IOException {
		
		if (m.getId() == id) {				// check if proxy is intended recipient of message broadcasted by dispatcher
			
			byte payload = m.getPayload();	// extract payload of message
			//Debug.trace(this.name+": Message "+payload+" received from dispatcher");
		    
		    switch(payload) {				// perform an action based on payload (instruction)
		    case 1:
		    	g.jumped();					// set jumped attribute of Game thread to true
		    	break;
		    case 2:
		    	g.landed();					// set jumped attribute of Game thread to false
		    	break;
		    default:
		    	;
		    }
			
		}
		
		
    }	       
}

