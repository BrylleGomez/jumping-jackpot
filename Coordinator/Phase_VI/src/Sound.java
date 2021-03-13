import java.io.*;
import sun.audio.*; //import the sun.audio package

public class Sound { 									// A public class Sounds which will allow us to add more excitement to the game
														// by playing sounds after the player scores a point or the game is over.
	public void goodJump() throws Exception { 			// Method which plays the “jump” sound when a player jumps correctly

		String jump = "C:\\Users\\bryll\\OneDrive\\root.recent\\Academic and Career\\AUS\\Spring 2019\\COE 312\\Project\\Phase 6\\Coordinator\\Phase_VI\\src\\Sounds\\point.wav"; 						// Obtain the file name “point.wav”
		InputStream in = new FileInputStream(jump); 	// Creates an input file stream to read directly from the file
														// “jump”
		AudioStream audioStream = new AudioStream(in);	// Creates an Audio input stream from the given file (Wraps the
														// InputStream into an AudioStream”
		AudioPlayer.player.start(audioStream); 			// The music begins playing when we call this method.

	}
	
	public void failJump() throws Exception { 			// Method which plays the “jump” sound when a player jumps correctly
		
		String jump = "C:\\Users\\bryll\\OneDrive\\root.recent\\Academic and Career\\AUS\\Spring 2019\\COE 312\\Project\\Phase 6\\Coordinator\\Phase_VI\\src\\Sounds\\die.wav"; 						// Obtain the file name “die.wav”
		InputStream in = new FileInputStream(jump); 	// Creates an input file stream to read directly from the file
														// “jump”
		AudioStream audioStream = new AudioStream(in);	// Creates an Audio input stream from the given file (Wraps the
														// InputStream into an AudioStream”
		AudioPlayer.player.start(audioStream); 			// The music begins playing when we call this method.

	}

	public void gameOver() throws Exception { 			// Method which plays the “gameOver” sound when the game is over

		String gameOver = "C:\\Users\\bryll\\OneDrive\\root.recent\\Academic and Career\\AUS\\Spring 2019\\COE 312\\Project\\Phase 6\\Coordinator\\Phase_VI\\src\\Sounds\\gameover.wav"; 				// Obtain the file name “gameover.wav”
		InputStream in = new FileInputStream(gameOver); // Creates an input file stream to read directly from the file
														// “gameOver”

		AudioStream audioStream = new AudioStream(in);	// Creates an Audio input stream from the given file (Wraps the
														// InputStream into an AudioStream”

		AudioPlayer.player.start(audioStream); 			// The music begins playing when we call this method.

	}

}