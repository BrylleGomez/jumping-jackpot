import java.io.IOException;

public class Driver {

	public static void main(String[] args) throws IOException, InterruptedException {
			
		System.out.println("Welcome to Jumping Jackpot!");
		System.out.println("Select Game Mode: ");
		System.out.println("1 - Easy");
		System.out.println("2 - Hard");
		int x = System.in.read();	// user input for game difficulty
		x = x - 48;					// convert from char to int
		System.out.println("Your input:" + x);
		
		Thread t1;
		switch (x) {	// selects game difficulty based on user input
		case 1: 
			System.out.println("Game difficulty set to easy!");
			EasyGame easy = new EasyGame();
			t1 = new Thread(easy);
			t1.start();
			break;
			
		case 2:
			System.out.println("Game difficulty set to hard!");
			HardGame hard = new HardGame();
			t1 = new Thread(hard);
			t1.start();
			break;
			
		default:
			;
		}
			
	}
	
	
	
}
