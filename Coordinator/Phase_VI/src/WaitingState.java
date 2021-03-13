
public class WaitingState implements State {

	@Override
	public void prev(Game context) {
		System.out.println("Game is already waiting to start.");
	}

	@Override
	public void next(Game context) {
		context.setState(new PlayingState());
	}

	@Override
	public void printStatus() {
		System.out.println("Game is waiting to start.");
	}

}
