import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProxyLED extends Proxy{

	SpeedBehavior speed;
	
	// Constructor
	ProxyLED(String name, int id, InputStream in, OutputStream out, Dispatcher d, Game g) throws IOException {
		super(name, id, in, out, d, g);
	}
	
	public void setSpeed(SpeedBehavior sp)
	{
		this.speed = sp;
	}
	
	public void performSpeed()
	{
		speed.speed(this);
	}
	
	public void process_message(msg m) throws IOException {
		
		if (m.getId() == id) {					// check if proxy is intended recipient of message broadcasted by dispatcher
			
			byte payload = m.getPayload();		// extract payload of message
		    //Debug.trace(this.name+": Message "+payload+" received from dispatcher");
		    
		    switch(payload) {					// perform an action based on payload (instruction)
		   	case 1:
		    	g.bottomLED1();					// set bottomLED attribute of Game thread to true
		    	break;
		    case 2:
		    	g.bottomLED2();					// set bottomLED attribute of Game thread to false
		    	break;    
		    case 3:
		    	System.out.println("GOT MESSAGE FOR GAME OVER!");
		    	g.stop();
		    default:
		    	;
		    }	
			
		}
		       
	}
	
}
