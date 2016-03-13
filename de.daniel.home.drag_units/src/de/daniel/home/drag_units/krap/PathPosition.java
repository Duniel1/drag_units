package de.daniel.home.drag_units.krap;

public class PathPosition extends Position{
	
	private double ax;
	private double ay;
	private Direction oppositeDirection;
	private Direction direction;

	public PathPosition(double x, double y, double ax, double ay) {
		super(x, y);
		this.ax = ax;
		this.ay = ay;
	}

	public double getAx() {
		return ax;
	}

	public void setAx(double ax) {
		this.ax = ax;
	}

	public double getAy() {
		return ay;
	}

	public void setAy(double ay) {
		this.ay = ay;
	}
	public Direction getOppositeDirection() {
		return oppositeDirection;
	}

	public void setOppositeDirection(Direction direction) {
		this.oppositeDirection = direction;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public enum Direction{
		LEFT, RIGHT, UP, DOWN, DIRECT;
	}
}
