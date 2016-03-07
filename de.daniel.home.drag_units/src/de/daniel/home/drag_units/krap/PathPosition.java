package de.daniel.home.drag_units.krap;

public class PathPosition extends Position{
	
	private double ax;
	private double ay;

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
}
