package PlayerState;

public class PlayerStateMachine {
	protected PlayerState currentState;
	protected void initialize(PlayerState _startState) {
		currentState=_startState;
		currentState.enter();
	}
	public void changeState(PlayerState _newState) {
		//System.out.println(currentState.animBoolName +currentState.player.isMy);
		currentState.exit();
		currentState=_newState;
		currentState.enter();
	}
	public PlayerState getCurrentState() {
		return currentState;
	}
}
