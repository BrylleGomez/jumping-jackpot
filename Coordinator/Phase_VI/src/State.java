
public interface State {

	public void prev(Game context);
	public void next(Game context);
	public void printStatus();
	
}
