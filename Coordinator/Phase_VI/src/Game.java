import java.io.IOException;

public abstract class Game implements Runnable {
	
	// Attributes
	int score = 0;											// keeps track of the score
	int multiplier = 1;										// keeps track of the multiplier
	boolean jumped = false;									// keeps track of whether player is in mid-air
	boolean bottomLED = false;								// keeps track of whether LEDs are in the area to be jumped over
	boolean scored = false;									// keeps track of whether player has scored or not
	boolean stop = false;									// keeps track of whether game has ended
	boolean harder = false;
	
	// Call-back Methods (called by proxies)
	synchronized void jumped() throws IOException { 		// called by acm proxy
		jumped = true;
	}
	synchronized void landed() throws IOException { 		// called by acm proxy
		jumped = false;
	}
	synchronized void bottomLED1() throws IOException { 	// called by led proxy
		bottomLED = true;
	}
	synchronized void bottomLED2() throws IOException { 	// called by led proxy
		bottomLED = false;
	}		
	void stop() throws IOException {						// called by led proxy
		stop = true;
	}
	
	// Other Methods
	void start(Proxy p) throws IOException {				// signal proxies to start game
		p.send_msg(new msg((byte) 4));
	}
	void flashLED3(ProxyLED p) throws IOException {	// signal led proxy to flash LED lights 3 times
		p.send_msg(new msg((byte) 3));
	}
	abstract void setspeed(ProxyLED p) throws IOException; 	// non-common method of Template pattern
	abstract void resetDifficulty(ProxyLED p);				// non-common method of Template pattern; strategy pattern method (changes behavior of end devices)
	abstract void increaseDifficulty(ProxyLED p);			// non-common method of Template pattern; strategy pattern method (changes behavior of end devices)
	
	// State Design Pattern
	private State state = new WaitingState();
	public void previousState() { state.prev(this); }
	public void nextState() { state.next(this); }
	public void printStatus() { state.printStatus(); }
	public void setState(State  state) { this.state= state; } 
	
	// Run Method
	@Override
	public void run() {	// recipe for template pattern (inherited by EasyGame and HardGame)
		
		try { // try block catches any exception
			
			Debug.tracefile("debugtrace.txt"); // Save console messages to a file (located in project directory)
			
			// Announce ED numbers
			Debug.trace("LED is ED0!");
			Debug.trace("ACM is ED1!");
			
			SerialPortHandle sph = new SerialPortHandle("COM9"); 							// Initiate serial port for MS Windows
			////SerialPortHandle sph = new SerialPortHandle("/dev/tty.usbserial-A601ERES"); // Initiate serial port for Mac OS
			Dispatcher d = new Dispatcher(sph); // Create a dispatcher
			
			// Create and start proxy objects
			ProxyLED led_proxy = new ProxyLED("led_proxy", 0, System.in, System.out, d, this); // Proxy object for the LED strip (end device 0)
			ProxyACM acm_proxy = new ProxyACM("acm_proxy", 1, System.in, System.out, d, this); // Proxy object for the accelerometer (end device 1)
			
			// Create sound class
			Sound sound = new Sound();
			
			this.flashLED3(led_proxy);		// make LED flash 3 times to indicate game start
			this.start(led_proxy);			// start LED light sequence
			this.start(acm_proxy);			// start ACM value reading
			this.setspeed(led_proxy);		// set initial LED speed; non-common recipe of template pattern
			
			while(true) {
				
				// Game Over
				if (stop) {
					sound.gameOver();
					System.out.println("Game Over! Final score = " + score);
					break;
				}
				
				// Waits for 0 or 1 input from System.in to set speed to slow or fast
				if (System.in.available() > 0) {
					int x = (int)System.in.read();
					if (x != 10 && x != 13) {		// discard newline and carriage return bytes
						x -= 48;					// convert from ASCII
						Debug.trace("Input: " + x);
						switch(x) {
						case 1:
							led_proxy.setSpeed(new SlowSpeed());
							led_proxy.performSpeed();
							break;
						case 2:
							led_proxy.setSpeed(new FastSpeed());
							led_proxy.performSpeed();
							break;
						case 3:
							led_proxy.setSpeed(new MaxSpeed());
							led_proxy.performSpeed();
							break;
						default:
							;
						}	
					}
				}
				
				// Game Logic
				if (this.bottomLED) {							// if LEDs are in the bottom 
					//Debug.trace("Test");
					while (!this.jumped && this.bottomLED) {	// wait for either a jump event or the LED leaving bottom area
						Thread.sleep(1);						// add negligible delay
					}
					
					if (this.jumped) {							// if jump event detected
						score += 10*multiplier;					// increment score 
						multiplier++;							// increment multiplier
						sound.goodJump();						// play relevant sound
						System.out.println("GOOD jump! \t Score = " + score + "\t||\t" + "New multiplier = " + multiplier);
						this.scored = true;						// set flag
					}
					if (!this.bottomLED && !this.scored) {		// if LED leaves bottom area and no jump detected
						if (score > 0)	score -= 10;			// decrement score by 10 if positive
						multiplier = 1;							// reset multiplier to 1
						harder = false;
						this.resetDifficulty(led_proxy);
						sound.failJump();						// play relevant sound
						System.out.println("FAIL jump! \t Score = " + score + "\t||\t" + "New multiplier = " + multiplier);
					}
					this.bottomLED = false;						// reset flag
					this.jumped = false;						// reset flag
					scored = false;								// reset flag
				}
				
				// Changing Difficulty
				if (multiplier == 5 && !harder) {
					this.increaseDifficulty(led_proxy);
					System.out.println("Reached multiplier 5! Increasing difficulty...");
					harder = true;
				}
				
			}
			
		} catch (Exception e) { // terminate the program if exception is caught
			e.printStackTrace();
			System.exit(1);
		}
		
	}

}

