
public class EasyGame extends Game implements Runnable{
	
	// Non-common method of Template pattern for Game class
	void setspeed(ProxyLED p) {
		p.setSpeed(new SlowSpeed());
		p.performSpeed();
	}

	void increaseDifficulty(ProxyLED p) {
		p.setSpeed(new FastSpeed());
		p.performSpeed();
	}

	@Override
	void resetDifficulty(ProxyLED p) {
		p.setSpeed(new SlowSpeed());
		p.performSpeed();
	}
}
