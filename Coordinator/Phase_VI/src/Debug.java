import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Debug {
	
	static BufferedOutputStream f=null;
		
	// Create a file stream to a file that saves the console messages
	static void tracefile(String s) throws FileNotFoundException {
		f = new BufferedOutputStream(new FileOutputStream(s));
	}
	// Output string s onto the console and onto a file (for later use)
	static void trace(String s) throws IOException {
		System.out.println(s); 			// output s to console
		
		if(f!=null) {
			f.write(s.getBytes()); 		// convert s from string to byte[] form and then write to a file
			f.write('\n');				// write newline to a file
			f.flush();					// flush output stream
		}
	}

}