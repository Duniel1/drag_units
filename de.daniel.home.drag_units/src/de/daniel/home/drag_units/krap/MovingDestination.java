package de.daniel.home.drag_units.krap;

public class MovingDestination implements IMovingDestination {
	private Position positionOnce;

	public Position getOnce() {
		return positionOnce;
	}

	public void setOnce(Position once) {
		this.positionOnce = once;
	}

	public MovingDestination(double x, double y) {
		positionOnce = new Position(x, y);
	}

	@Override
	public Position getNextPosition(Position p) {
		if (positionOnce.equals(p))
			return null;
		else
			return positionOnce;
	}

	@Override
	public Position getPosition() {
		return positionOnce;
	}
}
