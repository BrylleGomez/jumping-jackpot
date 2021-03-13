import java.io.IOException;

public class FastSpeed implements SpeedBehavior {

	public void speed(ProxyLED proxyled)
	{
		try {
			for (int i = 0; i < 5; i++) {					// send instruction 3 times to increase chances of being received correctly
				proxyled.send_msg(new msg((byte)(2)));		// send instruction 2 to LED
				//Debug.trace("LED speed set to FAST");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
