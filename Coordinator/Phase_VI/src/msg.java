import java.io.IOException;

// msg "Message" objects contain ID (ranging from 0 to 3) and payload (ranging from 0 to 63)
public class msg {
	
	byte m;													// contains the 1-byte message
	
	msg() {													// msg default constructor 
		this.m = 0;
	}
	
	msg(byte m) {											// msg constructor 
		this.m = m;
	}

	public byte getId() {									// ID getter
		return (byte)((m >> 6) & 3);						// retrieve ID by shifting the byte to the right 6 times (retrieve 2 leftmost bits) and AND with 00000011 to get rid of sign
	}

	public void setId(byte id) throws IOException{			// ID setter
		if (0 <= id && id <= 3) {							// check if specified id is between 0 and 3
			m = (byte)(m & 63);								// clear existing ID of msg by ANDing it with 00111111 (retrieve 00pppppp)
			m = (byte)(m | (id << 6));						// embed ID onto msg by ORing ID-less msg with ID (00pppppp | ii000000)
		}
		else {
			Debug.trace("Exception in msg id setter: Specified ID out of range!");
			throw new IllegalArgumentException(); 			// notify calling thread about exception
		}
	}

	public byte getPayload() {								// Payload getter
		return (byte)(m & 63);								// retrieve payload by ANDing byte with 00111111 (retrieve 6 rightmost bits)
	}

	public void setPayload(byte pl) throws IOException{		// Payload setter
		if (0 <= pl && pl <= 63) {							// check if specified payload is between 0 and 63
			m = (byte)(m & -64);							// clear existing payload of msg by ANDing it with 11000000 (retrieve ii000000)
			m = (byte)(m | pl);								// embed payload onto msg by ORing payload-less msg with payload (ii000000 | 00pppppp)
		}
		else {
			Debug.trace("Exception in msg payload setter: Specified payload out of range!");
			throw new IllegalArgumentException(); 			// notify calling thread about exception
		}
	}
	
}
