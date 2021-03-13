
public class GameOverState implements State {

	@Override
	public void prev(Game context) {
		context.setState(new PlayingState());
	}

	@Override
	public void next(Game context) {
		context.setState(new WaitingState());
	}

	@Override
	public void printStatus() {
		System.out.println("GameOVER!");
	}

}
