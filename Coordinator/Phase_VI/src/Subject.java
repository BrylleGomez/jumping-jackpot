import java.io.IOException;

public interface Subject {

	public void register(Proxy proxy) throws IOException;
	public void remove(Proxy proxy);
	public void notifyObservers() throws IOException;
		
}
