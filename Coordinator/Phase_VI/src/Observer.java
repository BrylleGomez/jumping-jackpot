import java.io.IOException;

public interface Observer {

	// Serves as the update() method of the observer in observer pattern
	public void process_message(msg m) throws IOException; 
	
}
