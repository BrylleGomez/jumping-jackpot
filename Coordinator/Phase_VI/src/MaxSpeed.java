import java.io.IOException;

public class MaxSpeed implements SpeedBehavior {

	public void speed(ProxyLED proxyled)
	{
		try {
			for (int i = 0; i < 5; i++) {					// send instruction 3 times to increase chances of being received correctly
				proxyled.send_msg(new msg((byte)(5)));		// send instruction 5 to LED
				//Debug.trace("LED speed set to MAX");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
