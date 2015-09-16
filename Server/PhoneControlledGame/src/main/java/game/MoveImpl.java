package game;

public class MoveImpl implements Move {
	
	private float[] acc;
	
	private float duration;

	public MoveImpl(float[] acc, float duration) {
		super();
		this.acc = acc;
		this.duration = duration;
	}

	@Override
	public float[] acceleration() {
		// TODO Auto-generated method stub
		return acc;
	}

	@Override
	public float durationInSecconds() {
		// TODO Auto-generated method stub
		return duration;
	}

}
