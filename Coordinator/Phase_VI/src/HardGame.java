
public class HardGame extends Game implements Runnable{
	
	// Non-common method of Template pattern for Game class
	void setspeed(ProxyLED p) {
		p.setSpeed(new FastSpeed());
		p.performSpeed();
	}
	
	void increaseDifficulty(ProxyLED p) {
		p.setSpeed(new MaxSpeed());
		p.performSpeed();
	}

	@Override
	void resetDifficulty(ProxyLED p) {
		p.setSpeed(new FastSpeed());
		p.performSpeed();
	}
}