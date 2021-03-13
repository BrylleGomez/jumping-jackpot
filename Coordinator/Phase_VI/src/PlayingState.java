
public class PlayingState implements State {

	@Override
	public void prev(Game context) {
		context.setState(new WaitingState());
	}

	@Override
	public void next(Game context) {
		context.setState(new GameOverState());
	}

	@Override
	public void printStatus() {
		System.out.println("Game is being played...");
	}

}
