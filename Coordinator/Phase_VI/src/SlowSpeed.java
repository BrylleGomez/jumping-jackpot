import java.io.IOException;

public class SlowSpeed implements SpeedBehavior {

	public void speed(ProxyLED proxyled)
	{
		try {
			for (int i = 0; i < 5; i++) {					// send instruction 3 times to increase chances of being received correctly
				proxyled.send_msg(new msg((byte)(1)));		// send instruction 1 to LED
				//Debug.trace("LED speed set to SLOW");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
