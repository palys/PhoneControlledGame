package maze;

import game.Move;

public class Move2D {

	private final float x;
	
	private final float y;
	
	private final static float g = 9.81f;
	
	private Move2D(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public static Move2D of(Move move) {
		return of(move, 1.0f);
	}
	
	public static Move2D of(Move move, float scale) {
		return of(move, scale, scale);
	}
	
	public static Move2D of(Move move, float scalex, float scaley) {
		
		float[] acc = move.acceleration();
		
		float ax = acc[0];
		float ay = acc[1];
		
		float dx = ax;
		float dy = ay + g;
		
		float x = dx * scalex * move.durationInSecconds();
		float y = (-dy) * scaley * move.durationInSecconds();
		
		return new Move2D(x, y);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
}
